package io.github.huherto.springyRecords.generator.classWriters;

import java.nio.file.Path;

import io.github.huherto.springyRecords.generator.tools.DatabaseTool;

public interface ClassWriterRegistry {

    void writeAllClasses(DatabaseTool dbTool, Path baseDir);

}