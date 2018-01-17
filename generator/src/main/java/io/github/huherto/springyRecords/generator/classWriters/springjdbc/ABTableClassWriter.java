package io.github.huherto.springyRecords.generator.classWriters.springjdbc;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.github.huherto.springyRecords.generator.tools.DatabaseTool;

public class ABTableClassWriter extends BaseClassWriter<DatabaseTool> {

	public ABTableClassWriter(Path baseDir) {
        super(baseDir);
    }

    @Override
    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("abtable.mustache");
    }

    @Override
    public File sourceFile(DatabaseTool dbTool) {
        return sourceFile(getMainSourceDir(),
                dbTool.getPackageNameForBaseTypes(),
                "AbstractBaseTable");
    }

    @Override
    public boolean overwriteExistingFile() {
        return true;
    }

}
