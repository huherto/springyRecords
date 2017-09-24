package io.github.huherto.springyRecords;

import io.github.huherto.springyRecords.PrintRowCallbackHandler;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class PrintingRows extends BaseTest {

    @Test
	public void printQuery() {

		JdbcTemplate jt = new JdbcTemplate(createDs());

		String sql = "select * from pet ";
		jt.query(sql, new PrintRowCallbackHandler());

		System.out.println();
		String format = "%-10s %-10s %-10s %-10s %-10s %-10s";
		System.out.println(String.format(format, "name", "owner" , "species", "sex", "birth" ,"death"));
		jt.query(sql, new PrintRowCallbackHandler(format));

	}

}
