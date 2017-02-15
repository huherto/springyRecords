package com.example;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class BaseDatabase extends JdbcDaoSupport {

    private OwnerTable ownerTable = null;
    private PetTable petTable = null;

    protected BaseDatabase(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public OwnerTable ownerTable() {
        if (ownerTable == null) {
            ownerTable = new OwnerTable(getDataSource());
        }
        return ownerTable;
    }

    public PetTable petTable() {
        if (petTable == null) {
            petTable = new PetTable(getDataSource());
        }
        return petTable;
    }

}
