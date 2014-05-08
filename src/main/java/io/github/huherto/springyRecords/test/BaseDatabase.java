package io.github.huherto.springyRecords.test;

import javax.sql.DataSource;

/**
 * BaseDatabase –
 * Automatically generated. Do not modify or your changes might be lost.
 */
public class BaseDatabase {

    private DataSource dataSource;

    private OwnerTable ownerTable = null;
    private PetTable petTable = null;

    protected BaseDatabase(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public OwnerTable ownerTable() {
        if (ownerTable == null) {
            ownerTable = new OwnerTable(dataSource);
        }
        return ownerTable;
    }

    public PetTable petTable() {
        if (petTable == null) {
            petTable = new PetTable(dataSource);
        }
        return petTable;
    }

}
