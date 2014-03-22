package com.github.springyRecords.test;

import com.github.springyRecords.Autoincrement;
import com.github.springyRecords.BaseRecord;
import com.github.springyRecords.Column;

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
}
