---
layout: page
title: Simple Queries
---

## Simple Query Examples


This was added.

### Get all the records from a table.

{% highlight java %}

PetStoreDatabase petStoreDb = new PetStoreDatabase(createDs());

List<OwnerRecord> all = petStoreDb.getOwnerTable().queryAll();

{% endhighlight %}


### Get exactly one record.

Add a reusable adhoc method to the OwnerTable

{% highlight java %}

public class OwnerTable extends BaseOwnerTable {

    public OwnerRecord findOwnerById(int id) {
		return queryForRequiredObject("select * from owner where owner_id = ?", id);
    }

}

{% endhighlight %}

Using the method is trivial...

{% highlight java %}

PetStoreDatabase petStoreDb = new PetStoreDatabase(createDs());

OwnerRecord ownerRecord =  petStoreDb.getOwnerTable().findOwnerById(10);

{% endhighlight %}

Use the queryForRequiredObject() when you know there must be one and only one records in the table that satisfy the condition. Such is the case when you have referential constrains on the field. queryForRequiredObject() will throw a DataAccessException if that is not the case.

### Get a list of records 

Add a reusable adhoc method to the OwnerTable

{% highlight java %}

public class OwnerTable extends BaseOwnerTable {

	public List<OwnerRecord> findByName(String name) {

		return query("select * from owner where name = ?", name);
	}
}

{% endhighlight %}

Using the method is easy and the intent of the code is pretty clear.

{% highlight java %}

PetStoreDatabase petStoreDb = new PetStoreDatabase(createDs());

List<OwnerRecord> result = petStoreDb.getOwnerTable().findByName("Humberto");

{% endhighlight %}
