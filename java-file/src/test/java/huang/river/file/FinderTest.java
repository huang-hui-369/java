package huang.river.file;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Test;

public class FinderTest {

	@Test
	public void testGrepAllFile() {
		try {
			Finder.getDefallt().grep(".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGrepAllFileDir() {
		try {
			Finder o = Finder.getDefallt();
			o.setModle(Finder.MODLE_FILE_DIR);
			o.grep(".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGrepString3() {
		try {
			Finder o = Finder.getDefallt();
			o.setModle(Finder.MODLE_FILE);
			o.grep(".", "**/*.java");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGrepRegx() {
		try {
			Finder o = Finder.getDefallt();
			o.setPattern(Finder.PathPattern.REGEX);
			o.grep(".", ".*Durid.*java");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testProcessor() {
		try {
			Finder o = Finder.getDefallt();
			o.setModle(Finder.MODLE_DIR);
			o.setProcessor(new MyProcessor());
//			o.setPattern(Finder.PathPattern.REGEX);
			o.grep(".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGrepStringString() {
		fail("Not yet implemented");
	}
	
	class MyProcessor extends Finder.DefaultPathProcessor {
		@Override
		public void preProcess(Path p) {
			System.out.println(p.getFileName());
		}

		@Override
		public void postProcess(Path p) {
			System.out.format("post:%s\n", p.getFileName());
		}
	}

}
