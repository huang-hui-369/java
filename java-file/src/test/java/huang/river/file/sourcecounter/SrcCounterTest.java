package huang.river.file.sourcecounter;

import org.junit.Test;

import huang.river.file.SpFiles;

public class SrcCounterTest {

	@Test
	public void testCount() {
		
		SrcCounter.getJavaCounter().count(".", SpFiles.FileCharset.UTF8); 
//		System.out.println(Pattern.compile("^\\s*/\\*+.*\\*/+\\s*$").matcher("   /***** aaaa ****/").matches());
	}

}
