package com.github.springRecords.generator;

import org.apache.commons.lang.StringUtils;

import com.github.springRecords.generator.TableTool.Column;

public class BaseTool {

    protected String basePackageName;

    public BaseTool() {
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

    public String javaTypeName(Column col) {
        return converJavaTypeName(col.columnTypeName, col.isNullable);
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

}