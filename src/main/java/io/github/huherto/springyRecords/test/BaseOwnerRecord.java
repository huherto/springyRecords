package io.github.huherto.springyRecords.test;

import io.github.huherto.springyRecords.Autoincrement;
import io.github.huherto.springyRecords.BaseRecord;
import io.github.huherto.springyRecords.Column;

/**
 * BaseOwnerRecord –
 * Automatically generated. Do not modify or your changes might be lost.
 */
public class BaseOwnerRecord extends BaseRecord {
    @Autoincrement
    @Column(name="owner_id", sqlType=java.sql.Types.INTEGER)
    public int ownerId;
    
    @Column(name="name", sqlType=java.sql.Types.VARCHAR)
    public String name;
    
    public BaseOwnerRecord() {        
    }
    
    public BaseOwnerRecord(BaseOwnerRecord other) {
        this.ownerId = other.ownerId;
        this.name = other.name;
    }
}
