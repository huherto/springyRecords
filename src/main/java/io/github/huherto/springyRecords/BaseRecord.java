package io.github.huherto.springyRecords;

import java.util.Map;

public abstract class BaseRecord {

    // public static abstract RowMapper<R extends BaseRecord> rowMapper();

    abstract public Map<String, Object> asMap();

}
