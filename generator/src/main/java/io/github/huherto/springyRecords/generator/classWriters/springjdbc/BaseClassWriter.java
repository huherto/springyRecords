package io.github.huherto.springyRecords.generator.classWriters.springjdbc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.io.Files;

import io.github.huherto.springyRecords.generator.tools.ClassTool;
import io.github.huherto.springyRecords.generator.tools.Clazz;

public abstract class BaseClassWriter<T> implements ClassWriter<T> {
    
    protected static final Log logger = LogFactory.getLog(BaseClassWriter.class);

	private Path baseDir;
	
	private MustacheFactory mustacheFactory;
	
    public BaseClassWriter(Path baseDir) {
        this.baseDir = baseDir;
        this.mustacheFactory = new DefaultMustacheFactory();
    }
    
    public MustacheFactory getMustacheFactory() {
        return mustacheFactory;
    }

    public void setMustacheFactory(MustacheFactory mustacheFactory) {
        this.mustacheFactory = mustacheFactory;
    }

    public Path getMainSourceDir() {
        return baseDir.resolve("src").resolve("main").resolve("java");
    }

    public Path getTestSourceDir() {
        return baseDir.resolve("src").resolve("test").resolve("java");
    }

    @Override
    public void makeClass(T tool) {
        try {
            File sourceFile = sourceFile(tool);
            if (overwriteExistingFile()) {
                if (sourceFile.exists()) {
                    sourceFile.delete();
                    sourceFile.createNewFile();
                }
            }
            else {            
                if (sourceFile.exists()) {
                    logger.info("Skipping source "+sourceFile);
                    return;                    
                }
            }
            Object scopes[] = { tool,  this.getClassTool(tool) };
            writeCode(sourceFile, createTemplate(), scopes);
            
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
        
    public File sourceFile(Path sourceDir, Clazz clazz) {
        return sourceFile(sourceDir, clazz.getPackageName(), clazz.getClassName());
        
    }
    
    public File sourceFile(Path sourceDir, String packageName, String className) {

        try {
            String[] segments = packageName.split("\\.");
    
            File dir = sourceDir.toFile();
            for(String seg : segments) {
                dir = new File(dir, seg);
            }
    
            File sourceFile = new File(dir, className + ".java");
            Files.createParentDirs(sourceFile);
    
            return sourceFile;
        }
        catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

	public void writeCode(File sourceFile, Mustache template, Object[] scopes) {
	    try {
	        logger.info("Creating source "+sourceFile);
	        Writer writer = new FileWriter(sourceFile);
	        template.execute(writer, scopes);
	        writer.flush();
	    }
	    catch(Exception ex) {
	        throw new RuntimeException(ex);
	    }
	}
	
    public ClassTool getClassTool(T tool) {
        return null;
    }

    public abstract File sourceFile(T tool);
	
	public abstract boolean overwriteExistingFile();
	
	public abstract Mustache createTemplate();

}
