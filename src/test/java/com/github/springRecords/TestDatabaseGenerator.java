package com.github.springRecords;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.github.springRecords.generator.DataBaseGenerator;
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
	public void generateAll() {
		DataSource ds = createDs();

		DataBaseGenerator dbGenerator = new DataBaseGenerator(ds, "def", "test","com.github.springRecords.test");
		dbGenerator.processTableList(Arrays.asList("owner", "pet"));
	}

	@Test
	public void insertPet() {
		DataSource ds = createDs();
		
		JdbcTemplate jt = new JdbcTemplate(ds);
		jt.execute("delete from pet");

		PetTable table = new PetTable(ds);
		PetRecord r = new PetRecord();
		r.birthDate = new Date();
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
