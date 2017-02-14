package io.github.huherto.springyRecords.generator.tools;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTool extends BaseTool {

    private List<TableTool> tables = new ArrayList<>();

    public DatabaseTool(String packageName) {
        this.basePackageName = packageName;
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

}
