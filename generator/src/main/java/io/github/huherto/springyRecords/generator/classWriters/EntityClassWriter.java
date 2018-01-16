package io.github.huherto.springyRecords.generator.classWriters;

import java.io.File;
import java.nio.file.Path;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.github.huherto.springyRecords.generator.tools.TableTool;

public class EntityClassWriter extends BaseClassWriter<TableTool> {

    public EntityClassWriter(Path baseDir) {
        super(baseDir);
    }

    @Override
    public File sourceFile(TableTool tableTool) {
        return
                sourceFile(
                        getMainSourceDir(),
                        tableTool.concreteEntity());
    }

    @Override
    public boolean overwriteExistingFile() {
        return false;
    }

    public Mustache createTemplate() {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile("centity.mustache");
    }
}
