package com.example;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PetRecord extends BasePetRecord {

    public PetRecord() {
    }

    public PetRecord(PetRecord other) {
        super(other);
    }

    public PetRecord(ResultSet rs, int rowNum) throws SQLException {
        super(rs, rowNum);
    }
}
