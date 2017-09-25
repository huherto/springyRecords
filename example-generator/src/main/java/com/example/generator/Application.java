package com.example.generator;


import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
	    
	    SchemaCrawlerGenerator dbGenerator = new SchemaCrawlerGenerator(dataSource(), "com.example", "Example");
	    
	    dbGenerator.printInformationSchema(null);
	    dbGenerator.processTableList("PUBLIC.PUBLIC", Arrays.asList("OWNER","pet"));
        dbGenerator.processAllTables("PUBLIC.PUBLIC");
        
    }

    public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
	
	public DataSource dataSource() {

        return new EmbeddedDatabaseBuilder()
                .setName("test")
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:schema.sql")
                .build();	    	    
	}
}
