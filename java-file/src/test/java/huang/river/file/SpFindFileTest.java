package huang.river.file;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import huang.river.file.SpFindFile.File1FindResult;

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

}
