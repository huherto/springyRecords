package com.example;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class OwnerTableIT extends BaseTableIT {

    public OwnerTable table() {
        return database().ownerTable();
    }

    @Test
    public void testNotNull() {
        assertNotNull(table());
    }


}
