---
layout: page
title: Getting started
---

It is very easy to get started and create your own customized code generator.

Create a project with a pom.xml like this one.

{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>example-generator</artifactId>
    <version>0.1</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.7.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>io.github.huherto</groupId>
            <artifactId>springyRecords</artifactId>
            <version>1.04-SNAPSHOT</version>
        </dependency>
        <dependency> <!-- Change this for which ever database driver is being used -->
            <groupId>org.hsqldb</groupId>
            <artifactId>hsqldb</artifactId>
            <version>2.4.0</version>            
        </dependency>        
    </dependencies>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
{% endhighlight %}

You will also need the dependencies for the driver to connect to your database.

### Executing the code generator.

If you already have JUnit set up. This is the easiest way is to execute the generator.

{% highlight java %}
package com.example.generator;


import java.nio.file.Paths;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import io.github.huherto.springyRecords.generator.SchemaCrawlerGenerator;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Override
	public void run(String... args) {	
	    generateExample();
	}

	private void generateExample() {
	    
	    System.out.println("start generateExample");
	    
	    SchemaCrawlerGenerator dbGenerator = new SchemaCrawlerGenerator(dataSource(), "com.example", "Example");
	    
	    dbGenerator.setBaseDir(Paths.get("../example-generated"));
	    
	    dbGenerator.printInformationSchema(null);
	    dbGenerator.processTableList("PUBLIC.PUBLIC", Arrays.asList("OWNER","pet"));
        dbGenerator.processAllTables("PUBLIC.PUBLIC");
        
    }

    public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
	
    @Bean
	public DataSource dataSource() {

        return new EmbeddedDatabaseBuilder()
                .setName("test")
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:schema.sql")
                .build();	    	    
	}
}

{% endhighlight %}

Using maven and spring boot it is very easy to build and run.

{% highlight bash %}

mvn clean package

# Use whatever name 

java -jar target/example-generator-0.1.jar 

{% endhighlight %}

In this example, the code will be generated in ../example-generated/src/main/java/com/example/*.java

For a more uptodate example copy this subdirectory.

<a href="{{ site.github.repo}}/tree/master/example-generator">"{{ site.github.repo}}/tree/master/example-generator"</a>

