package com.github.springRecords.generator;
/*
The MIT License (MIT)

Copyright (c) 2014 <copyright holders>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.io.Files;

public class DataBaseGenerator {

    private static final Logger logger = Logger.getLogger(DataBaseGenerator.class);

    private final String schema;

    private final String catalog;

    private final String packageName;

    DataSource ds;

    public DataBaseGenerator(DataSource ds, String catalog, String schema, String packageName) {
        this.ds = ds;
        this.catalog = catalog;
        this.schema = schema;
        this.packageName = packageName;
    }

    public File sourceDir() {
        return new File(new File(new File(new File(System.getProperty("user.dir")),"src"), "main"), "java");
    }

    public File sourceFile(String packageName, String className) throws IOException {

        File dir =sourceDir();
        String[] segments = packageName.split("\\.");

        for(String seg : segments) {
            dir = new File(dir, seg);
        }

        File sourceFile = new File(dir, className + ".java");
        Files.createParentDirs(sourceFile);

        return sourceFile;
    }

    public void makeDatabase(DatabaseTool dbTool) {
        try {
            File sourceFile = sourceFile(dbTool.basePackageName, dbTool.baseDatabaseClassName());
            if (sourceFile.exists()) {
                sourceFile.delete();
                sourceFile.createNewFile();
            }
            writeCode(sourceFile, baseDatabaseTemplate(), dbTool);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void makeConcreteRecord(TableTool tableTool, String tableName) {
        try {
            String className = tableTool.concreteRecordClassName();
            File sourceFile = sourceFile(packageName, className);
            if (sourceFile.exists()) {
                logger.info("Skipping source "+sourceFile);
                return;
            }

            writeCode(sourceFile, concreteRecordTemplate(), tableTool);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Mustache concreteRecordTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("record.mustache");
    }

    public void makeBaseRecord(TableTool tableTool, String tableName) {
        try {
            File sourceFile = sourceFile(tableTool.baseRecordPackageName(), tableTool.baseRecordClassName());
            if (sourceFile.exists()) {
                sourceFile.delete();
                sourceFile.createNewFile();
            }
            writeCode(sourceFile, baseRecordTemplate(), tableTool);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void makeTable(TableTool tableTool, String tableName) {
        try {
            File sourceFile = sourceFile(tableTool.tablePackageName(), tableTool.tableClassName());
            if (sourceFile.exists()) {
                logger.info("Skipping source "+sourceFile);
                return;
            }
            writeCode(sourceFile, tableTemplate(), tableTool);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Mustache baseDatabaseTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("basedatabase.mustache");
    }

    public Mustache tableTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("table.mustache");
    }

    public Mustache baseRecordTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("baserecord.mustache");
    }

    public void writeCode(File sourceFile, Mustache template, BaseTool tableTool) {
        try {
            logger.info("Creating source "+sourceFile);
            Writer writer = new FileWriter(sourceFile);
            template.execute(writer, tableTool);
            writer.flush();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private TableTool createTableTool(DataSource ds, String catalog, String schema, String tableName, String basePackage) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);

            DatabaseMetaData dbmd = con.getMetaData();

            Statement stmt = con.createStatement();

            String completeTableName = tableName;
            if (schema != null && schema.length() > 0) {
                completeTableName = schema + "." + tableName;
            }
            if (catalog != null && catalog.length() > 0 && !catalog.equals("def")) {
                // 'def' is used in mysql databases. TODO:
                completeTableName = catalog + "." + completeTableName;
            }

            ResultSet rs = stmt.executeQuery("select * from "+completeTableName+" where 1 = 0");
            TableTool tableTool = createTableTool();
            tableTool.initialize(dbmd, rs.getMetaData(), tableName, basePackage);
            DataSourceUtils.releaseConnection(con, ds);
            return tableTool;
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public TableTool createTableTool() {
        return new TableTool();
    }

    public void processTableList(List<String> tableNames) {

        DatabaseTool dbTool = new DatabaseTool(packageName);
        for(String tableName : tableNames) {
            TableTool tableTool = createTableTool(ds, catalog, schema, tableName, packageName);
            makeBaseRecord(tableTool, tableName);
            makeConcreteRecord(tableTool, tableName);
            makeTable(tableTool, tableName);
            dbTool.add(tableTool);
        }

        makeDatabase(dbTool);

    }

    public void processAllTables() {

        JdbcTemplate jt = new JdbcTemplate(ds);
        String sql =
                "select table_name " +
                "from information_schema.tables ";
//                "where table_catalog = ? and table_schema = ?";

        List<String> tableNames = jt.queryForList(sql, String.class);
        if (tableNames.size() != 0) {
            String select = String.format(
                    "select table_name " +
                    "from information_schema.tables " +
                    "where table_catalog = '%s' and table_schema = '%s' ", catalog, schema);
            logger.warn("Can't find tables: SQL ["+select+"]");
        }

        processTableList(tableNames);

    }

}
