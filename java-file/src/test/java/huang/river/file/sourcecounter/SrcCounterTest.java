package huang.river.file.sourcecounter;

import org.junit.Test;

import huang.river.file.SpFiles;
import huang.river.file.SrcCounter;

public class SrcCounterTest {

	@Test
	public void testCount() {
		
		SrcCounter.getJavaCounter(null).count("."); 
//		System.out.println(Pattern.compile("^\\s*/\\*+.*\\*/+\\s*$").matcher("   /***** aaaa ****/").matches());
	}
	
	@Test
	public void testCountSql() {
//		SrcCounter.getSqlCounter(SpFiles.FileCharset.SHIFTJIS_WIN).count(
//				"D:\\project\\saitamaMigration-git\\saitama_sql_win\\PROCEDURE\\PACKAGE_BODY_CODE_MAPPING_LIBRARY.sql");
		SrcCounter.getSqlCounter(SpFiles.FileCharset.SHIFTJIS_WIN).count(
				"D:\\project\\saitamaMigration-git\\saitama_sql_win\\PROCEDURE");
		
	}
	
	
	


}
