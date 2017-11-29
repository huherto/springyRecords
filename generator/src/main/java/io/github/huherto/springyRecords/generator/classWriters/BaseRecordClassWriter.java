package io.github.huherto.springyRecords.generator.classWriters;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.github.huherto.springyRecords.generator.tools.TableTool;

public class BaseRecordClassWriter extends BaseClassWriter<TableTool> {

	public BaseRecordClassWriter(Path baseDir) {
        super(baseDir);
    }

    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("baserecord.mustache");
    }

    @Override
    public File sourceFile(TableTool tableTool) {
        return
            sourceFile(getMainSourceDir(),
                    tableTool.baseRecordPackageName(),
                    tableTool.baseRecordClassName());
    }

    @Override
    public boolean overwriteExistingFile() {
        return true;
    }
}
