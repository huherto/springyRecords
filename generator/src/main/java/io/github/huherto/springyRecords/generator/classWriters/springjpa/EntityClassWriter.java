package io.github.huherto.springyRecords.generator.classWriters.springjpa;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;

import com.github.mustachejava.Mustache;

import io.github.huherto.springyRecords.generator.classWriters.springjdbc.BaseClassWriter;
import io.github.huherto.springyRecords.generator.tools.BaseClassTool;
import io.github.huherto.springyRecords.generator.tools.ClassTool;
import io.github.huherto.springyRecords.generator.tools.Clazz;
import io.github.huherto.springyRecords.generator.tools.ImportSet;
import io.github.huherto.springyRecords.generator.tools.TableTool;
import io.github.huherto.springyRecords.generator.tools.TableToolImpl;

public class EntityClassWriter extends BaseClassWriter<TableTool> {

    private final class MyClassTool extends BaseClassTool {
        
        private MyClassTool(Clazz clazz, TableTool tool) {
            super(clazz, tool);
        }

        @Override
        public Iterator<String> getImports() {

            ImportSet importSet = new ImportSet(this.getClazz());

            importSet.addImports(importsForColumns(tableTool.getColumns()));
            importSet.addImport("javax.persistence","Entity");

            return importSet.iterator();
        }
    }

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
        return true;
    }

    public Mustache createTemplate() {
        return getMustacheFactory().compile("centity.mustache");
    }

    @Override
    public ClassTool getClassTool(TableTool tool) {
        return new MyClassTool(tool.concreteEntity(), tool);
    }
    
}
