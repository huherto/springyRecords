package io.github.huherto.springyRecords.generator.classWriters;

import java.nio.file.Path;

import io.github.huherto.springyRecords.generator.classWriters.springjdbc.ABTableClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.springjdbc.BTableITClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.springjdbc.BaseDatabaseClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.springjdbc.BaseRecordClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.springjdbc.BaseTableClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.springjdbc.ConcreteRecordClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.springjdbc.ConcreteTableClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.springjdbc.DatabaseClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.springjdbc.InterfaceRecordClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.springjdbc.TableITClassWriter;

public class SpringJdbcClassWriterRegistry extends AbstractClassWriterRegistry {

    protected void registerClassWriters(Path baseDir) {
        addClassWriterForTable(new BaseRecordClassWriter(baseDir));
        addClassWriterForTable(new ConcreteRecordClassWriter(baseDir));
        addClassWriterForTable(new BaseTableClassWriter(baseDir));
        addClassWriterForTable(new ConcreteTableClassWriter(baseDir));
        addClassWriterForTable(new TableITClassWriter(baseDir));
        addClassWriter(new DatabaseClassWriter(baseDir));
        addClassWriter(new ABTableClassWriter(baseDir));
        addClassWriter(new InterfaceRecordClassWriter(baseDir));
        addClassWriter(new BaseDatabaseClassWriter(baseDir));        
        addClassWriter(new BTableITClassWriter(baseDir));        
    }
    
    
}
