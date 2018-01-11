package io.github.huherto.springyRecords.generator.tools;

import java.util.List;

public interface TableTool {

    List<ColumnTool> getColumns();

    boolean ignoreColumn(ColumnTool col);

    String tableName();
    
    String fullTableName();
    
    Clazz baseRecord();

    Clazz concreteRecord();

    Clazz baseTable();

    Clazz concreteTable();

    Iterable<String> concreteRecordImports();
    
    Iterable<String> baseRecordImports();

    Iterable<String> baseTableImports();
    
    Iterable<String> concreteTableImports();

    String tableInstanceName();

    boolean hasPrimaryKey();

    String pkSqlCondition();

    String pkMethodParameterList();

    String pkArgumentList();

    List<FinderMethod> finderMethods();

}