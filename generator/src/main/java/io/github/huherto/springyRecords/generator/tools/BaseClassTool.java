package io.github.huherto.springyRecords.generator.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseClassTool implements ClassTool {
    
    private Clazz clazz;
    protected final TableTool tableTool;

    public BaseClassTool(Clazz clazz, TableTool tool) {
        this.clazz = clazz;
        this.tableTool = tool;
    }

    @Override
    public String getClassName() {
        return clazz.getClassName();
    }

    @Override
    public String getPackageName() {
        return clazz.getPackageName();
    }

    @Override
    public Iterator<String> getImports() {
        return (new ImportSet(clazz)).iterator();
    }

    public Clazz getClazz() {
        return clazz;
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
    

}
