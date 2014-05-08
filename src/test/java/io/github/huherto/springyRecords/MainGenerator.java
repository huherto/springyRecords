package io.github.huherto.springyRecords;

import static java.util.Arrays.asList;
import io.github.huherto.springyRecords.generator.DataBaseGenerator;

import java.nio.file.Paths;

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
    public void slotGenearate() {

        DataSource ds = createMssqlDS("slot");

        String userHome = "/home/hhernandez";

        DataBaseGenerator dbGenerator = new DataBaseGenerator(ds, "com.onea.dal.slot");
        dbGenerator.printInformationSchema("slot\\.dbo");
        dbGenerator.setSourceDir(Paths.get(userHome+"/workspace/onea-ws/src/main/java"));
        dbGenerator.processTableList("slot\\.dbo", asList("replenish_ticket", "trailer", "transfer_que"));
    }

}
