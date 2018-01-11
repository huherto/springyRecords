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

    List<String> concreteRecordImports();
    
    List<String> baseRecordImports();

    List<String> baseTableImports();
    
    List<String> concreteTableImports();

    String tableInstanceName();

    boolean hasPrimaryKey();

    String pkSqlCondition();

    String pkMethodParameterList();

    String pkArgumentList();

    List<FinderMethod> finderMethods();

}