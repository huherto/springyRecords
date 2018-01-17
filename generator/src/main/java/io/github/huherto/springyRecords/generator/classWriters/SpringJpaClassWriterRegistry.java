package io.github.huherto.springyRecords.generator.classWriters;

import java.nio.file.Path;

import io.github.huherto.springyRecords.generator.classWriters.springjpa.EntityClassWriter;

public class SpringJpaClassWriterRegistry extends AbstractClassWriterRegistry {

    @Override
    protected void registerClassWriters(Path baseDir) {
        addClassWriterForTable(new EntityClassWriter(baseDir));
    }

}
