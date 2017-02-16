package io.github.huherto.springyRecords.generator.tools;

import java.util.ArrayList;

public class ColumnList extends ArrayList<ColumnTool> {

    private static final long serialVersionUID = 1L;

    public String sqlCondition() {
        StringBuilder sb = new StringBuilder();

        for(ColumnTool col : this) {

            if (sb.length() > 0)
                sb.append(" and ");

            sb.append(col.columnName());
            sb.append("  = ?");
        }
        return sb.toString();
    }

    public String methodParameterList() {
        StringBuilder sb = new StringBuilder();

        for(ColumnTool col : this) {

            if (sb.length() > 0)
                sb.append(", ");

            sb.append(col.javaTypeName());
            sb.append(" ");
            sb.append(col.javaFieldName());
        }
        return sb.toString();
    }

    public String argumentList() {
        StringBuilder sb = new StringBuilder();

        for(ColumnTool col : this) {

            if (sb.length() > 0)
                sb.append(", ");

            sb.append(col.javaFieldName());
        }
        return sb.toString();
    }
}
