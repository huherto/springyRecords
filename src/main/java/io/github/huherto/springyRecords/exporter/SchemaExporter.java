package io.github.huherto.springyRecords.exporter;

import static java.lang.String.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import schemacrawler.crawl.SchemaCrawler;
import schemacrawler.schema.Column;
import schemacrawler.schema.Database;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;

public class SchemaExporter {

    private final DataSource ds;

    public SchemaExporter(DataSource ds) {
        this.ds = ds;
    }

    private Database crawl(SchemaCrawlerOptions options) {
        try {
            SchemaCrawler crawler = new SchemaCrawler(ds.getConnection());
            return crawler.crawl(options);
        } catch (SchemaCrawlerException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void export(String schemaName, List<String> tableNames) {

        SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInclusionRule(new RegularExpressionInclusionRule(schemaName));
        Database database = crawl(options);

        for(Table table : database.getTables()) {

            if (tableNames.contains(table.getName())) {

                System.out.print(exportTable(table));

            }
        }

    }

    private void println(PrintWriter out, String fmt, Object...args) {
        out.println(format(fmt, args));
    }

    private String exportTable(Table table) {

        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        println(out, "IF EXISTS ( SELECT * FROM sysobjects WHERE id = OBJECT_ID('%s.%s') )", table.getSchema().getName(), table.getName());
        println(out, "  DROP TABLE %s", table.getName());
        println(out, "GO");
        println(out, "");
        println(out, "CREATE TABLE [%s].[%s] (", table.getSchema().getName(), table.getName());
        for(Column col : table.getColumns()) {

            String line = "";
            String cname = col.getName();
            String ctype = col.getColumnDataType().getName();
            String width = col.getWidth();
            if (width != null) {
                ctype += width;
            }
            String nullable = (col.isNullable() ? "" : "NOT") + " NULL";

            line = format("  [%s] %s %s",cname, ctype, nullable);
            println(out, line+",");
        }

        println(out, ")");
        println(out, "");
        println(out, "GO");
        println(out, "");

        return stringWriter.toString();
    }
}
