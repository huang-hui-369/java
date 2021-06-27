package huang.river.file;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class Finder {

	public static final String PATTERN_GLOB = "glob:";
	
	public static final String PATTERN_REGEX = "regex:";
	
	public static final byte MODLE_FILE = 5;
	
	public static final byte MODLE_DIR = 6;
	
	public static final byte MODLE_FILE_DIR = 7;
	
	private PathMatcher matcher = null;
	
	private PathPattern pattern = PathPattern.GLOB;
	
	private PathProcessor processor = new OutPathProcesoer();
	
	private byte modle = MODLE_FILE;
	
	private FileVisitor<Path> vistor = new PathVistor();
	
	private Finder() {
	}
	
	public static Finder getDefallt() {
		return new Finder();
	}
	
	public void setPattern(PathPattern pattern) {
		this.pattern = pattern;
	}

	public void setProcessor(PathProcessor processor) {
		this.processor = processor;
	}

	public void setModle(byte modle) {
		this.modle = modle;
	}

	private boolean isMatch(Path filepath) {
		assertNotNull(filepath);
		return matcher.matches(filepath);
	}
	
	public void grep(String pathstr) throws IOException {
		grep(pathstr, "**");
	}
	
	public void grep(String pathstr, String fileName) throws IOException {
		if(pattern.getName().equals(PATTERN_GLOB)) {
			matcher = FileSystems.getDefault().getPathMatcher(PATTERN_GLOB + fileName);
		} else {
			matcher = FileSystems.getDefault().getPathMatcher(PATTERN_REGEX + fileName);
		}
		Files.walkFileTree(Paths.get(pathstr), vistor);
	}
	
	enum PathPattern {
		
		GLOB("glob:"),
		REGEX("regex:");
		
		private String name;
		// 构造方法  
	    private PathPattern(String name) {
	    	this.setName(name);
	    }
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		} 

		
	}
	
	class PathVistor extends SimpleFileVisitor<Path> {
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Objects.requireNonNull(file);
		    Objects.requireNonNull(attrs);
		    if(isMatch(file) && (modle&MODLE_FILE)==MODLE_FILE) {
		    	processor.preProcess(file);
		    }
			return FileVisitResult.CONTINUE;
			
		}
		
		@Override
	    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
	        throws IOException
	    {
	        Objects.requireNonNull(dir);
	        Objects.requireNonNull(attrs);
	        if((modle&MODLE_DIR)==MODLE_DIR) {
		    	processor.preProcess(dir);
		    }
	        return FileVisitResult.CONTINUE;
	    }
		
		 @Override
	    public FileVisitResult postVisitDirectory(Path dir, IOException exc)
	        throws IOException
	    {
	        Objects.requireNonNull(dir);
	        if (exc != null)
	            throw exc;
	        if((modle&MODLE_DIR)==MODLE_DIR) {
		    	processor.postProcess(dir);
		    }
	        return FileVisitResult.CONTINUE;
	    }


		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			System.err.println(exc);
			if (exc != null)
	            throw exc;
			return FileVisitResult.CONTINUE;
		}
	}
	
	
	public interface PathProcessor {
        void preProcess(Path p);
        void postProcess(Path p);
    }
	
	public static class DefaultPathProcessor implements PathProcessor {

		@Override
		public void preProcess(Path p) {
		}

		@Override
		public void postProcess(Path p) {
		}
		
	}
	
	public static class OutPathProcesoer extends DefaultPathProcessor {
		
		@Override
		public void preProcess(Path p) {
			System.out.println(p.toString());
		}
	}
	
}
