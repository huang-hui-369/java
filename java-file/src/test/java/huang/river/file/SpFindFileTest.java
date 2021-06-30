package huang.river.file;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import huang.river.file.SpFindFile.File1FindResult;
import huang.river.file.SpFindFile.MachedLineResult;

public class SpFindFileTest {

	@Test
	public void testFind() {
		List<String> regexList = new ArrayList<>();
		regexList.add("open\\s+(\\w+)");
		regexList.add("open\\s+(\\w+)");
		regexList.add("FETCH\\s+(\\w+)");
		
		String path = "D:\\project\\saitamaMigration-git\\saitama_sql_win\\PROCEDURE";
		
//		path = "D:\\project\\saitamaMigration-git\\saitama_sql_win\\PROCEDURE\\PACKAGE_BODY_CODE_MAPPING_LIBRARY.sql";
		
		SpFindFile finder = SpFindFile.getSimpleFileFinder(regexList);
		List<File1FindResult> resultList =  finder.find(path, 
					regexList, SrcCounter.SrcCounterType.sql, SpFiles.FileCharset.SHIFTJIS_WIN);
		finder.print(resultList, System.out);
	}
	
	@Test
	public void testOpenCusor() {
		List<String> regexList = new ArrayList<>();
		regexList.add("open\\s+(\\w+)");
		regexList.add("open\\s+(\\w+)");
		regexList.add("FETCH\\s+(\\w+)");
		
		String path = "D:\\project\\saitamaMigration-git\\saitama_sql_win\\PROCEDURE";
		
//		path = "D:\\project\\saitamaMigration-git\\saitama_sql_win\\PROCEDURE\\PACKAGE_BODY_CODE_MAPPING_LIBRARY.sql";
		
		SpFindFile finder = SpFindFile.getSimpleFileFinder(regexList);
		List<File1FindResult> resultList =  finder.find(path, 
					regexList, SrcCounter.SrcCounterType.sql, SpFiles.FileCharset.SHIFTJIS_WIN);
		List<File1FindResult> resultList2 =  new ArrayList<File1FindResult>();
		
		
		for(File1FindResult result : resultList) {
			File1FindResult fileRst = new File1FindResult();
			int groupCnt = 0;
			int count = result.getFindCount();
			for(int i=0; i<count; i++) {
				Collection<MachedLineResult> linerstList = result.getGroupMap().get(i);
				Object[] lineList =  linerstList.toArray();
				String opeCusor01Name = ((MachedLineResult)lineList[0]).getGroupList().get(1);
				String fetchCusorName = ((MachedLineResult)lineList[1]).getGroupList().get(1);
				if(opeCusor01Name.equals(fetchCusorName)) {
					fileRst.getGroupMap().putAll(groupCnt, linerstList);
					groupCnt++;
				}
			}
			fileRst.setFindCount(groupCnt);
			fileRst.setFilepath(result.getFilepath());
			if(groupCnt>0) {
				resultList2.add(fileRst);
			}
		}
		
		finder.print(resultList2, System.out);
	}

}
