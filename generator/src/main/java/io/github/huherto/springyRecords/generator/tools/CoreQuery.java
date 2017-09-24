package io.github.huherto.springyRecords.generator.tools;

import java.util.Collection;
import java.util.stream.Collectors;

public class CoreQuery {
    
    private ColumnList columns;
    
    public CoreQuery(Collection< ? extends ColumnTool> cols) {
        this.columns = new ColumnList(cols);
    }
    
    public CoreQuery(ColumnTool col) {
        this.columns = new ColumnList();
        this.columns.add(col);
    }
    
    public String queryName() {

        return "queryBy" + 
                columns
                    .stream()
                    .map( x-> BaseTool.upperCaseFirst(x.javaFieldName()) )
                    .collect(Collectors.joining());        
    }
    
    public String sqlCondition() {
        return columns.sqlCondition();
    }

    public String methodParameterList() {
        return columns.methodParameterList();
    }

    public String argumentList() {
        return columns.argumentList();
    }
    
}
