package huang.river.file.sourcecounter;

import org.junit.Test;

import huang.river.file.SpFiles;
import huang.river.file.SrcCounter;

public class SrcCounterTest {

	@Test
	public void testCount() {
		
		SrcCounter.getJavaCounter().count(".", SpFiles.FileCharset.UTF8); 
//		System.out.println(Pattern.compile("^\\s*/\\*+.*\\*/+\\s*$").matcher("   /***** aaaa ****/").matches());
	}
	
	@Test
	public void testCount2() {
		
		SrcCounter.getJavaCounter().count("."); 
//		System.out.println(Pattern.compile("^\\s*/\\*+.*\\*/+\\s*$").matcher("   /***** aaaa ****/").matches());
	}
	
	@Test
	public void testCountSql() {
		
//		SrcCounter.getSqlCounter().count("D:\\project\\saitamaMigration-git\\saitama_sql_win\\PROCEDURE", 
//				SpFiles.FileCharset.SHIFTJIS_WIN);
		
		SrcCounter.getSqlCounter().count("D:\\project\\saitamaMigration-git\\saitama_sql_win\\PROCEDURE\\PACKAGE_BODY_CODE_MAPPING_LIBRARY.sql", 
				SpFiles.FileCharset.SHIFTJIS_WIN);
		
	}
	
	
	


}
