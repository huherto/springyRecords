package com.example;

import javax.sql.DataSource;

public class PetTable extends BasePetTable {
    public PetTable(DataSource dataSource) {
        super(dataSource);
    }
}
