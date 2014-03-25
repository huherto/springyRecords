package com.github.springyRecords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.github.springyRecords.test.OwnerRecord;
import com.github.springyRecords.test.PetStoreDatabase;

public class SimpleQueries extends BaseTest {

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
