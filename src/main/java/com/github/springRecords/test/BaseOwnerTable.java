package com.github.springRecords.test;

import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

import com.github.springRecords.BaseRecord;
import com.github.springRecords.BaseTable;
import com.github.springRecords.RecordMapper;

/**
 * BaseOwnerTable –
 * Automatically generated. Do not modify or your changes might be lost.
 */
public class BaseOwnerTable extends BaseTable<OwnerRecord> {

    private RowMapper<OwnerRecord> rm = RecordMapper.newInstance(OwnerRecord.class);

    public BaseOwnerTable(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<OwnerRecord> rowMapper() {
        return rm;
    }

    @Override
    public String tableName() {
        return "owner";
    }

    @Override
    public Class<? extends BaseRecord> recordClass() {
        return OwnerRecord.class;
    }
}
