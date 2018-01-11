package io.github.huherto.springyRecords.generator.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ImportSet implements Iterable<String> {

    private Clazz myClazz;
    
    private Set<String> imports = new HashSet<>();
    
    public ImportSet(Clazz clazz) {
        this.myClazz = clazz;        
    }

    public void addImport(Clazz clazz) {
        if (myClazz.getPackageName().isEmpty() || !myClazz.samePackage(clazz)) {
            imports.add(String.format("import %s;", clazz.getFullname()));
        }        
    }
    
    public void addImport(String packageName, String className) {
        addImport(new Clazz(packageName, className));
    }

    public void addImports(Iterable<Clazz> importLines) {
        
        for(Clazz importLine : importLines) {
            addImport(importLine);
        }
        
    }

    @Override
    public Iterator<String> iterator() {
        List<String> list = new ArrayList<>();        
        list.addAll(imports);                
        Collections.sort(list);
        return list.iterator();
    }    
   
}
