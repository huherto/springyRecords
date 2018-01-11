package io.github.huherto.springyRecords.generator.tools;

import java.util.List;

public interface TableTool {

    List<ColumnTool> getColumns();

    boolean ignoreColumn(ColumnTool col);

    String tableName();
    
    String fullTableName();
    
    Clazz baseRecord();

    String concreteRecordPackageName();

    String concreteRecordClassName();

    List<String> concreteRecordImports();
    
    List<String> baseRecordImports();

    List<String> baseTableImports();

    String concreteTablePackageName();

    String concreteTableClassName();

    String baseTablePackageName();

    String baseTableClassName();

    String tableInstanceName();

    boolean hasPrimaryKey();

    String pkSqlCondition();

    String pkMethodParameterList();

    String pkArgumentList();

    List<String> concreteTableImports();

    List<FinderMethod> finderMethods();

}