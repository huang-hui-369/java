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
		
		System.out.println(Paths.get("D:/tmp").relativize(p2).toString());
		
		Path p3 = FileSystems.getDefault().getPath(System.getProperty("user.home"),"log", "java.log");
		// C:\Users\hui\log\java.log
		System.out.println(p3.toString());
		Path p4 = Paths.get("D:/", "tmp","docker/doc", "img");
		// D:\tmp\docker\doc\img
		System.out.println(p4.toString());
		// p4��·�������ָ��·�����ַ���·��
		Path p5 = p4.resolve("../../a.txt");
		// D:\tmp\docker\doc\img\..\..\a.txt
		System.out.println(p5.toString());
		// p5����ʵ·��
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
		
		// ��ȡ�ļ���������չ��(a.txt��a)
		System.out.println(Files.getNameWithoutExtension(p6.toString()));
		// ��ȡ�ļ�����չ��(a.txt��txt)
		System.out.println(Files.getFileExtension(p6.toString()));
		
		
		
	}

}
