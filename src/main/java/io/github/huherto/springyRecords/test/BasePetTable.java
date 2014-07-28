package io.github.huherto.springyRecords.test;

import io.github.huherto.springyRecords.BaseRecord;
import io.github.huherto.springyRecords.BaseTable;
import io.github.huherto.springyRecords.RecordMapper;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

/**
 * BasePetTable –
 * Automatically generated. Do not modify or your changes might be lost.
 */
public class BasePetTable extends BaseTable<PetRecord> {

    private RowMapper<PetRecord> rm = RecordMapper.newInstance(PetRecord.class);

    public BasePetTable(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public RowMapper<PetRecord> rowMapper() {
        return rm;
    }

    @Override
    public String tableName() {
        return "pet";
    }

    @Override
    public Class<? extends BaseRecord> recordClass() {
        return PetRecord.class;
    }
}
