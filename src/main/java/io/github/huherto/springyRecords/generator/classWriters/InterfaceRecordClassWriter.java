package io.github.huherto.springyRecords.generator.classWriters;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.github.huherto.springyRecords.generator.tools.DatabaseTool;

public class InterfaceRecordClassWriter extends BaseClassWriter<DatabaseTool> {

    public InterfaceRecordClassWriter(Path baseDir) {
        super(baseDir);
    }

    @Override
    public void makeClass(DatabaseTool dbTool) {
        try {
            File sourceFile = sourceFile(getMainSourceDir(), dbTool.getPackageNameForBaseTypes(), "BaseRecord" );
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
        return mf.compile("irecord.mustache");
    }

}
