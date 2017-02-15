package io.github.huherto.springyRecords.generator.tools;

import java.util.Arrays;
import java.util.List;

import schemacrawler.schema.Column;

public class ColumnTool extends BaseTool {

    private final Column col;
    private final boolean isAutoincrement;

    public ColumnTool(schemacrawler.schema.Column col) {
        this.col = col;
        isAutoincrement = "YES".equalsIgnoreCase((String)col.getAttribute("IS_AUTOINCREMENT"));
    }

    public String javaTypeName() {
        return convertJavaTypeName(
                this.col.getColumnDataType().getName(),
                this.col.isNullable());
    }

    public String javaFieldName() {
        String columnName = this.columnName();
        String javaFieldName1 =  convertToCamelCase(columnName, false);
        List<String> reservedWords = Arrays.asList("protected");
        if (reservedWords.contains(javaFieldName1)) {
            return "_" + javaFieldName1;
        }
        return javaFieldName1;
    }

    public String sqlTypeConstant() {
        return findSqlTypeConstant(col.getColumnDataType().getJavaSqlType().getJavaSqlType());
    }

    public String resultSetGetter() {
        String tname = javaTypeName();
        switch(tname) {
        case "String":
        case "boolean":
        case "byte":
        case "int":
        case "short":
        case "long":
        case "float":
        case "double":
        case "Date":
        case "Timestamp":
        case "BigDecimal":
            return String.format("get%s(\"%s\")", upperCaseFirst(tname), columnName() );
        case "byte[]":
            return String.format("get%s(\"%s\")", "Bytes", columnName() );
        }
        return String.format("getObject(\"%s\", %s.class)",  columnName() );
    }

    public String columnName() {
        String columnName = col.getName();
        if (columnName.startsWith("\"") && columnName.endsWith("\""))
            columnName = columnName.replaceAll("\"", "");
        if (columnName.startsWith("'") && columnName.endsWith("'"))
            columnName = columnName.replaceAll("'", "");
        if (columnName.startsWith("`") && columnName.endsWith("`"))
            columnName = columnName.replaceAll("`", "");
        return columnName;
    }

    public boolean isAutoincrement() {
        return isAutoincrement;
    }
}