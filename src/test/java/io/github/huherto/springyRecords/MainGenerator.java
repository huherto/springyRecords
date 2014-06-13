package io.github.huherto.springyRecords;

import static java.util.Arrays.asList;
import io.github.huherto.springyRecords.exporter.SchemaExporter;
import io.github.huherto.springyRecords.generator.DataBaseGenerator;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;

public class MainGenerator {


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

        String userHome = "/home/hhernandez";

        DataBaseGenerator dbGenerator = new DataBaseGenerator(ds, "com.onea.dal.slot");
        dbGenerator.printInformationSchema("slot\\.dbo");
        dbGenerator.setSourceDir(Paths.get(userHome+"/workspace/onea-ws/src/main/java"));
        dbGenerator.processTableList("slot\\.dbo", asList("replenish_ticket", "trailer", "transfer_que", "trailer_orders"));
    }

    @Test
    public void amsGenerate() {

        DataSource ds = createMssqlDS("ams_1_1a");

        String userHome = "/home/hhernandez";

        DataBaseGenerator dbGenerator = new DataBaseGenerator(ds, "com.onea.dal.ams");
        dbGenerator.printInformationSchema("ams_1_1a\\.dbo");
        dbGenerator.setSourceDir(Paths.get(userHome+"/workspace/onea-ws/src/main/java"));

        List<String> tables = Arrays.asList(
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
        Collections.sort(tables);
        for(String tname:tables) {
            // System.out.println("            \""+tname+"\",");
        }
        dbGenerator.processTableList("ams_1_1a\\.dbo", tables);

        SchemaExporter exporter = new SchemaExporter(ds);

        exporter.export("ams_1_1a\\.dbo", tables);
    }


    @Test
    public void pickTicketsGenerate() {

        DataSource ds = createMssqlDS("picktickets");

        String userHome = "/home/hhernandez";

        List<String> tables = Arrays.asList(
                "batch_tickets",
                "options"
        );
        Collections.sort(tables);
        for(String tname:tables) {
            // System.out.println("            \""+tname+"\",");
        }

        SchemaExporter exporter = new SchemaExporter(ds);

        exporter.export("picktickets\\.dbo", tables);
    }




    @Test
    public void thGenerate() {

        DataSource ds = createMssqlDS("th");

        String userHome = "/home/hhernandez";

        DataBaseGenerator dbGenerator = new DataBaseGenerator(ds, "com.onea.dal.th");
        dbGenerator.printInformationSchema("th\\.TH_application");
        dbGenerator.setSourceDir(Paths.get(userHome+"/workspace/onea-ws/src/main/java"));

        List<String> tables = Arrays.asList(
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
        Collections.sort(tables);
        for(String tname:tables) {
            // System.out.println("            \""+tname+"\",");
        }
        dbGenerator.processTableList("th\\.TH_application", tables);
    }

}