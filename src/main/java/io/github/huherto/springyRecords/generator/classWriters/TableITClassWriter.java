package io.github.huherto.springyRecords.generator.classWriters;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.github.huherto.springyRecords.generator.tools.TableTool;

public class TableITClassWriter extends BaseClassWriter<TableTool> {

    public TableITClassWriter(Path baseDir) {
        super(baseDir);
    }

    @Override
    public void makeClass(TableTool tableTool) {
        try {
            File sourceFile =
                sourceFile(getTestSourceDir(),
                        tableTool.concreteTablePackageName(),
                        tableTool.concreteTableClassName() + "IT");
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
        return mf.compile("tableit.mustache");
    }

}
