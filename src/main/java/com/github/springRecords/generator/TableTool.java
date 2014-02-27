package com.github.springRecords.generator;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.JdbcUtils;

public class TableTool extends BaseTool {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(TableTool.class);

    public class Column {
        public String columnName;
        public String columnTypeName;
        public boolean isNullable;
        public boolean isAutoincrement;
        public int columnType;

        public Column(ResultSetMetaData rsmd, int index) throws SQLException {
            columnName = JdbcUtils.lookupColumnName(rsmd, index);
            isNullable = rsmd.isNullable(index) == ResultSetMetaData.columnNullable;
            isAutoincrement = rsmd.isAutoIncrement(index);
            columnType = rsmd.getColumnType(index);
            columnTypeName = rsmd.getColumnTypeName(index);
            //columnTypeName = rs.getString("TYPE_NAME");
        }

        public String javaTypeName() {
            return TableTool.this.javaTypeName(this);
        }

        public String javaFieldName() {
            return TableTool.this.javaFieldName(this);
        }

        public String sqlTypeConstant() {
            return findSqlTypeConstant(columnType);
        }
    }

    private String tableName;
    private String mydomain = "com.github.springRecords";
    private List<TableTool.Column> columns = new ArrayList<TableTool.Column>();

    public void initialize(DatabaseMetaData dbmd, ResultSetMetaData rsmd, String tableName, String basePackage) throws SQLException {
        this.tableName = tableName;
        this.basePackageName = basePackage;
        try {
            //ResultSet rs = dbmd.getColumns(null, null, tableName, null);
            for (int index = 1; index <= rsmd.getColumnCount(); index++) {
                columns.add(new Column(rsmd, index));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Column> getColumns() {
        List<Column> cols = new ArrayList<>();
        for(Column col : columns) {
            if (!ignoreColumn(col))
                cols.add(col);
        }
        return cols;
    } 

    public boolean ignoreColumn(Column col) {
        return false;
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
        for(Column column : getColumns() ) {
            if (column.javaTypeName().contains("BigDecimal"))
                importSet.add("import java.math.BigDecimal;");
            if (column.javaTypeName().contains("Date"))
                importSet.add("import java.util.Date;");
            if (column.javaTypeName().contains("Timestamp"))
                importSet.add("import java.sql.Timestamp;");
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
        return convertToCamelCase(tableName, true) + "Table";
    }

    public String tableInstanceName() {
        return convertToCamelCase(tableName, false) + "Table";
    }

    public String javaTypeName(Column col) {
        return converJavaTypeName(col.columnTypeName, col.isNullable);
    }

    public String javaFieldName(Column col) {
        return convertToCamelCase(col.columnName , false);
    }

    public static String converJavaTypeName(String typeName, boolean nullable) {

        typeName = typeName.toLowerCase();
        if (typeName.contains("char"))
            return "String";
        if (typeName.equals("text"))
            return "String";
        if (typeName.contains("date"))
            return "Date";

        if (typeName.contains("unsigned")) {
            typeName = typeName.replaceAll("\\s*unsigned\\s*", "");
        }

        if (typeName.equals("timestamp"))
            return "Timestamp";

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