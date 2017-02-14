package io.github.huherto.springyRecords.generator.classWriters;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.github.huherto.springyRecords.generator.tools.DatabaseTool;

public class CommonBaseTableClassWriter extends BaseClassWriter {
	
	public void makeClass(Path sourceDir, DatabaseTool dbTool) {
	    try {
	    	File sourceFile =
	    		sourceFile(sourceDir,
	    				dbTool.baseDatabasePackageName(),
	    				"AbstractBaseTable");
	        if (sourceFile.exists()) {
                logger.info("Skipping source "+sourceFile);
                return;
	        }
	        writeCode(sourceFile, createTemplate(), dbTool);
	    }
	    catch(Exception ex) {
	        throw new RuntimeException(ex);
	    }
	}

    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("commontable.mustache");
    }	

}
