package com.example;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

/**
 * OwnerTable –
 *
 */
public class OwnerTable extends BaseOwnerTable {

    public OwnerTable(DataSource dataSource) {
        super(dataSource);
    }

    public Optional<OwnerRecord> findOwnerById(int id) {
		return optionalSingle("select * from owner where owner_id = ?", id);
    }

	public List<OwnerRecord> findByName(String name) {

		return query("select * from owner where name = ?", name);

	}

}
