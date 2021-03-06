 package com.example;

import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BasePetTable extends AbstractBaseTable<PetRecord> {

    private RowMapper<PetRecord> rm = new RowMapper<PetRecord>() {
        @Override
        public PetRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
             return new PetRecord(rs, rowNum);
        }
    };

    public BasePetTable(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public RowMapper<PetRecord> rowMapper() {
        return rm;
    }

    @Override
    public String tableName() {
        return "PET";
    }
    
    @Override
    public String schemaName() {
        return "PUBLIC";
    }

    public Optional<PetRecord> findByPK(String name) {
        String sql =
            selectStar() +
            "where NAME  = ? ";

        return optionalSingle(sql, name);
    }
    
    public List<PetRecord> findByName(String name) {
        String sql =
            selectStar() +
            "where NAME  = ? ";

        return query(sql, name);
    }
    
    
}
