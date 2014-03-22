package com.github.springyRecords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.github.springyRecords.test.OwnerRecord;
import com.github.springyRecords.test.PetStoreDatabase;
import com.mysql.jdbc.Driver;

public class SimpleQueries {

    public DataSource createDs() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(Driver.class);
        ds.setUrl("jdbc:mysql://localhost:3306/test");
        ds.setUsername("root");
        ds.setPassword("mysql");
        return ds;
    }

    @Test
    public void queryAll() {

    	PetStoreDatabase petStoreDb = new PetStoreDatabase(createDs());

    	List<OwnerRecord> all = petStoreDb.getOwnerTable().queryAll();

    	assertTrue(all.size() >= 0);

    }

    @Test
    public void queryOne() {

    	PetStoreDatabase petStoreDb = new PetStoreDatabase(createDs());

    	OwnerRecord ownerRecord = petStoreDb.getOwnerTable().findOwnerById(10);

    	assertEquals("Humberto", ownerRecord.name);

    }


    @Test
    public void querySome() {
    	PetStoreDatabase petStoreDb = new PetStoreDatabase(createDs());

    	List<OwnerRecord> result = petStoreDb.getOwnerTable().findByName("Humberto");

    	assertTrue(result.size() >= 0);

    }

}
