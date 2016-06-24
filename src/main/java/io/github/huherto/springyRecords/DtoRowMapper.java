package io.github.huherto.springyRecords;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

/**
 * Implementation adapted from BeanPropertyRowMapper
 */
public class DtoRowMapper<T> implements RowMapper<T> {

    /** Logger available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    /** The class we are mapping to */
    private Class<T> mappedClass;

    /** Whether we're strictly validating */
    private boolean checkFullyPopulated = false;

    /** Whether we're defaulting primitives when mapping a null value */
    private boolean primitivesDefaultedForNullValue = false;

    private boolean autoTrimStrings = true;

    private Map<String, Field> mappedFields;

    /** Set of bean properties we provide mapping for */
    private Set<String> mappedProperties;


    /**
     * Create a new DtoRowMapper for bean-style configuration.
     * @see #setMappedClass
     * @see #setCheckFullyPopulated
     */
    public DtoRowMapper() {
    }

    /**
     * Create a new DtoRowMapper, accepting unpopulated properties
     * in the target bean.
     * <p>Consider using the {@link #newInstance} factory method instead,
     * which allows for specifying the mapped type once only.
     * @param mappedClass the class that each row should be mapped to
     */
    public DtoRowMapper(Class<T> mappedClass) {
        initialize(mappedClass);
    }

    /**
     * Create a new DtoRowMapper.
     * @param mappedClass the class that each row should be mapped to
     * @param checkFullyPopulated whether we're strictly validating that
     * all bean properties have been mapped from corresponding database fields
     */
    public DtoRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
        initialize(mappedClass);
        this.checkFullyPopulated = checkFullyPopulated;
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
        this.mappedProperties = new HashSet<String>();

        Field fields[] = mappedClass.getFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isPublic(mod) && !Modifier.isStatic(mod)) {

                String fieldNameV1 = field.getName().toLowerCase();
                String fieldNameV2 = underscoreName(field.getName());
                String fieldNameV3 = underscoreNameR(field.getName());

                this.mappedFields.put(fieldNameV1, field);
                if (!fieldNameV2.equals(fieldNameV1)) {
                    this.mappedFields.put(fieldNameV2, field);
                }
                if (!fieldNameV3.equals(fieldNameV1) && !fieldNameV3.equals(fieldNameV2)) {
                    this.mappedFields.put(fieldNameV3, field);
                }
                this.mappedProperties.add(fieldNameV2);

                /*
                this.mappedFields.put(field.getName().toLowerCase(), field);
                String underscoredName = underscoreName(field.getName());
                if (!field.getName().toLowerCase().equals(underscoredName)) {
                    this.mappedFields.put(underscoredName, field);
                }
                this.mappedProperties.add(underscoredName);
                */
            }
        }
    }

    /**
     * Convert a name in camelCase to an underscored name in lower case.
     * Any upper case letters (or a number) are converted to lower case with a preceding underscore.
     * @param name the string containing original name
     * @return the converted name
     */
    private String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            result.append(name.substring(0, 1).toLowerCase());
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                if (s.equals(s.toUpperCase())) {
                    result.append("_");
                    result.append(s.toLowerCase());
                }
                else {
                    result.append(s);
                }
            }
        }
        return result.toString();
    }

    /**
     * Convert a name in camelCase to an underscored name in lower case.
     * Any upper case letters are converted to lower case with a preceding underscore.
     * @param name the string containing original name
     * @return the converted name
     */
     private String underscoreNameR(String name){
       return name.replaceAll("([A-Z]|\\d+\\w+)", "_$1").toLowerCase();
     }

    /**
     * Get the class that we are mapping to.
     */
    public final Class<T> getMappedClass() {
        return this.mappedClass;
    }

    /**
     * Set whether we're strictly validating that all bean properties have been
     * mapped from corresponding database fields.
     * <p>Default is <code>false</code>, accepting unpopulated properties in the
     * target bean.
     */
    public void setCheckFullyPopulated(boolean checkFullyPopulated) {
        this.checkFullyPopulated = checkFullyPopulated;
    }

    /**
     * Return whether we're strictly validating that all bean properties have been
     * mapped from corresponding database fields.
     */
    public boolean isCheckFullyPopulated() {
        return this.checkFullyPopulated;
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
        Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<String>() : null);

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
                if (populatedProperties != null) {
                    populatedProperties.add(field.getName());
                }
            }
        }

        if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
            throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields " +
                    "necessary to populate object of class [" + this.mappedClass + "]: " + this.mappedProperties);
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
     * Static factory method to update a new DtoRowMapper
     * (with the mapped class specified only once).
     * @param mappedClass the class that each row should be mapped to
     */
    public static <T> DtoRowMapper<T> newInstance(Class<T> mappedClass) {
        DtoRowMapper<T> newInstance = new DtoRowMapper<T>();
        newInstance.setMappedClass(mappedClass);
        return newInstance;
    }

}
