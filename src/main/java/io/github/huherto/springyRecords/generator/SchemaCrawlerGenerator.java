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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import io.github.huherto.springyRecords.generator.tools.DatabaseTool;
import io.github.huherto.springyRecords.generator.tools.TableTool;
import schemacrawler.crawl.SchemaCrawler;
import schemacrawler.schema.Database;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;

public class SchemaCrawlerGenerator extends AbstractGenerator {

    private final DataSource ds;

    public SchemaCrawlerGenerator(DataSource ds, String packageName, String databaseClassName) {
        super(packageName, databaseClassName);
        this.ds = ds;
    }

    private TableTool createTableTool(Table table) {
        try {
            TableTool tableTool = createTableTool();
            tableTool.initialize(table, getPackageName());
            return tableTool;
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Database crawl(Connection dbconn, SchemaCrawlerOptions options) {
        try {
            SchemaCrawler crawler = new SchemaCrawler(dbconn);
            return crawler.crawl(options);
        } catch (SchemaCrawlerException e) {
            throw new RuntimeException(e);
        }
    }

    public void processTableList(String schemaName, List<String> tableNames) {

        DatabaseTool dbTool = new DatabaseTool(getPackageName(), getDatabaseClassName() );

        try(Connection con = ds.getConnection()) {
            for(String tableName : tableNames) {

                System.out.println(format("process schema:[%s] table:[%s] ", schemaName, tableName));
                Database database = crawl(con, schemaName, tableName);
                Table table = database.getTable(database.getSchema(schemaName), tableName);
                if (table != null) {
                    TableTool tableTool = createTableTool(table);
                    dbTool.add(tableTool);
                }
                else {

                    System.out.println("not found");
                }
            }
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }

        writeAllClasses(dbTool);
    }

    private Database crawl(Connection con, String schemaName, String tableName) {

        SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInclusionRule(new RegularExpressionInclusionRule(schemaName));
        options.setTableNamePattern("%" + tableName + "%");
        SchemaInfoLevel infoLevel = SchemaInfoLevel.standard();
        options.setSchemaInfoLevel(infoLevel);
        infoLevel.setRetrieveRoutines(false);

        return crawl(con, options);
    }

    public void printInformationSchema(String schemaInclusionRule) {

        try (Connection con = ds.getConnection()) {
            SchemaCrawlerOptions options = new SchemaCrawlerOptions();
            options.setSchemaInfoLevel(SchemaInfoLevel.minimum());
            if (schemaInclusionRule != null) {
                options.setSchemaInclusionRule(new RegularExpressionInclusionRule(schemaInclusionRule));
            }
            Database database = crawl(con, options);

          System.out.println(format("%-20s %-20s", "table_schema", "table_name"));
            for(Table table : database.getTables()) {
              System.out.println(format("%-20s %-20s", table.getSchema(), table.getName()));
            }
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void processAllTables(String schemaName) {
        try (Connection con = ds.getConnection()) {
            SchemaCrawlerOptions options = new SchemaCrawlerOptions();
            options.setSchemaInfoLevel(SchemaInfoLevel.minimum());
            options.setSchemaInclusionRule(new RegularExpressionInclusionRule(schemaName));
            Database database = crawl(con, options);

            List<String> tableNames = new ArrayList<>();

            for(Table table : database.getTables(database.getSchema(schemaName))) {
              tableNames.add(table.getName());
            }
            System.out.println("Found "+tableNames.size()+" tables");
            processTableList(schemaName, tableNames);
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
