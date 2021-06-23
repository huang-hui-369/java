package huang.river.nio;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import huang.river.nio.PrintDir;

public class PrintDirTest {

	@Test
	public void testPrintDir() {
		PrintDir pd = new PrintDir();
		try {
			Files.walkFileTree(Paths.get(System.getProperty("user.dir")), pd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPrintDir2() {
		Path dir = Paths.get(System.getProperty("user.dir"));
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for (Path file : stream) {
				System.out.println(file.getFileName());
			}
		} catch (IOException | DirectoryIteratorException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can only be thrown by newDirectoryStream.
			System.err.println(x);
		}
	}

	@Test
	public void testPrintDirFilter() {
		Path dir = Paths.get(System.getProperty("user.dir"), "src/main/java/nio/dir");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{java,class,jar}")) {
			for (Path entry : stream) {
				System.out.println(entry.getFileName());
			}
		} catch (IOException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can // only be thrown by newDirectoryStream.
			System.err.println(x);
		}
	}

	@Test
	public void testPrintDirMyFilter() {
		
		DirectoryStream.Filter<Path> myFilter = new DirectoryStream.Filter<Path>() {

			@Override
			public boolean accept(Path file) throws IOException {
				return (Files.isDirectory(file));
			}
		};
		
		Path dir = Paths.get(System.getProperty("user.dir"));
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, myFilter)) {
			for (Path entry : stream) {
				System.out.println(entry.getFileName());
			}
		} catch (IOException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can // only be thrown by newDirectoryStream.
			System.err.println(x);
		}
		
	}

}
