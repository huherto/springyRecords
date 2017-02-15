package io.github.huherto.springyRecords.generator;

import javax.sql.DataSource;

import io.github.huherto.springyRecords.generator.classWriters.ABTableClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.BaseRecordClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.BaseTableClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.ConcreteRecordClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.ConcreteTableClassWriter;
import io.github.huherto.springyRecords.generator.classWriters.DatabaseClassWriter;

public class DefaultSchemaCrawlerGenerator extends SchemaCrawlerGenerator {

    public DefaultSchemaCrawlerGenerator(DataSource ds, String packageName) {
        super(ds, packageName);

        addClassWriterForTable(new BaseRecordClassWriter(getBaseDir()));
        addClassWriterForTable(new ConcreteRecordClassWriter(getBaseDir()));
        addClassWriterForTable(new BaseTableClassWriter(getBaseDir()));
        addClassWriterForTable(new ConcreteTableClassWriter(getBaseDir()));
        addClassWriter(new DatabaseClassWriter(getBaseDir()));
        addClassWriter(new ABTableClassWriter(getBaseDir()));

    }

}
