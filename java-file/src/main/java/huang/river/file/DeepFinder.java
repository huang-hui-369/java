package huang.river.file;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 
 * 深度查找给定目录下的符合指定文件名模式的所有文件。
 * 默认为glob模式，查找文件模式，打印匹配的文件名。
 * <p> 1. 可以指定查找模式为 glob或regex
 *     2. 可以指定文件模式为文件，或者目录，或者文件和目录
 *     3. 可以指定PathProcessor 默认为打印path名。
 * 
 * glob模式
 * 	**\*.txt		匹配所有目录下的扩展名为.txt的文件
 * 	*.txt			匹配所有扩展名为.txt的文件
 * 	*.{html,htm}	匹配所有扩展名为.html或.htm的文件。{ }用于组模式，它使用逗号分隔
 * 	?.txt			匹配任何单个字符做文件名且扩展名为.txt的文件
 * 	.				匹配所有含扩展名的文件
 * 	C:\Users\*		匹配所有在C盘Users目录下的文件。反斜线“\”用于对紧跟的字符进行转义
 * 	/home/**		UNIX平台上匹配所有/home目录及子目录下的文件。**用于匹配当前目录及其所有子目录
 * 	[xyz].txt		匹配所有单个字符作为文件名，且单个字符只含“x”或“y”或“z”三种之一，且扩展名为.txt的文件。方括号[]用于指定一个集合
 * 	[a-c].txt		匹配所有单个字符作为文件名，且单个字符只含“a”或“b”或“c”三种之一，且扩展名为.txt的文件。减号“-”用于指定一个范围，且只能用在方括号[]内
 * 	[!a].txt		匹配所有单个字符作为文件名，且单个字符不能包含字母“a”，且扩展名为.txt的文件。叹号“!”用于否定
 * 
 * 文件模式
 *    文件: 			只匹配符合条件的文件名，忽略目录
 *    目录: 			只匹配符合条件的目录名，忽略文件
 *    文件和目录: 	匹配符合条件的文件名，和所有的目录名（忽略匹配条件）
 * 
 * @author huang
 * 
 */
public class DeepFinder {

	private static final byte MODLE_FILE = FILE_MODLE.FILE.getValue();
	private static final byte MODLE_DIR = FILE_MODLE.DIR.getValue();
	private static final byte MODLE_FILE_DIR = FILE_MODLE.FILE_DIR.getValue();
	
	private PathMatcher pathMatcher = null;
	
	private PathPattern pathPattern = PathPattern.GLOB;
	
	private PathProcessor pathProcessor = new FilePrinter();
	
	private byte filemodle = MODLE_FILE;
	
	private FileVisitor<Path> vistor = new PathVistor();
	
	private DeepFinder() {
	}
	
	/************************  build 方法 ************************************/
	
	public static DeepFinder getAllFileDir(PathProcessor... processor) {
		if(processor.length==1) {
			return build(processor[0], FILE_MODLE.FILE_DIR, null);
		}
		return build(null, FILE_MODLE.FILE_DIR, null);
	}
	
	public static DeepFinder getAllDir(PathProcessor... processor) {
		if(processor.length==1) {
			return build(processor[0], FILE_MODLE.FILE_DIR, null);
		}
		return build(null, FILE_MODLE.DIR, null);
	}
	
	public static DeepFinder getAllFile(PathProcessor... processor) {
		if(processor.length==1) {
			return build(processor[0], FILE_MODLE.FILE_DIR, null);
		}
		return build(null, FILE_MODLE.FILE, null);
	}
	
	public static DeepFinder getGlobFile(PathProcessor... processor) {
		if(processor.length==1) {
			return build(processor[0], FILE_MODLE.FILE_DIR, null);
		}
		return build(null, FILE_MODLE.FILE, null);
	}
	
	public static DeepFinder getRegexFile(PathProcessor... processor) {
		if(processor.length==1) {
			return build(processor[0], FILE_MODLE.FILE_DIR, null);
		}
		return build(null, FILE_MODLE.FILE, PathPattern.REGEX);
	}
	
	public static DeepFinder getRegexDir(PathProcessor... processor) {
		if(processor.length==1) {
			return build(processor[0], FILE_MODLE.FILE_DIR, null);
		}
		return build(null, FILE_MODLE.DIR, PathPattern.REGEX);
	}
	
	public DeepFinder setPathPattern(PathPattern pathPattern) {
		this.pathPattern = pathPattern;
		return this;
	}

	public DeepFinder setPathProcessor(PathProcessor pathProcessor) {
		this.pathProcessor = pathProcessor;
		return this;
	}

	public DeepFinder setFilemodle(byte filemodle) {
		this.filemodle = filemodle;
		return this;
	}

	public static DeepFinder build(PathProcessor processor, FILE_MODLE filemodle, PathPattern pattern) {
		DeepFinder o = new DeepFinder();
		if(processor!=null) {
			o.pathProcessor = processor;
		}
		if(filemodle!=null) {
			o.filemodle = filemodle.getValue();
		}
		if(pattern !=null) {
			o.pathPattern = pattern; 
		}
		return o;
	}

	private boolean isMatch(Path filepath) {
		assertNotNull(filepath);
		return pathMatcher.matches(filepath);
	}
	
	/************************  grep 方法 ************************************/
	
	/**
	 * 查找指定pathstr目录下的所有文件
	 * @param pathstr 文件目录（d:\）
	 * @throws IOException
	 */
	public void grep(String pathstr) throws IOException {
		grep(pathstr, "**");
	}
	
	/**
	 * 查找指定pathstr目录下以glob模式匹配指定fileName的文件
	 * @param pathstr  文件目录（d:\）
	 * @param fileName 文件名（**\*.java）
	 * @throws IOException
	 */
	public void grep(String pathstr, String fileName) throws IOException {
		if(pathPattern.equals(PathPattern.GLOB)) {
			pathMatcher = FileSystems.getDefault().getPathMatcher(PathPattern.GLOB.getName() + fileName);
		} else {
			pathMatcher = FileSystems.getDefault().getPathMatcher(PathPattern.REGEX.getName() + fileName);
		}
		Files.walkFileTree(Paths.get(pathstr), vistor);
	}
	
	/************************  enum ************************************/
	
	enum PathPattern {
		
		GLOB("glob:"),
		REGEX("regex:");
		
		private String name;
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
	
	enum FILE_MODLE {
		
		FILE((byte) 5),
		DIR((byte) 6),
		FILE_DIR((byte) 7);
		
		private byte value = 0;
		
		FILE_MODLE(byte value) {
			this.setValue(value);
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
		
	}
	
	/************************  PathVistor ************************************/
	
	class PathVistor extends SimpleFileVisitor<Path> {
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Objects.requireNonNull(file);
		    Objects.requireNonNull(attrs);
		    if(isMatch(file) && (filemodle&MODLE_FILE)==MODLE_FILE) {
		    	pathProcessor.processFile(file);
		    }
			return FileVisitResult.CONTINUE;
			
		}
		
		@Override
	    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
	        throws IOException
	    {
	        Objects.requireNonNull(dir);
	        Objects.requireNonNull(attrs);
	        if(filemodle==MODLE_DIR && isMatch(dir)) {
		    	pathProcessor.preProcessDir(dir);
		    } else if( filemodle==MODLE_FILE_DIR) {
		    	pathProcessor.preProcessDir(dir);
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
	        if(filemodle==MODLE_DIR && isMatch(dir)) {
		    	pathProcessor.postProcessDir(dir);
		    } else if( filemodle==MODLE_FILE_DIR) {
		    	pathProcessor.postProcessDir(dir);
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
	
	/************************  PathProcessor ************************************/
	public interface PathProcessor {
		void processFile(Path p) throws IOException ;
        void preProcessDir(Path p) throws IOException ;
        void postProcessDir(Path p) throws IOException ;
    }
	
	public static class DefaultPathProcessor implements PathProcessor {

		@Override
		public void processFile(Path p) throws IOException {
		}

		@Override
		public void preProcessDir(Path p) throws IOException  {
		}
		
		@Override
		public void postProcessDir(Path p) throws IOException  {
		}
		
	}
	
	public static class FilePrinter extends DefaultPathProcessor {
		
		@Override
		public void processFile(Path p) {
			System.out.format("file:%s\n", p.toString());
		}
		
		@Override
		public void preProcessDir(Path p) {
			System.out.format("dir:%s\n", p.toString());
		}
	}
	
	public static class ReadFileAllLinesProcessor extends DefaultPathProcessor {
		
		Path filepath = null;
		
		List<String> lines = null;
		
		Charset[] charset = null;
		
		public ReadFileAllLinesProcessor(Charset... charset) {
			this.charset = charset;
		}
		
		@Override
		public void processFile(Path filepath) throws IOException {
			this.filepath = filepath;
			lines = SpFiles.readFileIgnoreErr(filepath, charset);
		}
		
		public List<String> getLineList() {
			return lines;
		}
		
		public Path getFilePath() {
			return filepath;
		}
		
		public String getAbsolutePath() {
			return filepath.toAbsolutePath().toString();
		}
 	
	}
	
	
}
