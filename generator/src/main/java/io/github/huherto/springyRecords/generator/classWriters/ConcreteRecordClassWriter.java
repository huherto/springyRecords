package io.github.huherto.springyRecords.generator.classWriters;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.github.huherto.springyRecords.generator.tools.TableTool;

public class ConcreteRecordClassWriter extends BaseClassWriter<TableTool> {

	public ConcreteRecordClassWriter(Path baseDir) {
        super(baseDir);
    }

    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("record.mustache");
    }

    @Override
    public File sourceFile(TableTool tableTool) {
        return
                sourceFile(
                        getMainSourceDir(),
                        tableTool.concreteRecord());
    }

    @Override
    public boolean overwriteExistingFile() {
        return false;
    }


}
