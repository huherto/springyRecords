package io.github.huherto.springyRecords.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BaseOwnerRecord implements BaseRecord {
    public int ownerId;
    public String name;

    public BaseOwnerRecord() {
    }

    public BaseOwnerRecord(BaseOwnerRecord other) {
        this.ownerId = other.ownerId;
        this.name = other.name;
    }

    public BaseOwnerRecord(ResultSet rs, int rowNum) throws SQLException {
        this.ownerId = rs.getInt("OWNER_ID");
        this.name = rs.getString("NAME");
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("OWNER_ID", this.ownerId);
        map.put("NAME", this.name);
        return map;
    }
}
