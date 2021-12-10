package huang.river.nio;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class FindCallFuncTest {

	@Test
	public void testProcess() {
String pathstr = "D:\\project\\saitama\\db\\scripts\\PROCEDURE_ALTER";
		
		Path startingDir = Paths.get(pathstr);
		
		String pattern = "*.sql";
		FindFuncImpl finder = new FindFuncImpl();
		try {
			finder.process(pathstr, pattern);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
