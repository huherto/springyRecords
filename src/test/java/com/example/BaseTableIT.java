package com.example;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class BaseTableIT {

    private BaseDatabase database = null;

    public BaseDatabase database() {
        if (database == null) {
            database = new BaseDatabase(createDs());
        }
        return database;
    }

    public DataSource createDs() {
        return new EmbeddedDatabaseBuilder()
                .setName("test")
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:schema.sql")
                .build();
    }


}
