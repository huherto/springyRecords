package com.github.springRecords.test;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import com.github.springRecords.BaseRecord;
import com.github.springRecords.BaseTable;
import com.github.springRecords.RecordMapper;

/**
 * OwnerTable – 
 * 
 */
@Repository
public class OwnerTable extends BaseTable<OwnerRecord> {

    private RowMapper<OwnerRecord> rm = RecordMapper.newInstance(OwnerRecord.class);

    public OwnerTable(DataSource dataSource) {
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
