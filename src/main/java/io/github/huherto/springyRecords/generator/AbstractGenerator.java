package io.github.huherto.springyRecords.generator;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.github.huherto.springyRecords.generator.classWriters.ClassWriter;
import io.github.huherto.springyRecords.generator.tools.DatabaseTool;
import io.github.huherto.springyRecords.generator.tools.TableTool;
import io.github.huherto.springyRecords.generator.tools.TableToolImpl;

public class AbstractGenerator {

    private String packageName;
    private String databaseClassName;
    private Path baseDir = null;
    private List< ClassWriter<TableTool> > classWritersForTables = new ArrayList<>();
    private List< ClassWriter<DatabaseTool> > classWritersForAll = new ArrayList<>();

    public AbstractGenerator(String packageName, String databaseClassName) {
        this.packageName = packageName;
        this.databaseClassName = databaseClassName;
        this.baseDir = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
    }

    public void addClassWriterForTable(ClassWriter<TableTool> cw) {
        classWritersForTables.add(cw);
    }

    public void addClassWriter(ClassWriter<DatabaseTool> cw) {
        classWritersForAll.add(cw);
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

    public TableTool createTableTool() {
        return new TableToolImpl();
    }

    public void writeAllClasses(DatabaseTool dbTool) {

        for(ClassWriter<DatabaseTool> cw : classWritersForAll) {
            cw.makeClass(dbTool);
        }

        for(TableTool tableTool : dbTool.getTables() ) {
            for(ClassWriter<TableTool> cw : classWritersForTables) {
                cw.makeClass(tableTool);
            }
        }
    }

}