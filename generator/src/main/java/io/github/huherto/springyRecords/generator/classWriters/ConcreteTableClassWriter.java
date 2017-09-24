package io.github.huherto.springyRecords.generator.classWriters;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.github.huherto.springyRecords.generator.tools.TableTool;

public class ConcreteTableClassWriter extends BaseClassWriter<TableTool>  {

	public ConcreteTableClassWriter(Path baseDir) {
        super(baseDir);
    }

    @Override
    public void makeClass(TableTool tableTool) {
	    try {
	    	File sourceFile =
	    		sourceFile(getMainSourceDir(),
	    				tableTool.concreteTablePackageName(),
	    				tableTool.concreteTableClassName());
	        if (sourceFile.exists()) {
                logger.info("Skipping source "+sourceFile);
                return;
	        }
	        writeCode(sourceFile, createTemplate(), tableTool);
	    }
	    catch(Exception ex) {
	        throw new RuntimeException(ex);
	    }
	}

    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("table.mustache");
    }
}
