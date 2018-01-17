package io.github.huherto.springyRecords.generator.classWriters;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.github.huherto.springyRecords.generator.classWriters.springjdbc.ClassWriter;
import io.github.huherto.springyRecords.generator.tools.DatabaseTool;
import io.github.huherto.springyRecords.generator.tools.TableTool;

public abstract class AbstractClassWriterRegistry implements ClassWriterRegistry {

    private List< ClassWriter<TableTool> > classWritersForTables = new ArrayList<>();
    private List< ClassWriter<DatabaseTool> > classWritersForAll = new ArrayList<>();

    public AbstractClassWriterRegistry() {
        super();
    }

    public void addClassWriterForTable(ClassWriter<TableTool> cw) {
        classWritersForTables.add(cw);
    }

    public void addClassWriter(ClassWriter<DatabaseTool> cw) {
        classWritersForAll.add(cw);
    }

    @Override
    public void writeAllClasses(DatabaseTool dbTool, Path baseDir) {
        
        registerClassWriters(baseDir);
        
        for(ClassWriter<DatabaseTool> cw : classWritersForAll) {
            cw.makeClass(dbTool);
        }
    
        for(TableTool tableTool : dbTool.getTables() ) {
            for(ClassWriter<TableTool> cw : classWritersForTables) {
                cw.makeClass(tableTool);
            }
        }
    }

    abstract protected void registerClassWriters(Path baseDir);

}