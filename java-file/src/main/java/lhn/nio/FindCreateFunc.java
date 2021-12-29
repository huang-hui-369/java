package lhn.nio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindCreateFunc {

	/**
	 * A {@code FileVisitor} that finds all files that match the specified pattern.
	 */
	static class Finder extends SimpleFileVisitor<Path> {

		private final PathMatcher matcher;
		private int numMatches = 0;
		
		private Pattern openCusrPattern = Pattern.compile("\\s*CREATE FUNCTION\\s*[\\w\\W]+", Pattern.CASE_INSENSITIVE);
		
		private Pattern insertPattern = Pattern.compile("\\s*ssma_oracle\\s*[\\w\\W]*", Pattern.CASE_INSENSITIVE);
		
		private int openCusrLineNo = -1;
		private int openCusrLineNo2 = -1;
		private String cusr2Name = "";
		private String cusrName = "";
		
		private StringBuffer sb = new StringBuffer();
		
		private List<String> rstList = new ArrayList<String>();
		
		private int matchCount = 0;

		public Finder(String pattern) {
			matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		}

		// Compares the glob pattern against
		// the file or directory name.
		void find(Path file) throws IOException {
			Path name = file.getFileName();
			if (name != null && matcher.matches(name)) {
				numMatches++;
//				System.out.println(file);
			
				processFile(file);
				
				
			}
		}
		
		void processFile(Path file)  {
			
			
			
			// 璇诲嚭鎵�鏈夎
			List<String> lineList;
			String line = null;
			int i = 0;
			try {
//				System.out.println(file.getFileName());
//				lineList = Files.readAllLines(file, Charset.forName("shift-jis"));
//				
//				// 澶勭悊姣忎竴琛�
//				 for(int i=0; i<lineList.size(); i++ ) {
//					// 鏇挎崲涓�琛�
//					 processLine(lineList.get(i), i);
//					 
//				 }
				
//				Charset.forName("shift-jis").newDecoder();
				 CharsetDecoder decoder = Charset.forName("utf-8").newDecoder();
			     decoder.onMalformedInput(CodingErrorAction.REPORT);
				
				this.rstList.add(file.getFileName().toString());
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(new File(file.toString())), 
						decoder ));
				while((line = br.readLine()) != null) {
					 processLine(line, i);
					 i++;
				}
				clearCusr();
				
			} catch (IOException e) {
				System.out.println(file.getFileName());
				System.out.println(String.format("%d:%s", i , line));
				e.printStackTrace();
			}
			
			
		}
			
		void processLine(String line, int lineNo) {	
//			OPEN CURS_00;
//			OPEN CURS_01;
//			OPEN CURS_02;
//			FETCH CURS_00 INTO LINE_CURS_00;
//			FETCH CURS_01 INTO LINE_CURS_01;
//			FETCH CURS_02 INTO LINE_CURS_02;
			
			// find OPEN CURS_0
			if(openCusrLineNo < 0) {
				Matcher openCusrM = openCusrPattern.matcher(line);
				if(openCusrM.find()) {
//					System.out.format("found [%s]\n", openCusrM.group(0));
					sb.append(lineNo).append(':');
					sb.append(openCusrM.group(0)).append(":     ");
					openCusrLineNo = lineNo;
//					this.cusrName =  openCusrM.group(1);
				} 
				
			} else {
				Matcher openCusrM = openCusrPattern.matcher(line);
				if(openCusrM.find()) {
					sb.setLength(0);
					sb.append(lineNo).append(':');
					sb.append(openCusrM.group(0)).append(':');
					openCusrLineNo = lineNo;
				}
				// find OPEN CURS_1
				if(this.openCusrLineNo2<0) {
					Matcher insertM = insertPattern.matcher(line);
					if(insertM.find()) {
//						System.out.format("found [%s]\n", openCusrM.group(0));
						sb.append(insertM.group(0)).append(':');
						openCusrLineNo2 = lineNo;
//						this.cusr2Name =  openCusrM.group(1);
						clearCusr();
						matchCount ++;
					} 
				} 
				
//				else { // find FETCH
//					Pattern fetchPattern = Pattern.compile("FETCH\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
//					Matcher fetchM = fetchPattern.matcher(line);
//					if(fetchM.find()) {
//						if(cusrName.equals(fetchM.group(1)))
////						System.out.format("found [%s]\n", fetchM.group(0));
//						sb.append(fetchM.group(0));
//						rstList.add(sb.toString());
//						clearCusr();
//						matchCount ++;
//					}
//				}
			}
			
		}
		
		void clearCusr() {
			this.cusr2Name = "";
			this.cusrName = "";
			this.openCusrLineNo = -1;
			this.openCusrLineNo2 = -1;
			System.out.println(sb.toString());
			sb.setLength(0);
		}

		// Prints the total number of
		// matches to standard out.
		void done() {
			System.out.println("Matched: " + numMatches);
			for(String str : rstList) {
				System.out.println(str);
			}
			System.out.println(matchCount);
		}

		// Invoke the pattern matching
		// method on each file.
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			find(file);
			return FileVisitResult.CONTINUE;
		}

		// Invoke the pattern matching
		// method on each directory.
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			find(dir);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			System.err.println(exc);
			return FileVisitResult.CONTINUE;
		}
	}

	static void usage() {
		System.err.println("java Find <path>" + " -name \"<glob_pattern>\"");
		System.exit(-1);
	}

	public void process(String filePath, String namep) throws IOException {

		Path startingDir = Paths.get(filePath);
		String pattern = namep;

		Finder finder = new Finder(pattern);
		Files.walkFileTree(startingDir, finder);
		finder.done();
	}
}