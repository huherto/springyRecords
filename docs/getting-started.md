---
layout: page
title: Getting started
---


### Download.

Using maven is a simple as adding a dependency in the pom.xml

{% highlight xml %}
<dependency>
    <groupId>io.github.huherto</groupId>
    <artifactId>springyRecords</artifactId>
    <version>1.01</version>
</dependency>
{% endhighlight %}

You will also need the dependencies for the driver to connect to your database.

### Executing the code generator.

If you already have JUnit set up. This is the easiest way is to execute the generator.

{% highlight java %}
package com.litesite.dal;

import org.apache.commons.dbcp.BasicDataSource;
import org.hsqldb.jdbc.JDBCDriver;
import org.junit.Test;

import com.github.springRecords.generator.DataBaseGenerator;

public class MyDatabase {

	@Test
	public void generate() {

		// Step 1 - Create a DataSource to connect to your existing Database.
		BasicDataSource ds = new BasicDataSource();
		ds.setUrl("jdbc:hsqldb:hsql://localhost/test");
		ds.setUsername("sa");
		ds.setPassword("");
		ds.setDriverClassName(JDBCDriver.class.getName());

		// Step 2 - Create a DataBaseGenerator.
		String packageName = "com.litesite.dal";
		DataBaseGenerator dbgenerator = new DataBaseGenerator(ds, packageName);

		// Step 2.5 - Use this to find out the names of catalog, schema and tables available.
		dbgenerator.printInformationSchema();

		// Step 3 - Generate the code for all the tables in the catalog and schema.
		String catalog = "PUBLIC";
		String schema  = "PUBLIC";
		dbgenerator.processAllTables(catalog, schema);
	}
}

{% endhighlight %}

The code is easy to execute in your IDE or in maven like this.

{% highlight bash %}

mvn test -Dtest=MyDatabase

{% endhighlight %}

In this example, the code will be generated in ./src/main/java/com/litesite/dal/*.java

