package com.github.springRecords;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

/**
 * Implementation adapted from BeanPropertyRowMapper
 */
public class RecordMapper<T> implements RowMapper<T> {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	/** The class we are mapping to */
	private Class<T> mappedClass;

	/** Whether we're defaulting primitives when mapping a null value */
	private boolean primitivesDefaultedForNullValue = false;

	private final boolean autoTrimStrings = true;

	private Map<String, Field> mappedFields;

	/**
	 * Create a new DtoRowMapper for bean-style configuration.
	 * @see #setMappedClass
	 * @see #setCheckFullyPopulated
	 */
	public RecordMapper() {
	}

	/**
	 * Create a new DtoRowMapper, accepting unpopulated properties
	 * in the target bean.
	 * <p>Consider using the {@link #newInstance} factory method instead,
	 * which allows for specifying the mapped type once only.
	 * @param mappedClass the class that each row should be mapped to
	 */
	public RecordMapper(Class<T> mappedClass) {
		initialize(mappedClass);
	}

	/**
	 * Create a new DtoRowMapper.
	 * @param mappedClass the class that each row should be mapped to
	 * @param checkFullyPopulated whether we're strictly validating that
	 * all bean properties have been mapped from corresponding database fields
	 */
	public RecordMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
		initialize(mappedClass);
	}


	/**
	 * Set the class that each row should be mapped to.
	 */
	public void setMappedClass(Class<T> mappedClass) {
		if (this.mappedClass == null) {
			initialize(mappedClass);
		}
		else {
			if (!this.mappedClass.equals(mappedClass)) {
				throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " +
						mappedClass + " since it is already providing mapping for " + this.mappedClass);
			}
		}
	}

	/**
	 * Initialize the mapping metadata for the given class.
	 * @param mappedClass the mapped class.
	 */
	protected void initialize(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
		this.mappedFields = new HashMap<String, Field>();

		Field fields[] = mappedClass.getFields();
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isPublic(mod) && !Modifier.isStatic(mod)) {
				for(Annotation a: field.getAnnotations()) {
					if (a.annotationType().isAssignableFrom(Column.class)) {
						Column c = (Column)a;
						String columnName = c.name();
						mappedFields.put(columnName, field);
					}
				}
			}
		}
	}

	/**
	 * Get the class that we are mapping to.
	 */
	public final Class<T> getMappedClass() {
		return this.mappedClass;
	}

	/**
	 * Set whether we're defaulting Java primitives in the case of mapping a null value
	 * from corresponding database fields.
	 * <p>Default is <code>false</code>, throwing an exception when nulls are mapped to Java primitives.
	 */
	public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
		this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
	}

	/**
	 * Return whether we're defaulting Java primitives in the case of mapping a null value
	 * from corresponding database fields.
	 */
	public boolean isPrimitivesDefaultedForNullValue() {
		return primitivesDefaultedForNullValue;
	}


	/**
	 * Extract the values for all columns in the current row.
	 * <p>Utilizes public setters and result set metadata.
	 * @see java.sql.ResultSetMetaData
	 */
	@Override
	public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Assert.state(this.mappedClass != null, "Mapped class was not specified");

		T mappedObject;
		try {
			mappedObject = mappedClass.newInstance();
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			Field field = this.mappedFields.get(column.replaceAll(" ", "").toLowerCase());
			if (field != null) {
				Object value = getColumnValue(rs, index, field);
				if (logger.isTraceEnabled() && rowNumber == 0) {
					logger.trace("Mapping column '" + column + "' to property '" +
							field.getName() + "' of type " + field.getType());
				}
				try {
					field.set(mappedObject, value);
				}
				catch (IllegalArgumentException e) {
					if (value == null && primitivesDefaultedForNullValue) {
						logger.debug("Intercepted IllegalArgumentException for row " + rowNumber +
								" and column '" + column + "' with value " + value +
								" when setting property '" + field.getName() + "' of type " + field.getType() +
								" on object: " + mappedObject);
					}
					else {
						throw e;
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return mappedObject;
	}

	/**
	 * Retrieve a JDBC object value for the specified column.
	 * <p>The default implementation calls
	 * {@link JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)}.
	 * Subclasses may override this to check specific value types upfront,
	 * or to post-process values return from <code>getResultSetValue</code>.
	 * @param rs is the ResultSet holding the data
	 * @param index is the column index
	 * @param pd the bean property that each result object is expected to match
	 * (or <code>null</code> if none specified)
	 * @return the Object value
	 * @throws SQLException in case of extraction failure
	 * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)
	 */
	protected Object getColumnValue(ResultSet rs, int index, Field field) throws SQLException {

		Object value = JdbcUtils.getResultSetValue(rs, index, field.getType());
		if (value != null &&  value instanceof String && autoTrimStrings) {
			return ((String)value).trim();
		}
		return value;
	}


	/**
	 * Static factory method to create a new DtoRowMapper
	 * (with the mapped class specified only once).
	 * @param mappedClass the class that each row should be mapped to
	 */
	public static <T> RecordMapper<T> newInstance(Class<T> mappedClass) {
		RecordMapper<T> newInstance = new RecordMapper<T>();
		newInstance.setMappedClass(mappedClass);
		return newInstance;
	}

}
