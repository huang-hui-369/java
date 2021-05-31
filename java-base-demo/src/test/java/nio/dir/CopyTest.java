package nio.dir;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

import org.junit.Test;

import nio.dir.Copy.TreeCopier;

public class CopyTest {

	@Test
	public void testTreeCopy() {
		boolean recursive = true;
		boolean prompt = true;
		boolean preserve = true;

		Path baseDir = Paths.get(System.getProperty("user.dir"));
		Path source = baseDir.resolve("src\\test\\resources\\dir1");
		Path target = baseDir.resolve("src\\test\\resources\\dir3");

		EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		TreeCopier tc = new TreeCopier(source, target, prompt, preserve);
		try {
			Files.walkFileTree(source, opts, Integer.MAX_VALUE, tc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
