package io.github.huherto.springyRecords.generator.tools;
/*
The MIT License (MIT)

Copyright (c) 2014 <copyright holders>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import schemacrawler.schema.Table;

public class TableToolImpl extends BaseTool implements TableTool {

    protected String physicalName;
    protected String logicalName;
    protected final ColumnList columns = new ColumnList();
    protected final ColumnList primaryKey = new ColumnList();
    protected final List<ColumnList> indexes = new ArrayList<>();

    public TableToolImpl() {
    }

    @Override
    public void initialize(Table table, String basePackage) throws SQLException {
        this.basePackageName = basePackage;

        physicalName = table.getName();
        logicalName = convertToCamelCase(physicalName, true);

        for(schemacrawler.schema.Column column :table.getColumns() ) {
            columns.add(new ColumnToolImpl(column));
        }

        for(schemacrawler.schema.Column column :table.getPrimaryKey().getColumns() ) {
            primaryKey.add(new ColumnToolImpl(column));
        }

        for(schemacrawler.schema.Index index :table.getIndices() ) {
            ColumnList cols = new ColumnList();
            for(schemacrawler.schema.Column column : index.getColumns() ) {
                cols.add(new ColumnToolImpl(column));
            }
            indexes.add(cols);
        }
    }

    @Override
    public List<ColumnTool> getColumns() {
        return columns
                .stream()
                .filter( x -> !ignoreColumn(x))
                .collect(Collectors.toList());
    }

    public List<ColumnList> getIndexes() {
        return indexes;
    }

    @Override
    public boolean ignoreColumn(ColumnTool col) {
        return false;
    }

    @Override
    public String tableName(){
        return physicalName;
    }

    @Override
    public String baseRecordPackageName() {
        return basePackageName;
    }

    @Override
    public String baseRecordClassName() {
        return "Base" + concreteRecordClassName();
    }

    @Override
    public String concreteRecordPackageName() {
        return basePackageName;
    }

    @Override
    public String concreteRecordClassName() {
        return logicalName  + "Record";
    }

    @Override
    public List<String> baseRecordImports() {
        Set<String> importSet = importsForColumns(getColumns());

        importSet.add("import java.util.HashMap;");
        importSet.add("import java.util.Map;");
        importSet.add("import java.sql.SQLException;");
        importSet.add("import java.sql.ResultSet;");

        List<String> imports = new ArrayList<String>(importSet);
        Collections.sort(imports);
        return imports;
    }

    private static Set<String> importsForColumns(List<ColumnTool> cols) {
        Set<String> importSet = new HashSet<String>();
        for(ColumnTool column :  cols ) {
            String javaTypeName = column.javaTypeName();
            if (javaTypeName.contains("BigDecimal"))
                importSet.add("import java.math.BigDecimal;");
            if (javaTypeName.contains("Date"))
                importSet.add("import java.util.Date;");
            if (javaTypeName.contains("Timestamp"))
                importSet.add("import java.sql.Timestamp;");
            if (javaTypeName.contains("Blob"))
                importSet.add("import java.sql.Blob;");
            if (javaTypeName.contains("Clob"))
                importSet.add("import java.sql.Clob;");
        }
        return importSet;
    }

    @Override
    public List<String> baseTableImports() {
        Set<String> importSet = new HashSet<String>();
        if (hasPrimaryKey()) {
            importSet = importsForColumns(primaryKey);
            importSet.add("import java.util.Optional;");
        }
        importSet.add("import java.sql.SQLException;");
        importSet.add("import java.sql.ResultSet;");
        List<String> imports = new ArrayList<String>(importSet);
        Collections.sort(imports);
        return imports;
    }

    @Override
    public String concreteTablePackageName() {
        return basePackageName;
    }

    @Override
    public String concreteTableClassName() {
        return logicalName + "Table";
    }

    @Override
    public String baseTablePackageName() {
        return basePackageName;
    }

    @Override
    public String baseTableClassName() {
        return "Base" + logicalName + "Table";
    }

    @Override
    public String tableInstanceName() {
        return lowerCaseFirst(concreteTableClassName());
    }

    @Override
    public boolean hasPrimaryKey() {
        return !primaryKey.isEmpty();
    }

    @Override
    public String pkSqlCondition() {
        return primaryKey.sqlCondition();
    }

    @Override
    public String pkMethodParameterList() {
        return primaryKey.methodParameterList();
    }

    @Override
    public String pkArgumentList() {
        return primaryKey.argumentList();
    }

}