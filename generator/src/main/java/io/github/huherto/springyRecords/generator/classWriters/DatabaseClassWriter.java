package io.github.huherto.springyRecords.generator.classWriters;

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

    @Override
    public void makeClass(DatabaseTool dbTool) {
	    try {
	    	File sourceFile = sourceFile(getMainSourceDir(), dbTool.baseDatabasePackageName(), dbTool.databaseClassName());
            if (sourceFile.exists()) {
                logger.info("Skipping source "+sourceFile);
                return;
            }
	        writeCode(sourceFile, createTemplate(), dbTool);
	    }
	    catch(Exception ex) {
	        logger.error(ex);
	        throw new RuntimeException(ex);
	    }
	}

    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("database.mustache");
    }

}
