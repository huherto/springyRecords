package io.github.huherto.springyRecords.generator.tools;

import java.util.Iterator;

public interface ClassTool {
    
    String getClassName();
    
    String getPackageName();
    
    Iterator<String> getImports();

}
