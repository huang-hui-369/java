package lhn.nio;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListFile {

	List<Path> result = new ArrayList<>();

	public List<Path> list1Dir(Path dir) throws IOException {

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.zip")) {
			for (Path entry : stream) {
				result.add(entry);
				System.out.println(entry.getFileName());
			}
		} catch (DirectoryIteratorException ex) {
			// I/O error encounted during the iteration, the cause is an IOException
			throw ex.getCause();
		}
		return result;
	}

	public void listFiles(String dirs) throws IOException {

		PrintStream outer = System.out;

		Path dir = Paths.get(dirs);
		List<String> columns = new ArrayList<>();
		columns.add("ファイル名");
		columns.add("サイズ");

		outer.println(columns);

		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (file.getFileName().toString().endsWith(".java")) {
//    		result.add(file);
					columns.clear();
					columns.add(file.toString());
					columns.add(String.valueOf(Files.size(file)));
					outer.println(columns);
//    		System.out.print(file.toString());
//    		System.out.print(": ");
//    		System.out.println(Files.size(file));
				}
				return FileVisitResult.CONTINUE;
			}
		});

		outer.close();

	}

	/*
	 * 
	 */
	public static void main(String[] args) throws IOException {
		String root = "E:/java/basic/FileSystem";
		String outf = "G:/tmp/fsize.csv";
		ListFile listf = new ListFile();
		listf.listFiles(root);

	}

	//
	// adsf
}
