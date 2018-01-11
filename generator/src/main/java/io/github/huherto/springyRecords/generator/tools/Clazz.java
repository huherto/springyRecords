package io.github.huherto.springyRecords.generator.tools;

public class Clazz {
    
    private final String packageName;
    
    private final String className;

    public Clazz(String packageName, String className) {
        this.packageName = packageName.trim();
        this.className = className.trim();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }
    
    public String getFullname() {
        return packageName + "." + className;        
    }
    
    public boolean samePackage(Clazz clazz) {
        return packageName.equals(clazz.packageName);
    }
    
}
