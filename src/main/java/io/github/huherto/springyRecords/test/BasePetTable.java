 package io.github.huherto.springyRecords.test;

import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public Optional<PetRecord> findOptional(String name) {
        String sql =
            "select * "+
            "from PET "+
            "where NAME  = ? ";

        return optionalSingle(sql, name);
    }

    public PetRecord findRequired(String name) {
        String sql =
            "select * "+
            "from PET "+
            "where NAME  = ? ";

        return requiredSingle(sql, name);
    }


}
