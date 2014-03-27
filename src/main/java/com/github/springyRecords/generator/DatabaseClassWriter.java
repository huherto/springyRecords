package com.github.springyRecords.generator;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class DatabaseClassWriter extends BaseClassWriter {

	public void makeClass(Path sourceDir, DatabaseTool dbTool) {
	    try {
	    	File sourceFile = sourceFile(sourceDir, dbTool.baseDatabasePackageName(), dbTool.baseDatabaseClassName());
	        if (sourceFile.exists()) {
	            sourceFile.delete();
	            sourceFile.createNewFile();
	        }
	        writeCode(sourceFile, createTemplate(), dbTool);
	    }
	    catch(Exception ex) {
	        throw new RuntimeException(ex);
	    }
	}

    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("basedatabase.mustache");
    }

}
