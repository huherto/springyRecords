package com.github.springyRecords.generator;
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

import static java.lang.String.format;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
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
import com.github.springyRecords.PrintRowCallbackHandler;
import com.google.common.io.Files;

public class DataBaseGenerator {

    private static final Logger logger = Logger.getLogger(DataBaseGenerator.class);

    private final String packageName;

    private DataSource ds;

    private Path sourceDir = null;

    public DataBaseGenerator(DataSource ds, String packageName) {
        this.ds = ds;
        this.packageName = packageName;
    }

    public Path getSourceDir() {
    	if (sourceDir == null) {
            sourceDir = FileSystems.getDefault().getPath(System.getProperty("user.dir"), "src", "main", "java");
    	}
    	return sourceDir;
    }

    public void setSourceDir(Path path) {
    	this.sourceDir = path;
    }

    public File sourceFile(String packageName, String className) throws IOException {

        String[] segments = packageName.split("\\.");

        File dir = getSourceDir().toFile();
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

    public void makeConcreteRecord(TableTool tableTool) {
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

    public void makeBaseRecord(TableTool tableTool) {
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

    public void makeConcreteTable(TableTool tableTool) {
        try {
            String className = tableTool.concreteTableClassName();
            File sourceFile = sourceFile(tableTool.concreteTablePackageName(), className);
            if (sourceFile.exists()) {
                logger.info("Skipping source "+sourceFile);
                return;
            }

            writeCode(sourceFile, concreteTableTemplate(), tableTool);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void makeBaseTable(TableTool tableTool) {
        try {
            File sourceFile = sourceFile(tableTool.baseTablePackageName(), tableTool.baseTableClassName());
            if (sourceFile.exists()) {
                sourceFile.delete();
                sourceFile.createNewFile();
            }
            writeCode(sourceFile, baseTableTemplate(), tableTool);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Mustache baseDatabaseTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("basedatabase.mustache");
    }

    public Mustache baseTableTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("basetable.mustache");
    }

    public Mustache concreteTableTemplate() {
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
                // 'def' is used in mysql databases.
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

    public void processTableList(String catalog, String schema, List<String> tableNames) {

        DatabaseTool dbTool = new DatabaseTool(packageName);
        for(String tableName : tableNames) {
            TableTool tableTool = createTableTool(ds, catalog, schema, tableName, packageName);
            makeBaseRecord(tableTool);
            makeConcreteRecord(tableTool);
            makeBaseTable(tableTool);
            makeConcreteTable(tableTool);
            dbTool.add(tableTool);
        }

        makeDatabase(dbTool);
    }

    public void printInformationSchema() {

        JdbcTemplate jt = new JdbcTemplate(ds);
        String sql =
                "select table_catalog, table_schema, table_name " +
                "from information_schema.tables ";

    	System.out.println(format("%-20s %-20s %-20s", "table_catalog","table_schema", "table_name"));
        PrintRowCallbackHandler prch = new PrintRowCallbackHandler();
        prch.setFormat("%-20s %-20s %-20s");
        jt.query(sql, prch);
    }

    public void processAllTables(String catalog, String schema) {

        JdbcTemplate jt = new JdbcTemplate(ds);
        String sql =
                "select table_name " +
                "from information_schema.tables "+
                "where table_catalog = ? and table_schema = ?";

        List<String> tableNames = jt.queryForList(sql, String.class, catalog, schema);
        if (tableNames.size() == 0) {
            String select = String.format(
                    "select table_name " +
                    "from information_schema.tables " +
                    "where table_catalog = '%s' and table_schema = '%s' ", catalog, schema);
            logger.warn("Can't find tables: SQL ["+select+"]");
        }

        processTableList(catalog, schema, tableNames);

    }

}
