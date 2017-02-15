package io.github.huherto.springyRecords.generator.tools;

import java.sql.SQLException;
import java.util.List;

import schemacrawler.schema.PrimaryKey;
import schemacrawler.schema.Table;

public interface TableTool {

    void initialize(Table table, String basePackage) throws SQLException;

    List<ColumnTool> getColumns();

    boolean ignoreColumn(ColumnTool col);

    String tableName();

    String baseRecordPackageName();

    String baseRecordClassName();

    String concreteRecordPackageName();

    String concreteRecordClassName();

    List<String> baseRecordImports();

    List<String> baseTableImports();

    String concreteTablePackageName();

    String concreteTableClassName();

    String baseTablePackageName();

    String baseTableClassName();

    String tableInstanceName();

    PrimaryKey getPrimaryKey();

    String pkSqlCondition();

    String pkMethodParameterList();

    String pkArgumentList();

}