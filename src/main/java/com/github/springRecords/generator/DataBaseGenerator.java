package com.github.springRecords.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.io.Files;

public class DataBaseGenerator {

	private static final Logger logger = Logger.getLogger(DataBaseGenerator.class);

	private final String schema;
	
	private final String catalog;

	private final String packageName;

	DataSource ds;

	public DataBaseGenerator(DataSource ds, String catalog, String schema, String packageName) {
		this.ds = ds;
		this.catalog = catalog;
		this.schema = schema;
		this.packageName = packageName;
	}

	public File sourceDir() {
		return new File(new File(new File(new File(System.getProperty("user.dir")),"src"), "main"), "java");
	}

	public File sourceFile(String packageName, String className) throws IOException {

		File dir =sourceDir();
		String[] segments = packageName.split("\\.");

		for(String seg : segments) {
			dir = new File(dir, seg);
		}

		File sourceFile = new File(dir, className + ".java");
		Files.createParentDirs(sourceFile);

		return sourceFile;
	}

	public void makeConcreteRecord(TableTool tableTool, String tableName) {
		try {
			String className = tableTool.concreteRecordClassName();
			File sourceFile = sourceFile(packageName, className);
			if (sourceFile.exists()) {
				logger.info("Skipping source "+sourceFile);
				return;
			}

			writeCode(sourceFile, concreteRecordTemplate(), tableTool);
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Mustache concreteRecordTemplate() {
		MustacheFactory mf = new DefaultMustacheFactory();
		return mf.compile("record.mustache");
	}

	public void makeBaseRecord(TableTool tableTool, String tableName) {
		try {
			File sourceFile = sourceFile(tableTool.baseRecordPackageName(), tableTool.baseRecordClassName());
			if (sourceFile.exists()) {
				sourceFile.delete();
				sourceFile.createNewFile();
			}
			writeCode(sourceFile, baseRecordTemplate(), tableTool);
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void makeTable(TableTool tableTool, String tableName) {
		try {
			File sourceFile = sourceFile(tableTool.tablePackageName(), tableTool.tableClassName());
			if (sourceFile.exists()) {
				logger.info("Skipping source "+sourceFile);
				return;
			}
			writeCode(sourceFile, tableTemplate(), tableTool);
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Mustache tableTemplate() {
		MustacheFactory mf = new DefaultMustacheFactory();
		return mf.compile("table.mustache");
	}

	public Mustache baseRecordTemplate() {
		MustacheFactory mf = new DefaultMustacheFactory();
		return mf.compile("baserecord.mustache");
	}

	public void writeCode(File sourceFile, Mustache template, TableTool tableTool) {
		try {
			logger.info("Creating source "+sourceFile);
			Writer writer = new FileWriter(sourceFile);
			template.execute(writer, tableTool);
			writer.flush();
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void processTableList(List<String> tableNames) {

		for(String tableName : tableNames) {
			TableTool tableTool = TableTool.createTableTool(ds, catalog, schema, tableName, packageName);
			makeBaseRecord(tableTool, tableName);
			makeConcreteRecord(tableTool, tableName);
			makeTable(tableTool, tableName);
		}
	}

	public void processAllTables() {

		JdbcTemplate jt = new JdbcTemplate(ds);
		String sql =
				"select table_name " +
				"from information_schema.tables ";
//				"where table_catalog = ? and table_schema = ?";

		List<String> tableNames = jt.queryForList(sql, String.class);
		if (tableNames.size() != 0) {
			String select = String.format(
					"select table_name " +
					"from information_schema.tables " +
					"where table_catalog = '%s' and table_schema = '%s' ", catalog, schema);
			logger.warn("Can't find tables: SQL ["+select+"]");
		}

		processTableList(tableNames);

	}

}
