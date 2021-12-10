package huang.river.nio;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class FindOpenCusorTest {

	@Test
	public void test(){
		
		String pathstr = "D:\\project\\saitama\\調査";
		
//		String pathstr = "D:\\gutProjects\\saitamaMigration\\saitama_sql_win\\test";
		
//		pathstr = System.getProperty("user.dir");
		
		Path startingDir = Paths.get(pathstr);
		
		
		
		String pattern = "*.sql";
		FindCreateFunc finder = new FindCreateFunc();
		try {
			finder.process(pathstr, pattern);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
