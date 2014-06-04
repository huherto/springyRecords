package io.github.huherto.springyRecords.generator;
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

import schemacrawler.crawl.SchemaCrawler;
import schemacrawler.schema.Database;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;

import com.github.mustachejava.Mustache;
import com.google.common.io.Files;

public class DataBaseGenerator {

    private static final Logger logger = Logger.getLogger(DataBaseGenerator.class);

    private final String packageName;

    private final DataSource ds;

    private Path sourceDir = null;

	private final DatabaseClassWriter databaseClassWriter = new DatabaseClassWriter();

	private final BaseRecordClassWriter baseRecordClassWriter = new BaseRecordClassWriter();

	private final ConcreteRecordClassWriter concreteRecordClassWriter = new ConcreteRecordClassWriter();

	private final BaseTableClassWriter baseTableClassWriter = new BaseTableClassWriter();

	private final ConcreteTableClassWriter concreteTableClassWriter = new ConcreteTableClassWriter();

    public DataBaseGenerator(DataSource ds, String packageName) {
        this.ds = ds;
        this.packageName = packageName;

    }

    private Database crawl(SchemaCrawlerOptions options) {
        try {
            SchemaCrawler crawler = new SchemaCrawler(ds.getConnection());
            return crawler.crawl(options);
        } catch (SchemaCrawlerException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getSourceDir() {
    	String userDir = System.getProperty("user.dir");
    	if (sourceDir == null) {
            sourceDir = FileSystems.getDefault().getPath(userDir, "src", "main", "java");
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

    public void processTableList(String schemaName, List<String> tableNames) {

        SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInclusionRule(new RegularExpressionInclusionRule(schemaName));
        Database database = crawl(options);

        DatabaseTool dbTool = new DatabaseTool(packageName);

        for(Table table : database.getTables()) {

            // TODO: If one of the tables is wrong. This will fail.
            if (tableNames.contains(table.getName())) {

                System.out.println("tableName="+table.getName());
                TableTool tableTool = createTableTool(ds, table, packageName);

                baseRecordClassWriter.makeClass(getSourceDir(), tableTool);
                concreteRecordClassWriter.makeClass(getSourceDir(), tableTool);
                baseTableClassWriter.makeClass(getSourceDir(), tableTool);
                concreteTableClassWriter.makeClass(getSourceDir(), tableTool);
/*
                for(ColumnTool col: tableTool.getColumns()) {
                    System.out.println(String.format("params.put(\"%s,\",rec.%s);",col.columnName(), col.javaFieldName()));
                }
*/
                dbTool.add(tableTool);
            }
        }

        databaseClassWriter.makeClass(getSourceDir(), dbTool);
    }

    public void printInformationSchema(String schemaInclusionRule) {

        SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInfoLevel(SchemaInfoLevel.minimum());
        options.setSchemaInclusionRule(new RegularExpressionInclusionRule(schemaInclusionRule));
        Database database = crawl(options);

    	System.out.println(format("%-20s %-20s", "table_schema", "table_name"));
        for(Table table : database.getTables()) {
        	System.out.println(format("%-20s %-20s", table.getSchema(), table.getName()));
        }
    }

    public void processAllTables(String schemaName) {

        SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInclusionRule(new RegularExpressionInclusionRule(schemaName));
        Database database = crawl(options);

        List<String> tableNames = new ArrayList<>();

        for(Table table : database.getTables(database.getSchema(schemaName))) {
        	tableNames.add(table.getName());
        }
        System.out.println("Found "+tableNames.size()+" tables");
        processTableList(schemaName, tableNames);

    }

}
