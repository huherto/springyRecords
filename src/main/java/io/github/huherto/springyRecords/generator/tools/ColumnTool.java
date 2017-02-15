package io.github.huherto.springyRecords.generator.tools;

public interface ColumnTool {

    String javaTypeName();

    String javaFieldName();

    String resultSetGetter();

    String columnName();

    boolean isAutoincrement();

}