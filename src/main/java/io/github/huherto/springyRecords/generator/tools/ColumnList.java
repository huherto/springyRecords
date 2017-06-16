package io.github.huherto.springyRecords.generator.tools;

import java.util.ArrayList;
import java.util.Collection;

public class ColumnList extends ArrayList<ColumnTool> {

    public ColumnList() {
    }

    public ColumnList(Collection<? extends ColumnTool> c) {
        super(c);
    }

    public ColumnList(int initialCapacity) {
        super(initialCapacity);
    }

    private static final long serialVersionUID = 1L;

    public String sqlCondition() {
        StringBuilder sb = new StringBuilder();

        for(ColumnTool col : this) {

            if (sb.length() > 0)
                sb.append(" and ");

            sb.append(col.columnName());
            String dataTypeName = col.dataTypeName().toLowerCase().trim();
            if (dataTypeName.equalsIgnoreCase("char")) {
                sb.append(" = cast(? as char("+col.dataTypeSize()+"))");                
            }
            else {
                sb.append("  = ?");
            }
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
