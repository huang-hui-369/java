package lhn.file;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
			DeepFinder.getGlobFile().setPathProcessor(new MyProcessor())
			.grep("D:\\GYOSYA\\LOG\\NYUUSATU\\DBRENKEI\\SEIJYOU\\20210718", "**/06kensetsukobetsu_2_a000_*.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class MyProcessor extends DeepFinder.DefaultPathProcessor {
		@Override
		public void processFile(Path p) throws IOException {
			Hashtable<String, List<String>> gyosyaList = new Hashtable();
			System.out.format("file:%s\n",p.toAbsolutePath());
			List<String >lines = SpFiles.readFileIgnoreErr(p, Charset.forName("UTF-8"));
			int i = 0;
			for(String line : lines) {
				i++;
				if(i == 1) {
					continue;
				}
				String[] Columns = line.split(",");
				List<String> item = gyosyaList.get(Columns[2]);
				if(item==null) {
					item = new ArrayList<String>();
				}
				item.add(line);
				gyosyaList.put(Columns[2], item);
			}
			
			gyosyaList.forEach((gyousya, itemList) -> {
				if(itemList.size()>1) {
					System.out.println(gyousya + " : " + itemList.size());
				}
				
			});
			
		}

		@Override
		public void preProcessDir(Path p) {
			System.out.format("dir:%s\n", p.toAbsolutePath());
		}
	}

}
