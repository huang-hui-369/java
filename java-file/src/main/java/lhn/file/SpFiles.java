package lhn.file;

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
	
	/**
	 * 根据指定的文件路径读取文件内容，根据指定的字符编码解码字符，将读取的文件的所有行放入list中返回。
	 * @param filepath		要读文件路径
	 * @param charset		文件的字符编码，如果不指定字符编码默认用utf-8编码。
	 * @return				返回整个文件的所有行放在list中
	 * @throws IOException  当有字符不能解码成为乱码时（机器依存的字符），会抛出IOException
	 */
	public static List<String> readFileReport(Path filepath, Charset... charset) throws IOException {
		return readFile(filepath, CodingErrorAction.REPORT, charset);
	}
	
	/**
	 * 根据指定的文件路径读取文件内容，根据指定的字符编码解码字符，将读取的文件的所有行放入list中返回。
	 * @param filepath		要读文件路径
	 * @param charset		文件的字符编码，如果不指定字符编码默认用utf-8编码。
	 * @return				返回整个文件的所有行放在list中
	 * @throws IOException  当有字符不能解码成为乱码时（机器依存的字符），不会抛出IOException，直接忽略乱码。
	 */
	public static List<String> readFileIgnoreErr(Path filepath, Charset... charset) throws IOException {
	    return readFile(filepath, CodingErrorAction.IGNORE, charset);
	}
	
	/**
	 * 根据指定的文件路径读取文件内容，根据指定的字符编码解码字符，将读取的文件的所有行放入list中返回。
	 * @param filepath		要读文件路径
	 * @param charset		文件的字符编码，如果不指定字符编码默认用utf-8编码。
	 * @return				返回整个文件的所有行放在list中
	 * @throws IOException  当有字符不能解码成为乱码时（机器依存的字符），不会抛出IOException，
	 *                      直接将乱码字符替换成？，？可以通过decoder.replaceWith(newReplacement)来设置。
	 */
	public static List<String> readFileReplace(Path filepath, Charset... charset) throws IOException {
	    return readFile(filepath, CodingErrorAction.REPLACE, charset);
	}
	
	public static List<String> readFile(Path filepath, CodingErrorAction action, Charset... charset) throws IOException {
		Charset charseto = null;
		if(charset==null || charset.length==0) {
			charseto = FileCharset.UTF8;
		} else {
			charseto = charset[0];
		}
		CharsetDecoder decoder = charseto.newDecoder();
		decoder.onMalformedInput(action);
		decoder.onUnmappableCharacter(action);
	    return readFile(filepath.toFile(), decoder);
	}
	
	/**
	 * 一行一行读取文件，将读取的文件的所有行放入list中返回。
	 * @param file
	 * @param decoder
	 * @return
	 * @throws IOException
	 */
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
	
	
	/**
	 * 将文件的内容根据指定的字符编码写入指定的文件路径。
	 * @param filepath		要写入文件路径
	 * @param lines			文件内容
	 * @param charset		文件的字符编码，如果不指定字符编码默认用utf-8编码。
	 * @throws IOException	当有字符不能编码写入成为乱码时（机器依存的字符），会抛出IOException
	 */
	
	public static void writeFileReport(Path filepath, List<String> lines, Charset... charset) throws IOException {
		wirteFile(filepath, lines, CodingErrorAction.REPORT, charset);
	}
	
	/**
	 * 将文件的内容根据指定的字符编码写入指定的文件路径。
	 * @param filepath		要写入文件路径
	 * @param lines			文件内容
	 * @param charset		文件的字符编码，如果不指定字符编码默认用utf-8编码。
	 * @throws IOException	当有字符不能编码写入成为乱码时（机器依存的字符），不会抛出IOException，会直接忽略该错误。
	 */
	public static void writeFileIgnoreErr(Path filepath, List<String> lines, Charset... charset) throws IOException {
		wirteFile(filepath, lines, CodingErrorAction.IGNORE, charset);
	}
	
	/**
	 * 将文件的内容根据指定的字符编码写入指定的文件路径。
	 * @param filepath		要写入文件路径
	 * @param lines			文件内容
	 * @param charset		文件的字符编码，如果不指定字符编码默认用utf-8编码。
	 * @throws IOException	当有字符不能编码写入成为乱码时（机器依存的字符），不会抛出IOException，会直接将乱码字符替换成？后写入文件。
	 */
	public static void writeFileReplace(Path filepath, List<String> lines, Charset... charset) throws IOException {
		wirteFile(filepath, lines, CodingErrorAction.REPLACE, charset);
	}
	
	/**
	 * 将文件的内容根据指定的字符编码写入指定的文件路径。
	 * @param filepath		要写入文件路径
	 * @param lines			文件内容
	 * @param action		当字符编码有问题时所采取的处理方式
	 * @param charset		文件的字符编码，如果不指定字符编码默认用utf-8编码。
	 * @throws IOException	当有字符不能编码写入成为乱码时（机器依存的字符），根据处理方式有可能抛出IOException。
	 */
	public static void wirteFile(Path filepath,  List<String> lines, CodingErrorAction action, Charset... charset) throws IOException {
		Charset charseto = null;
		if(charset == null) {
			charseto =  FileCharset.UTF8;
		} else {
			charseto =  charset[0];
		}
		
		CharsetEncoder encoder = charseto.newEncoder();
		encoder.onMalformedInput(action);
		encoder.onUnmappableCharacter(action);
		writeFile(filepath, encoder, lines);
	}
	
	
	/**
	 * 一行一行将文件的内容根据指定的字符编码写入指定的文件路径。
	 * @param filepath		要写入文件路径
	 * @param encoder		字符编码器
	 * @param lines			文件内容
	 * @throws IOException
	 */
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
