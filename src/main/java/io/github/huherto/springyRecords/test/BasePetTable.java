package io.github.huherto.springyRecords.test;

import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import io.github.huherto.springyRecords.BaseTable;
import io.github.huherto.springyRecords.DtoRowMapper;

public class BasePetTable extends BaseTable<PetRecord> {

    private RowMapper<PetRecord> rm = DtoRowMapper.newInstance(PetRecord.class);

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

    public PetRecord findObject(String name) {
        String sql =
            "select * "+
            "from PET "+
            "where NAME  = ? ";

        return singleResult(sql, name);
    }

    public PetRecord fetchObject(String name) {
        String sql =
            "select * "+
            "from PET "+
            "where NAME  = ? ";

        return requiredSingleResult(sql, name);
    }


}
