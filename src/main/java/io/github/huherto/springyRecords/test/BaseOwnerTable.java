package io.github.huherto.springyRecords.test;

import io.github.huherto.springyRecords.BaseRecord;
import io.github.huherto.springyRecords.BaseTable;
import io.github.huherto.springyRecords.RecordMapper;

import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

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
