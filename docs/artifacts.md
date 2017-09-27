---
layout: page
title: Artifacts
---
## There are five types of artifacts generated.

### Class Diagram

<p class="message">
<img src="../public/sr-d1.png" />
</p>

1 - Concrete Record class, that you can modify. The Record classes follow the DTO (Data Transfer Object) pattern. 
{% highlight java %}
/**
 * OwnerRecord
 */
 public class OwnerRecord extends BaseOwnerRecord {
     // Add your own methods here...
 }
{% endhighlight %}

2 - Annotated Base Record class that is regenerated if the table changes.
{% highlight java %}
/**
 * BaseOwnerRecord
 * Automatically generated. Do not modify or your changes might be lost.
 */
public class BaseOwnerRecord extends BaseRecord {
    @Autoincrement
    @Column(name="owner_id", sqlType=java.sql.Types.INTEGER)
    public int ownerId;

    @Column(name="name", sqlType=java.sql.Types.VARCHAR)
    public String name;
}
{% endhighlight %}

3 - Concrete Table class, that you can modify. The Table clases follow the TDG (Table Data Gateway) pattern

{% highlight java %}
public class OwnerTable extends BaseOwnerTable {
    public OwnerTable(DataSource dataSource) {
        super(dataSource);
    }

    // Add your own methods here.
}
{% endhighlight %}

4 -  Base Table class that contains the automatically generated methods.

{% highlight java %}
/**
 * BaseOwnerTable
 * Automatically generated. Do not modify or your changes might be lost.
 */
public class BaseOwnerTable extends BaseTable<OwnerRecord> {

    private RowMapper<OwnerRecord> rm = RecordMapper.newInstance(OwnerRecord.class);

    public BaseOwnerTable(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<OwnerRecord> rowMapper() {
        return rm;
    }

    @Override
    public String tableName() {
        return "owner";
    }

    @Override
    public Class<? extends BaseRecord> recordClass() {
        return OwnerRecord.class;
    }
}
{% endhighlight %}
