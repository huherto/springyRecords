package io.github.huherto.springyRecords.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BasePetRecord implements BaseRecord {
    private String name;
    private String owner;
    private String species;
    private String sex;
    private Date birthDate;
    private Date death;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getDeath() {
        return death;
    }

    public void setDeath(Date death) {
        this.death = death;
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
