package io.github.huherto.springyRecords.test;

import java.util.HashMap;
import java.util.Map;

public class BaseOwnerRecord extends BaseRecord {
    public int ownerId;
    public String name;

    public BaseOwnerRecord() {
    }

    public BaseOwnerRecord(BaseOwnerRecord other) {
        this.ownerId = other.ownerId;
        this.name = other.name;
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("OWNER_ID", this.ownerId);
        map.put("NAME", this.name);
        return map;
    }
}
