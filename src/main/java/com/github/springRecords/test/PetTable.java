package com.github.springRecords.test;

import javax.sql.DataSource;

/**
 * PetTable –
 *
 */
public class PetTable extends BasePetTable {
    public PetTable(DataSource dataSource) {
        super(dataSource);
    }
}
