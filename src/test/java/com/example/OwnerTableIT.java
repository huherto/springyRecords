package com.example;

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

        OwnerTable table = new OwnerTable(ds);
        OwnerRecord r = new OwnerRecord();
        r.setName("Humberto");

        table.insert(r);

        List<OwnerRecord> owners = table.queryAll();
        assertTrue(owners.size() >= 1);

    }


}
