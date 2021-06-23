package huang.river.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import huang.river.nio.Find;

public class FindTest {

	@Test
	public void test(){
		
		String pathstr = "D:\\project\\saitamaMigration-git\\saitama_sql_win\\PROCEDURE";
		
//		String pathstr = "D:\\gutProjects\\saitamaMigration\\saitama_sql_win\\test";
		
//		pathstr = System.getProperty("user.dir");
		
		Path startingDir = Paths.get(pathstr);
		
		
		
		String pattern = "*.sql";
		Find finder = new Find();
		try {
			finder.process(pathstr, pattern);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
