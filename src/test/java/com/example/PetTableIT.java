package com.example;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class PetTableIT extends BaseTableIT {

    public PetTable table() {
        return database().petTable();
    }

    @Test
    public void testNotNull() {
        assertNotNull(table());
    }

    @Test
    public void insertPet() {
        DataSource ds = createDs();

        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("delete from pet");


        PetRecord r = new PetRecord();
        r.setBirthDate(new Date());
        r.setName("Manchas");
        r.setOwner("Humberto");
        r.setSex("M");
        r.setSpecies("Dog");
        table().insert(r);

        List<PetRecord> pets = table().queryAll();
        assertTrue(pets.size() >= 1);

    }

}
