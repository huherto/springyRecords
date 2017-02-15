package io.github.huherto.springyRecords.generator.tools;

import java.util.Arrays;
import java.util.List;

public class ColumnToolImpl extends BaseTool implements ColumnTool {

    private final boolean isAutoincrement;
    private String physicalName;
    private String logicalName;
    private String dataTypeName;
    private boolean isNullable;

    public ColumnToolImpl(schemacrawler.schema.Column col) {
        this.physicalName = removeQuotes(col.getName());
        this.logicalName  = physicalName;
        this.dataTypeName = col.getColumnDataType().getName();
        isAutoincrement = "YES".equalsIgnoreCase((String)col.getAttribute("IS_AUTOINCREMENT"));
    }

    @Override
    public String javaTypeName() {
        return convertJavaTypeName(
                this.dataTypeName,
                this.isNullable);
    }

    @Override
    public String javaFieldName() {
        String javaFieldName =  convertToCamelCase(logicalName, false);
        List<String> reservedWords = Arrays.asList("protected");
        if (reservedWords.contains(javaFieldName)) {
            return "_" + javaFieldName;
        }
        return javaFieldName;
    }

    @Override
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
            return String.format("get%s(\"%s\")", upperCaseFirst(tname), physicalName );
        case "byte[]":
            return String.format("get%s(\"%s\")", "Bytes", physicalName );
        }
        return String.format("getObject(\"%s\", %s.class)",  physicalName, tname );
    }

    @Override
    public String columnName() {
        String columnName = physicalName;
        return columnName;
    }

    @Override
    public boolean isAutoincrement() {
        return isAutoincrement;
    }
}