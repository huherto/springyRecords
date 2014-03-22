package com.github.springRecords;
/*
The MIT License (MIT)

Copyright (c) 2014 <copyright holders>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.github.springRecords.generator.DataBaseGenerator;
import com.github.springRecords.generator.TableTool;
import com.github.springRecords.test.OwnerRecord;
import com.github.springRecords.test.OwnerTable;
import com.github.springRecords.test.PetRecord;
import com.github.springRecords.test.PetTable;
import com.mysql.jdbc.Driver;

public class TestDatabaseGenerator {

    public DataSource createDs() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(Driver.class);
        ds.setUrl("jdbc:mysql://localhost:3306/test");
        ds.setUsername("root");
        ds.setPassword("mysql");
        return ds;
    }

    @Test
    public void generateExtendTableTool() {
        DataSource ds = createDs();

        DataBaseGenerator dbGenerator = new DataBaseGenerator(ds, "com.github.springRecords.test") {

            @Override
            public TableTool createTableTool() {
                return new ExtendedTableTool();
            }

        };

        dbGenerator.printInformationSchema();
        dbGenerator.processTableList("def", "test", Arrays.asList("owner", "pet"));
    }

    @Test
    public void insertPet() {
        DataSource ds = createDs();

        JdbcTemplate jt = new JdbcTemplate(ds);
        jt.execute("delete from pet");

        PetTable table = new PetTable(ds);
        PetRecord r = new PetRecord();
        r.birth = new Date();
        r.name = "Manchas";
        r.owner = "Humberto";
        r.sex = "M";
        r.species = "Dog";
        table.insert(r);

        List<PetRecord> pets = table.queryAll();
        assertTrue(pets.size() >= 1);

    }

    @Test
    public void insertOwner() {
        DataSource ds = createDs();

        OwnerTable table = new OwnerTable(ds);
        OwnerRecord r = new OwnerRecord();
        r.name = "Humberto";

        table.insert(r);

        List<OwnerRecord> owners = table.queryAll();
        assertTrue(owners.size() >= 1);

    }
}
