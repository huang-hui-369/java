package huang.river.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class SpFiles {
	
	public static List<String> readFileUtf8(Path filepath) throws IOException {
		return readFile(filepath, FileCharset.UTF8, CodingErrorAction.REPORT);
	}
	
	public static List<String> readFileUtf8IgnoreErr(Path filepath) throws IOException {
		return readFile(filepath, FileCharset.UTF8, CodingErrorAction.IGNORE);
	}
	
	public static List<String> readFile(Path filepath, Charset charset) throws IOException {
		return readFile(filepath, charset, CodingErrorAction.REPORT);
	}
	
	public static List<String> readFileIgnoreErr(Path filepath, Charset charset) throws IOException {
	    return readFile(filepath, charset, CodingErrorAction.IGNORE);
	}
	
	public static List<String> readFileReplace(Path filepath) throws IOException {
	    return readFile(filepath, FileCharset.UTF8, CodingErrorAction.REPLACE);
	}
	
	public static List<String> readFileReplace(Path filepath, Charset charset) throws IOException {
	    return readFile(filepath, charset, CodingErrorAction.REPLACE);
	}
	
	public static List<String> readFile(Path filepath, Charset charset, CodingErrorAction action) throws IOException {
		CharsetDecoder decoder = charset.newDecoder();
		decoder.onMalformedInput(action);
	    return readFile(filepath.toFile(), decoder);
	}
	
	public static List<String> readFile(File file, CharsetDecoder decoder) throws IOException {
		List<String> strList = new ArrayList<String>();
		
		try(BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream(file), decoder ) )) {
			String linestr = null;
			while((linestr = br.readLine()) != null) {
				strList.add(linestr);
			}
		}
		
		return strList;
	}
	
	public static void writeFileUtf8(Path filepath, List<String> lines) throws IOException {
		wirteFile(filepath, FileCharset.UTF8, lines, CodingErrorAction.REPORT);
	}
	
	public static void writeFileUtf8IgnoreErr(Path filepath, List<String> lines) throws IOException {
		wirteFile(filepath, FileCharset.UTF8, lines, CodingErrorAction.IGNORE);
	}
	
	public static void wirteFile(Path filepath, Charset charset,  List<String> lines, CodingErrorAction action) throws IOException {
		CharsetEncoder encoder = charset.newEncoder();
		encoder.onUnmappableCharacter(action);
		writeFile(filepath, encoder, lines);
	}
	
	public static void writeFile(Path filepath, CharsetEncoder encoder, List<String> lines) throws IOException {
		
		try(BufferedWriter wr = new BufferedWriter( new OutputStreamWriter( Files.newOutputStream(
						filepath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING), encoder ))) {
			
			 for (CharSequence line: lines) {
	                wr.append(line);
	                wr.newLine();
	            }
		}
		
		
	}
	
	public static class FileCharset {
		public static Charset UTF8 = StandardCharsets.UTF_8;
		public static Charset ISO8859_1 = StandardCharsets.ISO_8859_1;
		public static Charset SHIFTJIS = Charset.forName("SHIFT-JIS");
		public static Charset SHIFTJIS_WIN = Charset.forName("windows-31j");
		
	}

}
