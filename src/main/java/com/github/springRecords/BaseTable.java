package com.github.springRecords;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

	protected R queryForObject(String sql, Object...args) {
		return jdbcTemplate.queryForObject(sql, rowMapper(), args);
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
