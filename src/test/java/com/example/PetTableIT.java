package com.example;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PetTableIT extends BaseTableIT {

    public PetTable table() {
        return database().petTable();
    }

    @Test
    public void testNotNull() {
        assertNotNull(table());
    }

}
