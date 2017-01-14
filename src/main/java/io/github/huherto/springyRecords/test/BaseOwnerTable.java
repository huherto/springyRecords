package io.github.huherto.springyRecords.test;

import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import io.github.huherto.springyRecords.BaseTable;
import io.github.huherto.springyRecords.DtoRowMapper;

public class BaseOwnerTable extends BaseTable<OwnerRecord> {

    private RowMapper<OwnerRecord> rm = DtoRowMapper.newInstance(OwnerRecord.class);

    public BaseOwnerTable(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public RowMapper<OwnerRecord> rowMapper() {
        return rm;
    }

    @Override
    public String tableName() {
        return "OWNER";
    }

    public OwnerRecord findObject(int ownerId) {
        String sql =
            "select * "+
            "from OWNER "+
            "where OWNER_ID  = ? ";

        return singleResult(sql, ownerId);
    }

    public OwnerRecord fetchObject(int ownerId) {
        String sql =
            "select * "+
            "from OWNER "+
            "where OWNER_ID  = ? ";

        return requiredSingleResult(sql, ownerId);
    }


}
