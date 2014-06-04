package io.github.huherto.springyRecords.generator;
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

import io.github.huherto.springyRecords.BaseRecord;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import schemacrawler.schema.Column;
import schemacrawler.schema.Table;

public class TableTool extends BaseTool {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(TableTool.class);

    public class ColumnTool {

    	private final Column col;
    	private final boolean isAutoincrement;

        public ColumnTool(schemacrawler.schema.Column col) {
        	this.col = col;
        	isAutoincrement = "YES".equalsIgnoreCase((String)col.getAttribute("IS_AUTOINCREMENT"));
        }

        public String javaTypeName() {
            return TableTool.this.javaTypeName(this);
        }

        public String javaFieldName() {
            return TableTool.this.javaFieldName(this);
        }

        public String sqlTypeConstant() {
            return findSqlTypeConstant(col.getColumnDataType().getJavaSqlType().getJavaSqlType());
        }

        public String columnName() {
        	return col.getName();
        }

        public boolean isAutoincrement() {
        	return isAutoincrement;
        }
    }

    private Table table;
    private final String mydomain = BaseRecord.class.getPackage().getName();
    private final List<TableTool.ColumnTool> columns = new ArrayList<TableTool.ColumnTool>();

    public void initialize(Table table, String basePackage) throws SQLException {
        this.basePackageName = basePackage;
        this.table = table;

        for(schemacrawler.schema.Column column :table.getColumns() ) {
            columns.add(new ColumnTool(column));
        }

    }

    public List<ColumnTool> getColumns() {
        List<ColumnTool> cols = new ArrayList<>();
        for(ColumnTool col : columns) {
            if (!ignoreColumn(col))
                cols.add(col);
        }
        return cols;
    }

    public boolean ignoreColumn(ColumnTool col) {
        return false;
    }

    public String mydomain() {
        return mydomain;
    }

    public String tableName(){
        return table.getName();
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
        return convertToCamelCase(tableName() ,true) + "Record";
    }

    public List<String> baseRecordImports() {
        Set<String> importSet = new HashSet<String>();
        for(ColumnTool column : getColumns() ) {
            if (column.javaTypeName().contains("BigDecimal"))
                importSet.add("import java.math.BigDecimal;");
            if (column.javaTypeName().contains("Date"))
                importSet.add("import java.util.Date;");
            if (column.javaTypeName().contains("Timestamp"))
                importSet.add("import java.sql.Timestamp;");
            if (column.javaTypeName().contains("Blob"))
                importSet.add("import java.sql.Blob;");
            if (column.javaTypeName().contains("Clob"))
                importSet.add("import java.sql.Clob;");
//            if (column.isAutoincrement)
//                importSet.add("import "+mydomain+".Autoincrement;");
        }

        // importSet.add("import "+mydomain+".Column;");
        // importSet.add("import "+mydomain+".BaseRecord;");

        importSet.add("import com.onea.dale.BaseRecord;");
        importSet.add("import java.util.HashMap;");
        importSet.add("import java.util.Map;");

        List<String> imports = new ArrayList<String>(importSet);
        Collections.sort(imports);
        return imports;
    }

	public String concreteTablePackageName() {
		return basePackageName;
	}

    public String concreteTableClassName() {
        return convertToCamelCase(tableName(), true) + "Table";
    }

    public String baseTablePackageName() {
        return basePackageName;
    }

    public String baseTableClassName() {
        return "Base" + convertToCamelCase(tableName(), true) + "Table";
    }

    public String tableInstanceName() {
        return lowerCaseFirst(concreteTableClassName());
    }

	public String javaTypeName(ColumnTool columnTool) {

        return convertJavaTypeName(
        			columnTool.col.getColumnDataType().getName(),
        			columnTool.col.isNullable());
    }

    public String javaFieldName(ColumnTool columnTool) {
    	// TODO: For some reason some fields names are quoted
    	String columnName = columnTool.col.getName();
    	if (columnName.startsWith("\"") && columnName.endsWith("\""))
    		columnName = columnName.replaceAll("\"", "");
        return convertToCamelCase(columnName, false);
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