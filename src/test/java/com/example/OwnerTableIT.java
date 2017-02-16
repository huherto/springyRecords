package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;

public class OwnerTableIT extends BaseTableIT {

    public OwnerTable table() {
        return database().ownerTable();
    }

    @Test
    public void testNotNull() {
        assertNotNull(table());
        
    }

    @Test
    public void insertOwner() {
        DataSource ds = createDs();

        OwnerRecord r = new OwnerRecord();
        r.setName("Humberto");

        table().insert(r);
        table().findByPK(1);

        List<OwnerRecord> owners = table().queryAll();
        assertTrue(owners.size() >= 1);

    }

    @Test
    public void queryAll() {

        List<OwnerRecord> all = table().queryAll();

        assertTrue(all.size() >= 0);

    }

    @Test
    public void queryOne() {

        OwnerRecord ownerRecord = table().findOwnerById(10).get();

        assertEquals("Humberto", ownerRecord.getName());

    }

    @Test
    public void querySome() {
        List<OwnerRecord> result = table().findByName("Humberto");

        assertTrue(result.size() >= 0);

    }

}
