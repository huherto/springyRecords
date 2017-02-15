package io.github.huherto.springyRecords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.example.OwnerRecord;
import com.example.PetStoreDatabase;

public class SimpleQueries extends BaseTest {

    @Test
    public void queryAll() {

        PetStoreDatabase petStoreDb = new PetStoreDatabase(createDs());

        List<OwnerRecord> all = petStoreDb.ownerTable().queryAll();

        assertTrue(all.size() >= 0);

    }

    @Test
    public void queryOne() {

        PetStoreDatabase petStoreDb = new PetStoreDatabase(createDs());

        OwnerRecord ownerRecord = petStoreDb.ownerTable().findOwnerById(10).get();

        assertEquals("Humberto", ownerRecord.getName());

    }

    @Test
    public void querySome() {
        PetStoreDatabase petStoreDb = new PetStoreDatabase(createDs());

        List<OwnerRecord> result = petStoreDb.ownerTable().findByName("Humberto");

        assertTrue(result.size() >= 0);

    }

}
