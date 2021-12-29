package lhn.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

public class ReaderWriter {

	static void process(String srcpath) {
		
		String despath = srcpath + ".bak";
		
		File srcf = new File(srcpath);
		
		File desf = new File(despath);
		
		
		String line = null;
		int i = 0;
		
		 CharsetDecoder decoder = Charset.forName("windows-31j").newDecoder();
	     decoder.onMalformedInput(CodingErrorAction.REPLACE);
	     
//			Charset.forName("shift-jis").newDecoder();
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(srcf), 
				decoder ));
				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(
						new FileOutputStream(desf),Charset.forName("windows-31j")
						));
				) {
			
			
			while((line = br.readLine()) != null) {
				bw.write(line);
				bw.newLine();
				 i++;
			}
			
		} catch (IOException e) {
			System.out.println(srcpath);
			System.out.println(String.format("%d:%s", i , line));
			e.printStackTrace();
		} 
		
	}
	
}
