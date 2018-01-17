package io.github.huherto.springyRecords.generator.classWriters.springjdbc;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.github.huherto.springyRecords.generator.tools.DatabaseTool;

public class DatabaseClassWriter extends BaseClassWriter<DatabaseTool> {

	public DatabaseClassWriter(Path baseDir) {
        super(baseDir);
    }

    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("database.mustache");
    }

    @Override
    public File sourceFile(DatabaseTool dbTool) {
        return 
                sourceFile(
                        getMainSourceDir(),
                        dbTool.baseDatabasePackageName(), 
                        dbTool.databaseClassName());
    }

    @Override
    public boolean overwriteExistingFile() {
        return false;
    }

}
