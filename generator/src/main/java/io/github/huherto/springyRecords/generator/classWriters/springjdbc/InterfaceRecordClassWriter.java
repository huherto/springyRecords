package io.github.huherto.springyRecords.generator.classWriters.springjdbc;

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

    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("irecord.mustache");
    }

    @Override
    public File sourceFile(DatabaseTool dbTool) {
        return sourceFile(
                getMainSourceDir(), 
                dbTool.getPackageNameForBaseTypes(), 
                "BaseRecord" );
    }

    @Override
    public boolean overwriteExistingFile() {
        return true;
    }

}
