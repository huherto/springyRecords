package io.github.huherto.springyRecords.generator.tools;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTool extends BaseTool {

    private List<TableTool> tables = new ArrayList<>();

    private String databaseClassName;

    public DatabaseTool(String packageName, String databaseClassName) {
        this.basePackageName = packageName;
        this.databaseClassName = databaseClassName;
    }

    public void add(TableTool tableTool) {
        tables.add(tableTool);
    }

    public List<TableTool> getTables() {
        return tables;
    }

    public String baseDatabaseClassName() {
        return "BaseDatabase";
    }

    public String baseDatabasePackageName() {
        return basePackageName;
    }

    public String databaseClassName() {
        return databaseClassName + "Database";
    }
}
