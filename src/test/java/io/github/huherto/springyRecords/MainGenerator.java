package io.github.huherto.springyRecords;

import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import io.github.huherto.springyRecords.exporter.SchemaExporter;
import io.github.huherto.springyRecords.generator.DataBaseGenerator;
import io.github.huherto.springyRecords.generator.TableTool;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;

public class MainGenerator {


    private static final String userHome = "/home/hhernandez";

    protected BasicDataSource createMssqlDS(String dbname) {

        String urlPrefix = "jdbc:sqlserver://mssql-hades.dev.1aauto.inc:1433";

        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(com.microsoft.sqlserver.jdbc.SQLServerDriver.class.getName());
        bds.setUrl(urlPrefix+";databaseName="+dbname);
        bds.setUsername("jtaylor");
        bds.setPassword("123");
        bds.setValidationQuery("select 1");
        bds.setTestOnBorrow(true);
        bds.setDefaultReadOnly(true);
        return bds;
    }

    @Test
    public void slotGenerate() {

        DataSource ds = createMssqlDS("slot");

        DataBaseGenerator dbGenerator = new DataBaseGenerator(ds, "com.onea.dal.slot");
        dbGenerator.printInformationSchema("slot\\.dbo");
        dbGenerator.setSourceDir(Paths.get(userHome+"/workspace/onea-ws/src/main/java"));
        dbGenerator.processTableList("slot\\.dbo", slotTables());
    }

    @Test
    public void amsGenerate() {

        DataSource ds = createMssqlDS("ams_1_1a");

        DataBaseGenerator dbGenerator = new DataBaseGenerator(ds, "com.onea.dal.ams");
        dbGenerator.printInformationSchema("ams_1_1a\\.dbo");
        dbGenerator.setSourceDir(Paths.get(userHome+"/workspace/onea-ws/src/main/java"));
        dbGenerator.processTableList("ams_1_1a\\.dbo", amsTables());

    }

    public class GpTableTool extends TableTool {

        @Override
        public boolean ignoreColumn(ColumnTool col) {

            if (super.tableName().contains("SOP10200")) {

                return !Arrays.asList(
                        "ITEMNMBR","XTNDPRCE", "UOFM",
                        "UNITPRCE", "SOPNUMBE", "QUANTITY",
                        "ITEMDESC", "QTYTOINV", "QTYTBAOR",
                        "CMPNTSEQ").contains(col.columnName().toUpperCase().trim());

            }

            if (super.tableName().contains("POP10100")) {

                return !Arrays.asList(
                        "PONUMBER","POSTATUS", "POTYPE",
                        "VENDID", "VENDNAME").contains(col.columnName().toUpperCase().trim());

            }
            if (super.tableName().contains("POP10110")) {

                return !Arrays.asList(
                        "PONUMBER","ORD", "POLNESTA",
                        "POTYPE", "ITEMNMBR", "ITEMDESC",
                        "VENDORID", "VNDITNUM", "VNDITDSC",
                        "QTYORDER", "QTYCANCE"
                        ).contains(col.columnName().toUpperCase().trim());

            }


            return super.ignoreColumn(col);
        }

        @Override
        public String javaFieldName(ColumnTool col) {
            return col.columnName().toUpperCase().trim();
        }

    }

    @Test
    public void gpGenerate() {

        DataSource ds = createMssqlDS("auto");

        DataBaseGenerator dbGenerator = new DataBaseGenerator(ds, "com.onea.dal.gp") {

            @Override
            public TableTool createTableTool() {
                return new GpTableTool();
            }

        };

        dbGenerator.printInformationSchema("auto\\.dbo");
        dbGenerator.setSourceDir(Paths.get(userHome+"/workspace/onea-ws/src/main/java"));

        dbGenerator.processTableList("auto\\.dbo", asList("IV00104", "POP10100", "POP10110"));
    }

    @Test
    public void pickTicketsGenerate() throws FileNotFoundException {

        DataSource ds = createMssqlDS("picktickets");

        List<String> tables = pickTicketsTables();
        sort(tables);
        for(String tname:tables) {
            // System.out.println("            \""+tname+"\",");
        }

    }

    @Test
    public void thGenerate() {

        DataSource ds = createMssqlDS("th");

        DataBaseGenerator dbGenerator = new DataBaseGenerator(ds, "com.onea.dal.th");
        dbGenerator.printInformationSchema("th\\.TH_application");
        dbGenerator.setSourceDir(Paths.get(userHome+"/workspace/onea-ws/src/main/java"));

        dbGenerator.processTableList("th\\.TH_application", thTables());
    }

    private List<String> sortedList(String...args) {
        List<String> list = Arrays.asList(args);
        sort(list);
        for(String tname:list) {
            System.out.println("            \""+tname+"\",");
        }
        return list;
    }

    private List<String> amsTables() {
        return sortedList(
                "country_profile",
                "description",
                "ebay_categories",
                "fitment",
                "images",
                "item_option_values",
                "item_specifics",
                "listing",
                "listing_profile",
                "product_option_values",
                "product_options",
                "sku_class_mapping"
        );
    }

    private List<String> gpTables() {
        return sortedList(
                "IV00101", "IV00102", "IV00103", "IV00104", "IV40600", "IV40700",
                "RM00192",
                "ShipperTracking",
                "SOP10100", "SOP10103", "SOP10107", "SOP10200", "SOP30200",
                "POP10100", "POP10110",
                "SY01200", "SY03900");
    }

    private List<String> thTables() {
        return sortedList(
                "brands",
                "categories",
                "description_types",
                "descriptions",
                "ebay_subtitles",
                "ebay_vehicle_models",
                "ebay_vehicle_model_attr",
                "ebay_vehicle_models_map",
                "engine_types",
                "engine_displacements",
                "fit_trims",
                "fit_engines",
                "fits",
                "fits_years",
                "images",
                "inv_classes",
                "inv_class_descriptions",
                "item_class_descriptions",
                "item_descriptions",
                "item_holds",
                "item_images",
                "item_options",
                "item_option_values",
                "item_qualifiers",
                "item_references",
                "item_sites",
                "item_tags",
                "items",
                "product_descriptions",
                "qualifiers",
                "qualifier_classes",
                "tags",
                "trims",
                "vehicle_makes",
                "vehicle_model_engines",
                "vehicle_model_trims",
                "vehicle_models",
                "vehicle_models_years",
                "vendors"
        );
    }

    private List<String> slotTables() {
        return sortedList("replenish_ticket", "trailer", "transfer_que", "trailer_orders", "replenish_edits", "intersite_move_request");
    }

    private List<String> pickTicketsTables() {
        return sortedList(
                "batch_tickets",
                "options"
        );
    }

    @SuppressWarnings("unused")
    @Test
    public void exportSchemas()
            throws FileNotFoundException {

        DataSource ds = createMssqlDS("picktickets");

        SchemaExporter exporter = new SchemaExporter(ds);
        String  dir = "/home/hhernandez/workspace/onea-ws/src/test/resources/sql";

        exporter.export(dir + "/ams.sql", "ams_1_1a\\.dbo", amsTables());
        if (false) {
            exporter.export(dir + "/auto.sql", "auto\\.dbo", gpTables());
        }
        exporter.export(dir + "/slot.sql", "slot\\.dbo", slotTables());
        exporter.export(dir + "/picktickets.sql", "picktickets\\.dbo", pickTicketsTables());

        ds = createMssqlDS("th");
        exporter = new SchemaExporter(ds);
        exporter.export(dir + "/th.sql", "th\\.TH_application", thTables());

    }


}
