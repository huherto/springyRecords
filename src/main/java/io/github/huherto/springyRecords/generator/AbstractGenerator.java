package io.github.huherto.springyRecords.generator;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.github.huherto.springyRecords.generator.classWriters.ABTableClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.BTableITClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.BaseDatabaseClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.BaseRecordClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.BaseTableClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.ClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.ConcreteRecordClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.ConcreteTableClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.DatabaseClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.InterfaceRecordClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.TableITClassWriter;
import io.github.huherto.springyRecords.generator.tools.DatabaseTool;
import io.github.huherto.springyRecords.generator.tools.TableTool;
import io.github.huherto.springyRecords.generator.tools.TableToolImpl;

public class AbstractGenerator {

    private String packageName;
    private String packageNameForBaseTypes;
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

    protected void registerClassWriters() {        
        addClassWriterForTable(new BaseRecordClassWriter(getBaseDir()));
        addClassWriterForTable(new ConcreteRecordClassWriter(getBaseDir()));
        addClassWriterForTable(new BaseTableClassWriter(getBaseDir()));
        addClassWriterForTable(new ConcreteTableClassWriter(getBaseDir()));
        addClassWriterForTable(new TableITClassWriter(getBaseDir()));
        addClassWriter(new DatabaseClassWriter(getBaseDir()));
        addClassWriter(new ABTableClassWriter(getBaseDir()));
        addClassWriter(new InterfaceRecordClassWriter(getBaseDir()));
        addClassWriter(new BaseDatabaseClassWriter(getBaseDir()));        
        addClassWriter(new BTableITClassWriter(getBaseDir()));        
    }
    
    public void writeAllClasses(DatabaseTool dbTool) {
        
        registerClassWriters();

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