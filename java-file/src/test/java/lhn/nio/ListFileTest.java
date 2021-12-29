package lhn.nio;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import lhn.nio.ListFile;

public class ListFileTest {

	@Test
	public void testListDirs() {
		ListFile listf = new ListFile();
		try {
			listf.list1Dir(Paths.get("D:\\tmp\\java"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testListFiles() {
		ListFile listf = new ListFile();
		try {
			listf.listFiles("D:\\tmp\\java");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
