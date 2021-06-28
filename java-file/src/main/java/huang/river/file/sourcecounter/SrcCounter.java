package huang.river.file.sourcecounter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import huang.river.file.DeepFinder;
import huang.river.string.RegexPattern;

public class SrcCounter {
	
	// 有效sorce行数
	private int validLineCnt = 0;
	
	// 空行数
	private int emptyLineCnt = 0;
	
	// 注释行数
	private int commentLineCnt = 0;
	
	// 是否多行注释中
	private boolean isMutiCommentState = false;
	
	private int allLineCnt = 0;
	
	private SrcCounterI srcCounter = null;
	
	SrcCounter(SrcCounterI counter) {
		this.srcCounter = counter;
	}
	
	public 	static SrcCounter getJavaCounter() {
		return new SrcCounter(new JavaSrcCounter());
	}
	
	protected void countLine(String line) {
		++allLineCnt;
		if(srcCounter.isOneComment(line)) {
			commentLineCnt++;
		} else if(srcCounter.isStartMutiComment(line)) {
			isMutiCommentState = true;
			commentLineCnt++;
		} else if(isMutiCommentState && srcCounter.isEndMutiComment(line)) {
			isMutiCommentState = false;
			commentLineCnt++;
		} else if(isMutiCommentState) {
			commentLineCnt++;
		} else if( srcCounter.isEmptyLine(line) ) {
			emptyLineCnt++;
		} else {
			validLineCnt++;
		}
		
	}
	

	public void count(String pathstr, Charset charset ) {
		
		try {
			DeepFinder finder = DeepFinder.getGlobFile();
			DeepFinder.ReadFileAllLinesProcessor prodessor = new DeepFinder.ReadFileAllLinesProcessor(charset, p-> {
				for(String line : p.getLineList()) {
					countLine(line);
				}
				System.out.format("%s,%s\n", p.getAbsolutePath(), formatCountString());
//				System.out.println(p.getLineList().size());
				clear();
			});
			finder.setPathProcessor(prodessor);
			finder.grep(pathstr, "**/" + srcCounter.getMatchFileName());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void clear() {
		this.commentLineCnt = 0;
		this.emptyLineCnt = 0;
		this.isMutiCommentState = false;
		this.validLineCnt = 0;
		this.allLineCnt = 0;
	}
	
	private String formatCountString() {
		return String.format("total line:%d, source line:%d, comment line:%d, empty line;%d", 
				allLineCnt, validLineCnt, commentLineCnt, emptyLineCnt);
	}
	
	public interface SrcCounterI {
		
		boolean isOneComment(String line);
		
		boolean isStartMutiComment(String line);
		
		boolean isEndMutiComment(String line);
		
		boolean isEmptyLine(String line);
		
		String getMatchFileName();

	}
	
	public static class EmptySrcCounter implements SrcCounterI {

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
	
	public static class JavaSrcCounter extends EmptySrcCounter {
		
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


}
