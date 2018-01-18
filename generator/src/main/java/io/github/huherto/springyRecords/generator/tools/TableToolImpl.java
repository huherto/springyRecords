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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import schemacrawler.schema.Table;

public class TableToolImpl extends BaseTool implements TableTool {

    protected String schemaName;
    protected String physicalName;
    protected String logicalName;
    protected final ColumnList columns = new ColumnList();
    protected final ColumnList primaryKey = new ColumnList();
    protected final List<ColumnList> indexes = new ArrayList<>();
    
    // spring-jdbc
    private Clazz baseRecord;
    private Clazz concreteRecord;
    private Clazz baseTable;
    private Clazz concreteTable;
    
    // spring-jpa
    private Clazz concreteEntity;

    public TableToolImpl(String packageName) {
        super(packageName);
    }

    public void initialize(Table table) throws SQLException {

        this.physicalName = table.getName();
        this.logicalName = convertToCamelCase(physicalName, true);
        this.schemaName = table.getSchema().getName();

        for(schemacrawler.schema.Column column :table.getColumns() ) {
            this.columns.add(new ColumnToolImpl(column));
        }

        if (table.getPrimaryKey() == null) {
            throw new RuntimeException(String.format("Table [%s] doesn't have primary key", table.getFullName()));
        }

        for(schemacrawler.schema.Column column :table.getPrimaryKey().getColumns() ) {
            this.primaryKey.add(new ColumnToolImpl(column));
        }

        for(schemacrawler.schema.Index index :table.getIndices() ) {
            ColumnList cols = new ColumnList();
            for(schemacrawler.schema.Column column : index.getColumns() ) {
                cols.add(new ColumnToolImpl(column));
            }
            this.indexes.add(cols);
        }
        
        this.baseRecord     = new Clazz(getPackageNameForBaseTypes(), "Base" + logicalName + "Record");
        this.concreteRecord = new Clazz(getPackageName(), logicalName  + "Record");
        this.baseTable      = new Clazz(getPackageNameForBaseTypes(), "Base" + logicalName + "Table");
        this.concreteTable  = new Clazz(getPackageName(), logicalName + "Table");         
        this.concreteEntity = new Clazz(getPackageName(), logicalName + "Entity" );
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
    public Clazz baseRecord() {        
        return baseRecord; 
    }

    @Override
    public Iterable<String> baseRecordImports() {
                
        ImportSet importSet = new ImportSet(baseRecord());
        
        importSet.addImports(importsForColumns(getColumns()));

        importSet.addImport("java.util","HashMap");
        importSet.addImport("java.util", "Map");
        importSet.addImport("java.sql","SQLException");
        importSet.addImport("java.sql","ResultSet");

        return importSet;
    }

    @Override
    public Clazz concreteRecord() {
        return concreteRecord;
    }

    @Override
    public Iterable<String> concreteRecordImports() {
        
        ImportSet importSet = new ImportSet(concreteRecord);
        importSet.addImport(baseRecord());
        importSet.addImport("java.sql", "SQLException");
        importSet.addImport("java.sql", "ResultSet");

        return importSet;
    }

    @Override
    public Clazz baseTable() {
        return baseTable;
    }

    @Override
    public Iterable<String> baseTableImports() {
        ImportSet importSet = new ImportSet(baseTable);
        if (hasPrimaryKey()) {
            importSet.addImports(importsForColumns(primaryKey));
            importSet.addImport("java.util","Optional");
        }
        importSet.addImport(concreteRecord);
        if (finderMethods().size() > 0) {
            importSet.addImport("java.util","List");
        }
        importSet.addImport("java.sql","SQLException");
        importSet.addImport("java.sql","ResultSet");        
        return importSet;
    }
    
    @Override
    public Clazz concreteTable() {
        return concreteTable;
    }

    @Override
    public Iterable<String> concreteTableImports() {
        
        ImportSet importSet = new ImportSet(concreteTable());
        importSet.addImport(baseTable);
        importSet.addImport("javax.sql","DataSource");

        return importSet;
    }

    @Override
    public String tableInstanceName() {
        return lowerCaseFirst(concreteTable.getClassName());
    }

    public static List<Clazz> importsForColumns(List<ColumnTool> cols) {
        List<Clazz> importSet = new ArrayList<Clazz>();
        for(ColumnTool column :  cols ) {
            String javaTypeName = column.javaTypeName();
            if (javaTypeName.contains("BigDecimal"))
                importSet.add(new Clazz("java.math","BigDecimal"));
            if (javaTypeName.contains("Date"))
                importSet.add(new Clazz("java.util","Date"));
            if (javaTypeName.contains("Timestamp"))
                importSet.add(new Clazz("java.sql","Timestamp"));
            if (javaTypeName.contains("Blob"))
                importSet.add(new Clazz("java.sql","Blob"));
            if (javaTypeName.contains("Clob"))
                importSet.add(new Clazz("java.sql","Clob"));
        }
        return importSet;
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

    @Override
    public List<FinderMethod> finderMethods() {

        Set<String> methodNames = new HashSet<>();
        List<FinderMethod> result = new ArrayList<>();
        for (ColumnList index : indexes) {
            for(int i = 0; i < index.size(); i++) {
                ColumnList list = new ColumnList(i + 1);
                for(int j = 0; j <= i; j++) {
                    list.add(index.get(j));
                }

                FinderMethod method = new FinderMethod(list);
                if (!methodNames.contains(method.methodName())) {
                    result.add(new FinderMethod(list));
                    methodNames.add(method.methodName());
                }

            }

        }

        return result;
    }

    @Override
    public String fullTableName() {
        if (schemaName != null && !schemaName.isEmpty()) {
            return schemaName + "." + tableName();
        }
        return tableName();
    }

    @Override
    public Clazz concreteEntity() {
        return concreteEntity;
    }

}