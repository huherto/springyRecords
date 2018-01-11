 package com.example;

import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BaseOwnerTable extends AbstractBaseTable<OwnerRecord> {

    private RowMapper<OwnerRecord> rm = new RowMapper<OwnerRecord>() {
        @Override
        public OwnerRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
             return new OwnerRecord(rs, rowNum);
        }
    };

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
    
    @Override
    public String schemaName() {
        return "PUBLIC";
    }

    public Optional<OwnerRecord> findByPK(int ownerId) {
        String sql =
            selectStar() +
            "where OWNER_ID  = ? ";

        return optionalSingle(sql, ownerId);
    }
    
    public List<OwnerRecord> findByOwnerId(int ownerId) {
        String sql =
            selectStar() +
            "where OWNER_ID  = ? ";

        return query(sql, ownerId);
    }
    
    
}
