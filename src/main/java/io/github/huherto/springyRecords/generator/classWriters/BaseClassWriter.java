package io.github.huherto.springyRecords.generator.classWriters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

import org.apache.log4j.Logger;

import com.github.mustachejava.Mustache;
import com.google.common.io.Files;

public abstract class BaseClassWriter<T> implements ClassWriter<T> {

	protected static final Logger logger = Logger.getLogger(BaseClassWriter.class);

	private Path baseDir;

    public BaseClassWriter(Path baseDir) {
        this.baseDir = baseDir;
    }

    public Path getMainSourceDir() {
        return baseDir.resolve("src").resolve("main").resolve("java");
    }

    public Path getTestSourceDir() {
        return baseDir.resolve("src").resolve("test").resolve("java");
    }

    public File sourceFile(Path sourceDir, String packageName, String className) throws IOException {

        String[] segments = packageName.split("\\.");

        File dir = sourceDir.toFile();
        for(String seg : segments) {
            dir = new File(dir, seg);
        }

        File sourceFile = new File(dir, className + ".java");
        Files.createParentDirs(sourceFile);

        return sourceFile;
    }

	public void writeCode(File sourceFile, Mustache template, T tool) {
	    try {
	        logger.info("Creating source "+sourceFile);
	        Writer writer = new FileWriter(sourceFile);
	        template.execute(writer, tool);
	        writer.flush();
	    }
	    catch(Exception ex) {
	        throw new RuntimeException(ex);
	    }
	}

}
