package io.github.huherto.springyRecords.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BasePetRecord implements BaseRecord {
    public String name;
    public String owner;
    public String species;
    public String sex;
    public Date birthDate;
    public Date death;

    public BasePetRecord() {
    }

    public BasePetRecord(BasePetRecord other) {
        this.name = other.name;
        this.owner = other.owner;
        this.species = other.species;
        this.sex = other.sex;
        this.birthDate = other.birthDate;
        this.death = other.death;
    }

    public BasePetRecord(ResultSet rs, int rowNum) throws SQLException {
        this.name = rs.getString("NAME");
        this.owner = rs.getString("OWNER");
        this.species = rs.getString("SPECIES");
        this.sex = rs.getString("SEX");
        this.birthDate = rs.getDate("BIRTH_DATE");
        this.death = rs.getDate("DEATH");
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("NAME", this.name);
        map.put("OWNER", this.owner);
        map.put("SPECIES", this.species);
        map.put("SEX", this.sex);
        map.put("BIRTH_DATE", this.birthDate);
        map.put("DEATH", this.death);
        return map;
    }
}
