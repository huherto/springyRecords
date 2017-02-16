package io.github.huherto.springyRecords.generator.tools;

import java.util.Arrays;
import java.util.List;

public class ColumnToolImpl extends BaseTool implements ColumnTool {

    protected String physicalName;
    protected String logicalName;
    protected String dataTypeName;
    protected boolean isNullable;
    protected String logicalType;
    protected boolean isAutoincrement;

    public ColumnToolImpl() {

    }

    public ColumnToolImpl(schemacrawler.schema.Column col) {
        this.physicalName = removeQuotes(col.getName());
        this.logicalName  = convertToCamelCase(physicalName, true);
        this.dataTypeName = col.getColumnDataType().getName();
        this.isNullable = col.isNullable();
        this.logicalType = convertJavaTypeName(dataTypeName, isNullable);
        this.isAutoincrement = "YES".equalsIgnoreCase((String)col.getAttribute("IS_AUTOINCREMENT"));
    }

    @Override
    public String javaTypeName() {
        return logicalType;
    }

    @Override
    public String javaFieldName() {
        String javaFieldName =  lowerCaseFirst(logicalName);
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

    public String getterName() {
        return "get" + upperCaseFirst(javaFieldName());
    }

    public String setterName() {
        return "set" + upperCaseFirst(javaFieldName());
    }

    @Override
    public String columnName() {
        return physicalName;
    }

    @Override
    public boolean isAutoincrement() {
        return isAutoincrement;
    }
}