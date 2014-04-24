package io.github.huherto.springyRecords;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.mysql.jdbc.Driver;

public class BaseTest {

	public BaseTest() {
		super();
	}

	public DataSource createDs() {
	    SimpleDriverDataSource ds = new SimpleDriverDataSource();
	    ds.setDriverClass(Driver.class);
	    ds.setUrl("jdbc:mysql://localhost:3306/test");
	    ds.setUsername("root");
	    ds.setPassword("mysql");
	    return ds;
	}

}