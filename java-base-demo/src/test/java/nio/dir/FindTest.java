package nio.dir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import nio.dir.Find.Finder;

public class FindTest {

	@Test
	public void test() {
		Path startingDir = Paths.get(System.getProperty("user.dir"));
		String pattern = "*.java";
		Finder finder = new Finder(pattern);
		try {
			Files.walkFileTree(startingDir, finder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finder.done();
	}

}
