package com.github.springyRecords;
/*
The MIT License (MIT)

Copyright (c) 2014 <copyright holders>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public abstract class  BaseTable<R extends BaseRecord> {

    protected JdbcTemplate jdbcTemplate;
    protected SimpleJdbcInsert insertCommand;
    private final Field autoIncrementField;

    public BaseTable(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        insertCommand = new SimpleJdbcInsert(dataSource);
        insertCommand.withTableName(tableName());

        autoIncrementField = RecordUtils.autoIncrementField(recordClass());
        if (autoIncrementField != null) {
            insertCommand.setGeneratedKeyName(autoIncrementField.getAnnotation(Column.class).name());
        }
    }

    abstract public String tableName();

    abstract public Class<? extends BaseRecord> recordClass();

    protected List<R> query(String sql, Object...args) {
        return jdbcTemplate.query(sql, rowMapper(), args);
    }

    public List<R> queryAll() {
        return query("select * from "+tableName());
    }

    /**
     * @throws DataAccessException if the object is not found.
     */
    protected R queryForRequiredObject(String sql, Object...args) {
        return jdbcTemplate.queryForObject(sql, rowMapper(), args);
    }

    /**
     * @return null if the object is not found.
     */
    protected R queryForSingleObject(String sql, Object...args) {
		List<R> results = query(sql, args, new RowMapperResultSetExtractor<R>(rowMapper(), 1));
		return DataAccessUtils.singleResult(results);
    }

    protected abstract RowMapper<R> rowMapper();

    protected int update(String sql, Object...args) {
        return jdbcTemplate.update(sql, args);
    }

    public Number insert(R record) {

        Field[] fields = recordClass().getFields();
        String autoIncrementFieldName = null;
        if (autoIncrementField != null)
            autoIncrementFieldName = autoIncrementField.getName();
        Map<String, Object> parameters = new HashMap<String, Object>(fields.length);
        for(Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isPublic(mod) && !Modifier.isStatic(mod)) {
                Column col = field.getAnnotation(Column.class);
                if (col != null && !field.getName().equals(autoIncrementFieldName)) {
                    Object value;
                    try {
                        value = field.get(record);
                        parameters.put(col.name(), value );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        if (autoIncrementField != null)
            return insertCommand.executeAndReturnKey(parameters);
        insertCommand.execute(parameters);
        return null;
    }

}
