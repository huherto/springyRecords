package com.github.springRecords.generator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

public class TableTool {

	public static class Column {
		String columnName;
		String columnTypeName;
		boolean isNullable;
		boolean isAutoincrement;
		int columnType;

		public Column(ResultSetMetaData rsmd, int index) throws SQLException {
			columnName = JdbcUtils.lookupColumnName(rsmd, index);
			isNullable = rsmd.isNullable(index) == ResultSetMetaData.columnNullable;
			isAutoincrement = rsmd.isAutoIncrement(index);
			columnType = rsmd.getColumnType(index);
			columnTypeName = rsmd.getColumnTypeName(index);
		}

		public String javaTypeName() {
			return converJavaTypeName(columnTypeName, isNullable);
		}

		public String javaFieldName() {
			return convertToCamelCase(columnName , false);
		}

		public String sqlTypeConstant() {
			return findSqlTypeConstant(columnType);
		}
	}

	private final ResultSetMetaData rsmd;
	private final String basePackageName;
	private final String tableName;
	private final String mydomain = "com.github.springRecords";

	List<TableTool.Column> columns = new ArrayList<TableTool.Column>();

	public TableTool(ResultSetMetaData rsmd, String basePackage) throws SQLException {

		this.rsmd = rsmd;
		this.tableName = rsmd.getTableName(1);
		this.basePackageName = basePackage;
		for (int index = 1; index <= rsmd.getColumnCount(); index++) {
			columns.add(createColumn(rsmd, index));
		}
	}

	public Column createColumn(ResultSetMetaData rsmd, int index) throws SQLException {
		return new Column(rsmd, index);
	}

	public static TableTool createTableTool(DataSource ds, String schema, String tableName, String basePackage) {
		try {
			Connection con = DataSourceUtils.getConnection(ds);
			Statement stmt = con.createStatement();

			String completeTableName = tableName;
			if (schema != null && schema.length() > 0) {
				completeTableName = schema + "." + tableName;
			}

			ResultSet rs = stmt.executeQuery("select * from "+completeTableName+"");
			TableTool tableTool = new TableTool(rs.getMetaData(), basePackage);
			DataSourceUtils.releaseConnection(con, ds);
			return tableTool;
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public String mydomain() {
		return mydomain;
	}

	public String tableName(){
		return tableName;
	}

	public String baseRecordPackageName() {
		return basePackageName;
	}

	public String baseRecordClassName() {
		return "Base" + concreteRecordClassName();
	}

	public String concreteRecordPackageName() {
		return basePackageName;
	}

	public String concreteRecordClassName() {
		return convertToCamelCase(tableName ,true) + "Record";
	}

	public List<String> baseRecordImports() {
		Set<String> importSet = new HashSet<String>();
		for(Column column : columns ) {
			if (column.javaTypeName().contains("BigDecimal"))
				importSet.add("import java.math.BigDecimal;");
			if (column.javaTypeName().contains("Date"))
				importSet.add("import java.util.Date;");
			if (column.isAutoincrement)
				importSet.add("import "+mydomain+".Autoincrement;");
		}

		importSet.add("import "+mydomain+".Column;");
		importSet.add("import "+mydomain+".BaseRecord;");

		List<String> imports = new ArrayList<String>(importSet);
		Collections.sort(imports);
		return imports;
	}

	public String tablePackageName() {
		return basePackageName;
	}

	public String tableClassName() {
		return convertToCamelCase(tableName ,true) + "Table";
	}

	public static String convertToCamelCase(String columnName, boolean upperCaseFirst) {

		String words[] = columnName.toLowerCase().split("_");
		String camelCase = "";
		for(int i = 0; i < words.length; i++) {
			if (i > 0 || upperCaseFirst) {
				camelCase += StringUtils.capitalize(words[i]);
			}
			else {
				camelCase += words[i];
			}
		}
		return camelCase;
	}

	public static String converJavaTypeName(String typeName, boolean nullable) {

		typeName = typeName.toLowerCase();
		if (typeName.equals("char"))
			return "String";
		if (typeName.equals("text"))
			return "String";
		if (typeName.equals("varchar"))
			return "String";
		if (typeName.contains("date"))
			return "Date";

		if (typeName.contains("unsigned")) {
			typeName = typeName.replaceAll("\\s*unsigned\\s*", "");
		}

		if (typeName.equals("numeric"))
			return "BigDecimal";
		if (typeName.equals("decimal"))
			return "BigDecimal";

		if (typeName.equals("int") || typeName.equals("smallint") || typeName.equals("tinyint"))
			return nullable?"Integer":"int";
		if (typeName.equals("int identity")|| typeName.equals("bigint"))
			return nullable?"Long":"long";
		if (typeName.equals("bit")) {
			return nullable?"Boolean":"boolean";
		}
		return typeName;
	}

	private static String findSqlTypeConstant(int sqlType) {
		for(Field field : java.sql.Types.class.getFields()) {

			int mod = field.getModifiers();
			if (field.getType() == int.class && Modifier.isPublic(mod) && Modifier.isStatic(mod)) {
				try {
					if (field.getInt(null) == sqlType) {
						return "java.sql.Types."+field.getName();
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return "" + sqlType;
	}

}