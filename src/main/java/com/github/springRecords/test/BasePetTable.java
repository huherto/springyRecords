package com.github.springRecords.test;

import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

import com.github.springRecords.BaseRecord;
import com.github.springRecords.BaseTable;
import com.github.springRecords.RecordMapper;

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
    protected RowMapper<PetRecord> rowMapper() {
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
