package io.github.huherto.springyRecords.generator;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import io.github.huherto.springyRecords.generator.classWriters.ClassWriterRegistry;
import io.github.huherto.springyRecords.generator.classWriters.DefaultClassWriterRegistry;
import io.github.huherto.springyRecords.generator.tools.DatabaseTool;

public class AbstractGenerator {

    private String packageName;
    private String databaseClassName;
    private Path baseDir = null;
    private ClassWriterRegistry classWriterRegistry = new DefaultClassWriterRegistry();

    public ClassWriterRegistry getClassWriterRegistry() {
        return classWriterRegistry;
    }

    public void setClassWriterRegistry(ClassWriterRegistry classWriterRegistry) {
        this.classWriterRegistry = classWriterRegistry;
    }

    public AbstractGenerator(String packageName, String databaseClassName) {
        this.packageName = packageName;
        this.databaseClassName = databaseClassName;
        this.baseDir = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
    }

    public String getPackageName() {
        return packageName;
    }

    public String getDatabaseClassName() {
        return databaseClassName;
    }

    public Path getBaseDir() {
      return baseDir;
    }

    public void setBaseDir(Path path) {
      baseDir = path;
    }

    public void writeAllClasses(DatabaseTool dbTool) {

        classWriterRegistry.writeAllClasses(dbTool, baseDir);
    }

}