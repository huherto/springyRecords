package io.github.huherto.springyRecords.generator.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class BaseTool {

    protected String basePackageName;

    public BaseTool() {
    }

    public static String convertToCamelCase(String columnName, boolean upperCaseFirst) {

        String words[] = columnName.toLowerCase().split("_");
        String camelCase = "";
        for(int i = 0; i < words.length; i++) {
            if (i > 0 || upperCaseFirst) {
                camelCase += upperCaseFirst(words[i]);
            }
            else {
                camelCase += words[i];
            }
        }
        return camelCase;
    }

    public static String convertJavaTypeName(String typeName, boolean nullable) {

        typeName = typeName.toLowerCase();
        if (typeName.contains("char"))
            return "String";
        if (typeName.contains("text"))
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

        if (typeName.equals("int") || typeName.equals("smallint") || typeName.equals("tinyint") || typeName.equals("integer"))
            return nullable?"Integer":"int";
        if (typeName.equals("int identity"))
            return "int";

        if (typeName.equals("long identity"))
            return "long";

        if (typeName.equals("bigint"))
            return nullable?"Long":"long";

        if (typeName.equals("bigint identity"))
            return "long";

        if (typeName.equals("bit")) {
            return nullable?"Boolean":"boolean";
        }

        if (typeName.equals("binary")) {
            return "byte[]";
        }

        if (typeName.contains("blob"))
        	return "Blob";

        if (typeName.equals("clob"))
        	return "Clob";

        return typeName;
    }

    public static String upperCaseFirst(String str) {
    	 if(str.length() == 0)
    	        return str;
    	    return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

    public static String lowerCaseFirst(String str) {
    	 if(str.length() == 0)
    	        return str;
    	    return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

    public static String findSqlTypeConstant(int sqlType) {
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