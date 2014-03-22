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
