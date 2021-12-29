package lhn.nio;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import lhn.nio.FindFuncImpl;

public class FindFuncImplTest {

	@Test
	public void testProcess() {
		String pathstr = "D:\\project\\saitama\\調査";
		
//		String pathstr = "D:\\gutProjects\\saitamaMigration\\saitama_sql_win\\test";
		
//		pathstr = System.getProperty("user.dir");
		
		Path startingDir = Paths.get(pathstr);
		
		String pattern = "*.txt";
		FindFuncImpl finder = new FindFuncImpl();
		try {
			finder.process(pathstr, pattern);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
