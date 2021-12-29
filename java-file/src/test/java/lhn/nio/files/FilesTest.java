package lhn.nio.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Joiner;

public class FilesTest {

	@Test
	public void testCharsets() {
		System.out.println(Charset.defaultCharset().toString());
		System.out.println(Joiner.on('\n').withKeyValueSeparator(":").join(Charset.availableCharsets()));
	}
	
	@Test
	public void testCopy() {
		Path base = Paths.get(System.getProperty("user.dir"),"src/test/resources/");
		Path source = base.resolve("dir1/dir2/a.txt");
		Path target = base.resolve("dir1/dir2/a.txt.bak");;
		try {
			Files.copy(source,
			           target,
			           StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	@Test
	public void testMove() {
		Path base = Paths.get(System.getProperty("user.dir"),"src/test/resources/");
		Path source = base.resolve("dir1/dir2/a.txt.bak");
		Path target = base.resolve("dir1/dir2/b.txt");;
		try {
			Files.move(source,
			           target,
			           StandardCopyOption.REPLACE_EXISTING,
			           StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	@Test
	public void testReadSmallFile() {
		Path base = Paths.get(System.getProperty("user.dir"),"src/test/resources/");
		Path target = base.resolve("dir1/dir2/b.txt");;
		try {
			List<String> lines = Files.readAllLines(target, Charset.forName("UTF-8"));
			lines.forEach(str->{
				System.out.println(str);
			});
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	@Test
	public void testReadBigFile() {
		Path base = Paths.get(System.getProperty("user.dir"),"src/test/resources/");
		Path target = base.resolve("dir1/dir2/b.txt");;
		try(BufferedReader br = Files.newBufferedReader(target)) {
			String str = null;
			while((str = br.readLine())!=null) {
				System.out.println(str);
			}
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	@Test
	public void testWalk() {
		Path p = Paths.get(".");
		try {
			// FileVisitOption.FOLLOW_LINKS
			Files.walk(p, FileVisitOption.FOLLOW_LINKS).forEach(f->{
				if(Files.isDirectory(f)) {
					System.out.format("dir:%s\n", f.getFileName());
				} else if(Files.isRegularFile(f)) {
					System.out.format("file:%s\n", f.getFileName());
				} else {
					System.out.format("other:%s\n", f.getFileName());
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWalk1level() {
		Path p = Paths.get(".");
		try {
			// FileVisitOption.FOLLOW_LINKS
			Files.walk(p, 1).forEach(f->{
				if(Files.isDirectory(f)) {
					System.out.format("dir:%s\n", f.getFileName());
				} else if(Files.isRegularFile(f)) {
					System.out.format("file:%s\n", f.getFileName());
				} else {
					System.out.format("other:%s\n", f.getFileName());
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFind() {
		Path p = Paths.get(".");
		try {
			// FileVisitOption.FOLLOW_LINKS
			Files.walk(p, FileVisitOption.FOLLOW_LINKS).forEach(f->{
				if(Files.isDirectory(f)) {
					System.out.format("dir:%s\n", f.getFileName());
				} else if(Files.isRegularFile(f)) {
					System.out.format("file:%s\n", f.getFileName());
				} else {
					System.out.format("other:%s\n", f.getFileName());
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOther() {
//		101
//		110
//		111
		System.out.println(5&5);
		System.out.println(5&7);
		System.out.println(6&6);
		System.out.println(6&5);
		System.out.println(6&7);
		
	}

}
