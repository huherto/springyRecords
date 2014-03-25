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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import schemacrawler.crawl.SchemaCrawler;
import schemacrawler.schema.Database;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;

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

	private Database database;

    public DataBaseGenerator(DataSource ds, String packageName) {
        this.ds = ds;
        this.packageName = packageName;

        try {
			SchemaCrawler crawler = new SchemaCrawler(ds.getConnection());
			database = crawler.crawl(new SchemaCrawlerOptions());
		} catch (SchemaCrawlerException | SQLException e) {
			throw new RuntimeException(e);
		}
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

    private TableTool createTableTool(DataSource ds, Table table, String basePackage) {
        try {
            TableTool tableTool = createTableTool();
            tableTool.initialize(table, basePackage);
            return tableTool;
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public TableTool createTableTool() {
        return new TableTool();
    }

    public void processTableList(String catalog, String schemaName, List<String> tableNames) {

    	Schema schema = database.getSchema(schemaName);
        DatabaseTool dbTool = new DatabaseTool(packageName);
        for(String tableName : tableNames) {
        	Table table = database.getTable(schema, tableName);

            TableTool tableTool = createTableTool(ds, table, packageName);
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

    public void processAllTables(String catalog, String schemaName) {


        List<String> tableNames = new ArrayList<>();

        for(Table table : database.getTables(database.getSchema(schemaName))) {
        	tableNames.add(table.getName());
        }
        processTableList(catalog, schemaName, tableNames);

    }

}
