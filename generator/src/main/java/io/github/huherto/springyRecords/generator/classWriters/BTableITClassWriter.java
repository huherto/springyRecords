package io.github.huherto.springyRecords.generator.classWriters;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.github.huherto.springyRecords.generator.tools.DatabaseTool;

public class BTableITClassWriter extends BaseClassWriter<DatabaseTool> {

    public BTableITClassWriter(Path baseDir) {
        super(baseDir);
    }

    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("btableit.mustache");
    }

    @Override
    public File sourceFile(DatabaseTool dbTool) {
        return
                sourceFile(getTestSourceDir(),
                        dbTool.baseDatabasePackageName(),
                        "BaseTableIT");
    }

    @Override
    public boolean overwriteExistingFile() {
        return false;
    }

}
