package io.github.huherto.springyRecords.test;

import java.util.List;

import javax.sql.DataSource;

/**
 * OwnerTable –
 *
 */
public class OwnerTable extends BaseOwnerTable {

    public OwnerTable(DataSource dataSource) {
        super(dataSource);
    }

    public OwnerRecord findOwnerById(int id) {
		return queryForRequiredObject("select * from owner where owner_id = ?", id);
    }

	public List<OwnerRecord> findByName(String name) {

		return query("select * from owner where name = ?", name);

	}

}
