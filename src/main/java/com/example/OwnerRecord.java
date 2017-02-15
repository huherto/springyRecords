package com.example;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OwnerRecord extends BaseOwnerRecord {

    public OwnerRecord() {
    }

    public OwnerRecord(OwnerRecord other) {
        super(other);
    }

    public OwnerRecord(ResultSet rs, int rowNum) throws SQLException {
        super(rs, rowNum);
    }
}
