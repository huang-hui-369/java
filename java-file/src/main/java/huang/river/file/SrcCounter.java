package huang.river.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import huang.river.string.RegexPattern;

/**
 * 
 * 利用DeepFinder递归查找符合的文件，然后对文件进行代码行数统计。
 * 可以追加SrcCounterParserI来实现新的种类的source文件。
 * 可以追加SrcCounterResultProcessor来处理统计的结果。
 * 
 * @author D2019-06
 *
 */
public class SrcCounter {
	
	// 有效sorce行数
	private int sourceCnt = 0;
	
	// 空行数
	private int emptyCnt = 0;
	
	// 注释行数
	private int commentCnt = 0;
	
	private Map<Integer, String> srcMap = new TreeMap<>();
	
	// 是否多行注释中
	private boolean isMutiCommentState = false;
	
	private int totalCnt = 0;
	
	private SrcCounterParserI srcCounterParser = null;
	
	private SrcCounterResultProcessor resultProcessor = null;
	
	private Path workpath;
	
	SrcCounter(SrcCounterParserI counter, SrcCounterResultProcessor resultProcessor) {
		this.srcCounterParser = counter;
		this.resultProcessor = resultProcessor;
	}
	
	/**
	 * @return　返回java source counter 
	 */
	public static SrcCounter getJavaCounter(SrcCounterResultProcessor... resultProcessor) {
		return getCounter(SrcCounterType.java, resultProcessor);
	}
	
	public static SrcCounter getSqlCounter(SrcCounterResultProcessor... resultProcessor) {
		return getCounter(SrcCounterType.sql, resultProcessor);
	}
	
	public static SrcCounter getCounter(SrcCounterType type, SrcCounterResultProcessor... resultProcessor) {
		SrcCounterParserI SrcParser = null;
		if(SrcCounterType.java.equals(type)) {
			SrcParser = new JavaSrcCounterParser();
		} else if(SrcCounterType.sql.equals(type)) {
			SrcParser = new SqlSrcCounterParser();
		}
		
		if(resultProcessor.length==1) {
			return new SrcCounter(SrcParser, resultProcessor[0]);
		}
		return new SrcCounter(SrcParser, new ConsoleOutSrcCounterResultProcessor());
	}
		
	
	/**
	 * 统计
	 * @param line
	 */
	protected void countLine(String line) {
		++totalCnt;
		if(srcCounterParser.isOneComment(line)) {
			commentCnt++;
		} else if(srcCounterParser.isStartMutiComment(line)) {
			isMutiCommentState = true;
			commentCnt++;
		} else if(isMutiCommentState && srcCounterParser.isEndMutiComment(line)) {
			isMutiCommentState = false;
			commentCnt++;
		} else if(isMutiCommentState) {
			commentCnt++;
		} else if( srcCounterParser.isEmptyLine(line) ) {
			emptyCnt++;
		} else {
			srcMap.put(totalCnt, line);
			sourceCnt++;
		}
		
	}
	

	/**
	 * 会用递归的方式遍历指定的目录下的文件进行统计，
	 * 会用指定的charset打开文件，如果遇到字符编码问题，不会报错会忽略错误的字符。
	 * @param pathstr	要统计的文件目录
	 * @param charset	打开文件的字符编码Charset
	 */
	public void count(String pathstr, Charset... charset ) {
		
		try {
			this.workpath = Paths.get(pathstr);
			DeepFinder finder = DeepFinder.getGlobFile();
			DeepFinder.ReadFileAllLinesProcessor prodessor = new DeepFinder.ReadFileAllLinesProcessor( 
				// 这是一个call back函数，
				p-> {
					// 文件处理，可以调用getLineList来获取整个文件的内容，获取后统计文件source行数，
					for(String line : p.getLineList()) {
						// 行处理
						countLine(line);
					}
					// 
					resultProcessor.processCount(workpath, p.getFilePath(), totalCnt, sourceCnt, commentCnt, emptyCnt, srcMap);
//					System.out.format("%s,%s\n", p.getAbsolutePath(), formatCountString());
	//				System.out.println(p.getLineList().size());
					clear();
				}, charset);
			finder.setPathProcessor(prodessor);
			finder.grep(pathstr, "**/" + srcCounterParser.getMatchFileName());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void clear() {
		this.commentCnt = 0;
		this.emptyCnt = 0;
		this.isMutiCommentState = false;
		this.sourceCnt = 0;
		this.totalCnt = 0;
	}
	
	public enum SrcCounterType {
		java,
		sql
	}
	
	public interface SrcCounterParserI {
		
		boolean isOneComment(String line);
		
		boolean isStartMutiComment(String line);
		
		boolean isEndMutiComment(String line);
		
		boolean isEmptyLine(String line);
		
		String getMatchFileName();

	}
	
	public static class EmptySrcCounterParser implements SrcCounterParserI {

		@Override
		public boolean isOneComment(String line) {
			return false;
		}

		@Override
		public boolean isStartMutiComment(String line) {
			return false;
		}

		@Override
		public boolean isEndMutiComment(String line) {
			return false;
		}

		@Override
		public boolean isEmptyLine(String line) {
			return RegexPattern.isEmptyLine(line);
		}
		
		@Override
		public String getMatchFileName() {
			return "";
		}

	}
	
	public static class JavaSrcCounterParser extends EmptySrcCounterParser {
		
		// 单行注释 //
		private final Pattern oneCommentP = Pattern.compile("^\\s*//+.*");
		private final Pattern oneCommentP2 = Pattern.compile("^\\s*/\\*+.*\\*/+\\s*$");
		// 多行注释开始 /*
		private final Pattern startMutiCommentP = Pattern.compile("^\\s*/\\*+.*");
		// 多行注释结束 */
		private final Pattern endMutiCommentP =  Pattern.compile(".*\\*/+\\s*$");
		
		@Override
		public boolean isOneComment(String line) {
			return (oneCommentP.matcher(line).matches() 
					|| oneCommentP2.matcher(line).matches());
		}

		@Override
		public boolean isStartMutiComment(String line) {
			return startMutiCommentP.matcher(line).matches();
		}

		@Override
		public boolean isEndMutiComment(String line) {
			return endMutiCommentP.matcher(line).matches();
		}
		
		@Override
		public String getMatchFileName() {
			return "*.java";
		}
		
	}
	
public static class SqlSrcCounterParser extends EmptySrcCounterParser {
		
		// 单行注释 //
		private final Pattern oneCommentP = Pattern.compile("^\\s*--+.*");
		private final Pattern oneCommentP2 = Pattern.compile("^\\s*/\\*+.*\\*/+\\s*$");
		// 多行注释开始 /*
		private final Pattern startMutiCommentP = Pattern.compile("^\\s*/\\*+.*");
		// 多行注释结束 */
		private final Pattern endMutiCommentP =  Pattern.compile(".*\\*/+\\s*$");
		
		@Override
		public boolean isOneComment(String line) {
			return (oneCommentP.matcher(line).matches() 
					|| oneCommentP2.matcher(line).matches());
		}

		@Override
		public boolean isStartMutiComment(String line) {
			return startMutiCommentP.matcher(line).matches();
		}

		@Override
		public boolean isEndMutiComment(String line) {
			return endMutiCommentP.matcher(line).matches();
		}
		
		@Override
		public String getMatchFileName() {
			return "*.sql";
		}
		
	}
	
	public interface SrcCounterResultProcessor {
		void processCount(Path workpath, Path filepath, int totalCnt, int srcCnt, int commentCnt, int emptyCnt, Map<Integer, String> srcMap);
	}
	
	public static class ConsoleOutSrcCounterResultProcessor implements SrcCounterResultProcessor {
		@Override
		public void processCount(Path workpath, Path filepath, int totalCnt, int srcCnt, int commentCnt, int emptyCnt,  Map<Integer, String> srcMap) {
			System.out.println(String.format("File:%s, total line:%d, source line:%d, comment line:%d, empty line:%d", 
					workpath.relativize(filepath).toString(), totalCnt, srcCnt, commentCnt, emptyCnt));
		}
		
	}
	

}
