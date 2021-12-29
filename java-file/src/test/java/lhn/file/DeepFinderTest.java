package lhn.file;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Test;

import lhn.file.DeepFinder;

public class DeepFinderTest {

	@Test
	public void testAllDirFile() {
		try {
			DeepFinder.getAllFileDir().grep(".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAllDir() {
		try {
			DeepFinder.getAllDir().grep(".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAllFile() {
		try {
			DeepFinder.getAllFile().grep(".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testGrepFileGlob() {
		try {
			DeepFinder o = DeepFinder.getGlobFile();
			o.grep(".", "**/*.java");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGrepFileRegex() {
		try {
			DeepFinder.getRegexFile().grep(".", ".*class");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testGrepDir() {
		try {
			DeepFinder.getRegexDir().grep(".", ".*hn.*");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void testProcessor() {
		try {
			DeepFinder.getRegexDir().setPathProcessor(new MyProcessor())
			.grep(".", ".*lhn.*");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class MyProcessor extends DeepFinder.DefaultPathProcessor {
		@Override
		public void processFile(Path p) {
			System.out.format("file:%s\n",p.toAbsolutePath());
		}

		@Override
		public void preProcessDir(Path p) {
			System.out.format("dir:%s\n", p.toAbsolutePath());
		}
	}

}
