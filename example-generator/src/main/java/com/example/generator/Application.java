package com.example.generator;


import static java.util.Arrays.asList;

import java.nio.file.Paths;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import io.github.huherto.springyRecords.generator.SchemaCrawlerGenerator;

/*
 * Modify this file to suit your needs.
 * 
 * 
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
    
	@Override
	public void run(String... args) {	
	    generateExample();
	}

	private void generateExample() {	    
	    
	    String packageName = "com.example";  // Modify this.
	    String databaseClassName = "Example"; // Modify this.
	    SchemaCrawlerGenerator dbGenerator = new SchemaCrawlerGenerator(dataSource(), packageName, databaseClassName);
	    
	    // Base directory where code will be created.
	    dbGenerator.setBaseDir(Paths.get("../example-generated"));
	    
	    // Use this to discover schemas and tables.
	    dbGenerator.printInformationSchema(null);

	    // Use this if you only want a subset of tables in a schema.
	    dbGenerator.processTableList("PUBLIC.PUBLIC", asList("OWNER","PET"));

	    // Use this if you want to generate code for all the tables in a schema.
        dbGenerator.processAllTables("PUBLIC.PUBLIC");

    }

	// Use this data source to create an embedded database.
    @Bean
	public DataSource dataSource() {

        // Replace this with your own datasource.
        return new EmbeddedDatabaseBuilder()
                .setName("test")
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:hsql-schema.sql")
                .build();	    	    
	}
    
    // Use this data source to connect to an existing database.
    @Bean
    public DataSource dataSource_plain() {
        
        SimpleDriverDataSource ds =
                new SimpleDriverDataSource();
        
        ds.setDriverClass(null);
        ds.setUrl("jdbc:oracle:thin:@<server>[:<1521>]:<database_name>");
        ds.setUsername("");
        ds.setPassword("");
        
        return ds;
    }    
}
