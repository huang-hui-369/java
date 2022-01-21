package lhn.file;


//import static org.junit.Assert.assertNotEquals;
//import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;


public class SpFindFile extends SrcCounter{
	
	File1Finder finder = null;
	
	
	
	public void setFinder(File1Finder finder) {
		this.finder = finder;
	}


	public static SpFindFile getSimpleFileFinder(List<String> regexList) {
		SpFindFile s = new SpFindFile();
		s.setFinder(new SpFindRexpRule(regexList));
		return s;
	}
	
	/**
	 *	查找一个文件
	 */
	@Override
	public void processFile(Path filepath) throws IOException {
		super.processFile(filepath);
		// 根据获取到的有效的行List来查找
		List<File1FindResult> resultList = new ArrayList<>();
		File1FindResult result = finder.find1file(filepath, this.srcMap);
		if(result.getFindCount()>0) {
			resultList.add(result);
		}
	}
	
	
	
	public List<File1FindResult> find(String pathstr, List<String> regexList, SrcCounter.SrcCounterType type, Charset charset) {
		
		List<File1FindResult> resultList = new ArrayList<>();
		
		SrcCounter.SrcCounterResultProcessor srcProcessor = new SrcCounter.SrcCounterResultProcessor() {

			@Override
			public void processCount(Path workpath, Path filepath, int totalCnt, int srcCnt, int commentCnt,
					int emptyCnt, Map<Integer, String> srcMap) {
				// 根据获取到的有效的行List来查找
				File1FindResult result = finder.find1file(filepath, srcMap);
				if(result.getFindCount()>0) {
					resultList.add(result);
				}
			}
			
		};
		
		SrcCounter.getCounter(charset, type, srcProcessor).count(pathstr);
		
		return resultList;
		
	}
	
	protected void print(List<File1FindResult> resultList, PrintStream out) {
		
		int taotalFoundCount = sumFoundCount(resultList);
		
		int count = 0;
		out.format("--------  total found %d  ---------------\n", taotalFoundCount);
		
		for(File1FindResult result : resultList) {
			count = result.getFindCount();
			out.format("--------  file:%s  found %d  ---------------\n", result.filepath.toString(), count);
			for(int i=0; i<count; i++) {
				out.format("### found %d ###\n", i+1);
				Collection<MachedLineResult> linerstList = result.getGroupMap().get(i);
				for(MachedLineResult rst : linerstList) {
					out.format("%d:,%s, %d\n", rst.getLineNo(), rst.getGroupList().toString(), rst.groupCount);
				}
				
			}
		}
	}
	
	protected int sumFoundCount(List<File1FindResult> resultList) {
		int total = 0;
		for(File1FindResult result : resultList) {
			total += result.getFindCount();
		}
		
		return total;
		
	}
	
	
	public interface File1Finder {
		File1FindResult find1file(Path filepath, Map<Integer, String> srcMap);
	}
	
	public static class SpFindRexpRule implements File1Finder {

		private List<Pattern> patternList = new ArrayList<>();
		
		private int curPatternIdx = 0;
		
		private final int patternSize;
		
		private int matchCnt = 0;
		
		SpFindRexpRule(List<String> regexList) {
//			assertNotNull("regex list is null.", regexList);
//			assertNotEquals("regex list size is 0." ,0, regexList.size());
			if(regexList!=null&&regexList.size()>0) {
				for(String regex: regexList ) {
					patternList.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
				}
			}
			patternSize = regexList.size();
		}
		
		@Override
		public File1FindResult find1file(Path filepath, Map<Integer, String> srcMap) {
			
			File1FindResult result = new File1FindResult();
			matchCnt = 0;
			clear();
			
			result.setFilepath(filepath);
			
//			System.out.println("find in file:" + filepath.toString());
			
			srcMap.forEach((Integer lineNo, String linestr)->{
				MachedLineResult lineRst = isLineMatched(lineNo, linestr);
				if( lineRst !=null) {
					result.getGroupMap().put(matchCnt, lineRst);
					if(isFind()) {
						matchCnt++;
						clear();
					}
				}
			});
			result.setFindCount(matchCnt);
			return result;
		}
		
		private MachedLineResult isLineMatched(int lineNo, String str) {
			Matcher m = patternList.get(curPatternIdx).matcher(str);
			if( m.find() ) {
				curPatternIdx++;
				List<String> groupList = new ArrayList<String>();
				for(int i=0; i<=m.groupCount();i++) {
					groupList.add(m.group(i));
				}
				MachedLineResult r = new MachedLineResult(lineNo, str, m.groupCount(), groupList);
				return r;
			}
			return null;
		}
		
		private boolean isFind() {
			return  curPatternIdx == patternSize;
		}
		
		private void clear() {
			curPatternIdx = 0;
//			matchCnt = 0;
		}
		
		
	}
	
	public static class File1FindResult {
		
		private Path filepath = null;
		private int findCount = 0;
		Multimap<Integer,MachedLineResult> groupMap = ArrayListMultimap.create();
		
		public Path getFilepath() {
			return filepath;
		}
		public void setFilepath(Path filepath) {
			this.filepath = filepath;
		}
		public int getFindCount() {
			return findCount;
		}
		public void setFindCount(int findCount) {
			this.findCount = findCount;
		}
		public Multimap<Integer, MachedLineResult> getGroupMap() {
			return groupMap;
		}
		public void setGroupMap(Multimap<Integer, MachedLineResult> groupMap) {
			this.groupMap = groupMap;
		}
		
		
		
		
	}
	
	public static class MachedLineResult {
		
		private int lineNo = 0;
		private String lineStr = null;
		private int groupCount = 0;
		private List<String> groupList = null;
		
		
		
		public MachedLineResult(int lineNo, String lineStr, int groupCount, List<String> groupList) {
			super();
			this.lineNo = lineNo;
			this.lineStr = lineStr;
			this.groupCount = groupCount;
			this.groupList = groupList;
		}
		
		
		public int getLineNo() {
			return lineNo;
		}
		public String getLineStr() {
			return lineStr;
		}
		public int getGroupCount() {
			return groupCount;
		}
		public List<String> getGroupList() {
			return groupList;
		}
		
		
		
	}

}
