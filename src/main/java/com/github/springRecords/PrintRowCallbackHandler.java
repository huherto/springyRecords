package com.github.springRecords;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.JdbcUtils;

public class PrintRowCallbackHandler implements RowCallbackHandler {

	private PrintWriter writer;

	private ResultSetMetaData rsmd = null;

	private String format = null;

	public PrintRowCallbackHandler() {
		writer = new PrintWriter(System.out);
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public PrintRowCallbackHandler(PrintWriter writer) {
		this.writer = writer;
	}

	public String format(Object obj) {
		StringBuilder sb = new StringBuilder();
		if (obj == null) {
			sb.append("null");
		}
		else if (obj instanceof Number) {

			if (obj instanceof Integer || obj instanceof Long) {
				sb.append("%d");
			}
			else if (obj instanceof Float || obj instanceof Double) {
				sb.append("%f");
			}
			else if (obj instanceof BigDecimal) {
				sb.append("%s");
			}

		}
		else {
			sb.append("%s");
		}
		return sb.toString();
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {

		if (rsmd == null) {
			rsmd = rs.getMetaData();
		}

		if (format != null) {
			Object args[] = new Object[ rsmd.getColumnCount() ];
			for(int index = 1; index <= rsmd.getColumnCount(); index++) {
				Object obj = JdbcUtils.getResultSetValue(rs, index);
				args[index - 1] = obj;
			}
			writer.println(String.format(format, args));
		}
		else {
			StringBuilder sb = new StringBuilder();
			for(int index = 1; index <= rsmd.getColumnCount(); index++) {
				Object obj = JdbcUtils.getResultSetValue(rs, index);
				if (index > 1) {
					sb.append(", ");
				}
				sb.append(JdbcUtils.lookupColumnName(rsmd, index));
				sb.append("=");
				sb.append(obj);
			}
			writer.println(sb.toString());
		}
		writer.flush();
	}
}
