package nio.path;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.google.common.io.Files;

public class PathTest {

	@Test
	public void test() {
		
		Path p1 = Paths.get("/tmp/foo");
		// \tmp\foo
		System.out.println(p1.toString());
		Path p2 = Paths.get(URI.create("file:///D:/tmp/docker/doc"));
		// D:\tmp\docker\doc
		System.out.println(p2.toString());
		Path p3 = FileSystems.getDefault().getPath(System.getProperty("user.home"),"log", "java.log");
		// C:\Users\hui\log\java.log
		System.out.println(p3.toString());
		Path p4 = Paths.get("D:/", "tmp","docker/doc", "img");
		// D:\tmp\docker\doc\img
		System.out.println(p4.toString());
		// p4的路径针对于指定路径的字符串路径
		Path p5 = p4.resolve("../../a.txt");
		// D:\tmp\docker\doc\img\..\..\a.txt
		System.out.println(p5.toString());
		// p5的真实路径
		Path p6 = p5.normalize();
		// D:\tmp\docker\a.txt
		System.out.println(p6);
		try {
			// D:\tmp\docker\a.txt
			System.out.println(p5.toRealPath(LinkOption.NOFOLLOW_LINKS));
			// D:\tmp\docker\a.txt
			System.out.println(p5.toFile().getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 4
		System.out.println(p6.getNameCount());
		// img
		System.out.println(p6.getFileName());
		// D:\tmp\docker\doc
		System.out.println(p6.getParent());
		
		// D:\
		System.out.println(p6.getRoot());
		for(int i=0;i<p6.getNameCount();i++) {
			System.out.println(p6.getName(i));
		}
		
		// 获取文件名不带扩展名(a.txt的a)
		System.out.println(Files.getNameWithoutExtension(p6.toString()));
		// 获取文件的扩展名(a.txt的txt)
		System.out.println(Files.getFileExtension(p6.toString()));
		
		
		
	}

}
