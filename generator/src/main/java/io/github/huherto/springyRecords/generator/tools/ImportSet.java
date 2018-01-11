package io.github.huherto.springyRecords.generator.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ImportSet implements Iterable<String> {

    private String myPackageName;
    
    private Set<String> imports = new HashSet<>();
    
    public ImportSet(String myPackageName) {
        this.myPackageName = myPackageName;        
    }
    
    public void addImport(String importLine) {
        importLine = importLine.trim();
        if (myPackageName.isEmpty() || !importLine.endsWith(myPackageName + ";")) {
            imports.add(importLine);
        }        
    }
    
    public void addImports(Iterable<String> importLines) {
        
        for(String importLine : importLines) {
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
