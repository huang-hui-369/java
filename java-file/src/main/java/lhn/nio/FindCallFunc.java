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

public class FindCallFunc {

	/**
	 * A {@code FileVisitor} that finds all files that match the specified pattern.
	 */
	static class Finder extends SimpleFileVisitor<Path> {

		private final PathMatcher matcher;
		private int numMatches = 0;
		
		// \\$impl
		private Pattern funcPattern = Pattern.compile("create.*procedure.*\\$impl", Pattern.CASE_INSENSITIVE);
		
		private static final String WORD_BEGIN = "BEGIN";
		
		private static final String WORD_END = "CREATE PROCEDURE";
		
		private String funcName;
		
		private String[] dmlList = {
			"INSERT",
			"DELETE",
			"UPDATE",
			"MERGE"
		};
		
		private int findFunc = -1;
		private int findBegin = -1;
		private int findeEnd = -1;
		
		private StringBuffer sb = new StringBuffer();
		
		private List<String> rstList = new ArrayList<String>();
		
		private int matchCount = 0;

		public Finder(String pattern) {
			matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		}
		
		public Finder(String pattern, String funcName) {
			matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
			this.funcName = funcName;
		}

		// Compares the glob pattern against
		// the file or directory name.
		void find(Path file) throws IOException {
			Path name = file.getFileName();
			if (name != null && matcher.matches(name)) {
				numMatches++;
//				System.out.println(file.getFileName());
			
				processFile(file);
				
				
			}
		}
		
		void processFile(Path file)  {
			
			
			
			// file all line
			List<String> lineList;
			String line = null;
			int i = 0;
			try {
				
//				Charset.forName("shift-jis").newDecoder();
				 CharsetDecoder decoder = Charset.forName("windows-31j").newDecoder();
			     decoder.onMalformedInput(CodingErrorAction.REPORT);
			     clearFind();
				this.rstList.add(file.getFileName().toString());
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(new File(file.toString())), 
						decoder ));
				while((line = br.readLine()) != null) {
					 processLine(line, i);
					 if(this.findeEnd>=0) {
						 clearFind();
						 return;
					 }
					 i++;
				}
				
				clearFind();
				
			} catch (IOException e) {
				System.out.println(file.getFileName());
				System.out.println(String.format("%d:%s", i , line));
				e.printStackTrace();
			}
			
			
		}
			
		void processLine(String line, int lineNo) {	
			
			// find func
			String lineUpper = line.toUpperCase();
			
			if(findFunc < 0) { // find func
				Matcher macher = funcPattern.matcher(lineUpper);
				if(macher.find()) {
					this.findFunc = 1;
					this.rstList.add(String.format("line:%d,%s", lineNo, line));
					return;
				}
				
			} else {
				// find begin
				if(this.findBegin<0) {
					this.findBegin = lineUpper.indexOf(WORD_BEGIN);
					if(findBegin >0) {
						return;
					}
				} else { // find dml
					
					this.findeEnd = lineUpper.indexOf(WORD_END);
					if(this.findeEnd>=0) {
						return;
					}
					for(int i =0; i<this.dmlList.length; i++) {
						int idx = lineUpper.indexOf(this.dmlList[i]);
						if(idx>=0) {
							this.findFunc = 1;
							this.rstList.add(String.format("line:%d,%s", lineNo, line));
							clearFind();
							return;
						}
					}
				}
			}
			
		}
		
		void clearFind() {
			this.findBegin = -1;
			this.findeEnd = -1;
			this.findFunc = -1;
//			this.rstList.clear();
//			sb.setLength(0);
		}

		// Prints the total number of
		// matches to standard out.
		void done() {
//			System.out.println("Matched: " + numMatches);
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

		String[] funcList = {
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_ANKEN_ID$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_ANKEN_ID_KOUSEI$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_EIGYOUSYO_ID$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_EIGYOUSYO_ID_KOUSIN$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_EIGYOUSYO_ID_SAISIN$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_KAKUDUKE_CODE$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_KAKUDUKE_SYORI$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_KEIYAKU_HOUHOU_KONKYO$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_KENNAI_KENGAI_SYUBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_KUBUN_CODE$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_L1_CODE$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_L2_CODE$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_L3_CODE$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_NAIBU_KASYO_ID$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_NAIBU_ZIGYOU_ID$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_SIKAKU_CODE$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_TIIKI_CODE$IMPL]",
				"CREATE PROCEDURE [SAI16].[CODE_MAPPING_LIBRARY$FUNC_TODOUHUKEN_CODE$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$CHECK_EIGYOUSYO_KOUSIN_RIREKI$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$CHECK_EIGYOUSYO_SAISIN_RIREKI$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$CHECK_GYOUSYA_KOUSIN_RIREKI$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$CHECK_GYOUSYA_SAISIN_RIREKI$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$CHECK_HENKOU_IPPAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$CHECK_HENKOU_JV$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_IKOU_KIBOU_FLAG$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_KASYO_CODE$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_NENDO$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_SAISIN_ANKEN_RIREKI_NO$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_SAISIN_EIGYOU_RIREKI_NO$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_SAISIN_GYOUSYA_RIREKI_NO$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_SAISIN_TORIKESI_FLAG$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_TANTOU_KASYO_ID$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_TO_DATE$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_TO_NUMBER$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_TUUTI_BUNSYO_RYA_HANI$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$FUNC_WAREKI_MEIBO_MEI$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$GET_ANKEN_RIREKI_SAKUSEI_FLAG$IMPL]",
				"CREATE PROCEDURE [SAI16].[COMMON_LIBRARY$REPLACE_KASYO_CODE$IMPL]",
				"CREATE PROCEDURE [SAI16].[CREATE_SUMMARY_DATA$FUNC_GET_TOUSYO_NENDOWARIGAKU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXECUTE_DATA_IKOU$FUNC_IS_IKOU_TAISYOU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_ANKEN_NYUUSATU_ANKEN$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_ANKEN_SIKKOU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_ANKEN_SIKKOU_KAMOKU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_ANKEN_SISYUTU_HUTAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_ANKEN_SISYUTU_HUTA_KAMO$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_ANKEN_SISYUTU_HUTA_SAI$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_NYUUSATU_ANKEN$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_NYUUSATU_ANKEN_KOUZI_NULL$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_SIKKOU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_SIKKOU_KAMOKU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_SISYUTU_HUTAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_SISYUTU_HUTAN_KAMOKU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_SISYUTU_HUTAN_SAIKENSYA$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD$FUNC_SISYUTU_HUTAN_SAI_KAMO$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_DOBOKU_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_DOBOKU_KYOUTUU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_EIGYOU_DOBOKU_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_EIGYOU_DOBOKU_KYOUTUU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_EIGYOU_KEN_KOBETU_KOU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_EIGYOU_KEN_KOBETU_ZEN$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_EIGYOU_KEN_KYOU_KOU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_EIGYOU_KEN_KYOU_ZEN$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_EIGYOU_KIHON_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_EIGYOU_KIHON_KYOUTUU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_EIGYOU_SEKKEI_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_EIGYOU_SEKKEI_KYOU_KOU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_EIGYOU_SEKKEI_KYOU_ZEN$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_KEN_KOBETU_KOU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_KEN_KOBETU_ZEN$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_KEN_KYOU_KOU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_KEN_KYOU_ZEN$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_KIHON_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_KIHON_KYOUTUU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_SEKKEI_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_SEKKEI_KYOU_KOU$IMPL]",
				"CREATE PROCEDURE [SAI16].[EXISTS_DATA_RECORD2$FUNC_SEKKEI_KYOU_ZEN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_DOBOKU_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_DOBOKU_KYOUTUU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_KENSETU_KOBETU_KOUHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_KENSETU_KOBETU_ZENHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_KENSETU_KYOUTUU_KOUHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_KENSETU_KYOUTUU_ZENHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_KIHON_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_KIHON_KYOUTUU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_NYUUSATU_ANKEN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_NYUUSATU_SANKA_GYOUSYA$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_SEISEKI_HYOUTEI$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_SEKKEI_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_SEKKEI_KYOUTUU_KOUHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_SEKKEI_KYOUTUU_ZENHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_SIKKOU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_SIKKOU_KAMOKU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_SISYUTU_HUTAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_SISYUTU_HUTAN_KAMOKU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_SISYUTU_HUTAN_NENDOWARI$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_BODY_ID$FUNC_SISYUTU_HUTAN_SAIKENSYA$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_ANKEN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_DOBOKU_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_DOBOKU_KYOUTUU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_EIGYOUSYO$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_KENSETU_KOBETU_KOUHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_KENSETU_KOBETU_ZENHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_KENSETU_KYOUTUU_KOUHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_KENSETU_KYOUTUU_ZENHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_KIHON_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_KIHON_KYOUTUU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_NYUUSATU_ANKEN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_NYUUSATU_SANKA_GYOUSYA$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_SEISEKI_HYOUTEI$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_SEKKEI_KOBETU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_SEKKEI_KYOUTUU_KOUHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_SEKKEI_KYOUTUU_ZENHAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_SIKKOU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_SIKKOU_KAMOKU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_SISYUTU_HUTAN$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_SISYUTU_HUTAN_KAMOKU$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_SISYUTU_HUTAN_NENDOWARI$IMPL]",
				"CREATE PROCEDURE [SAI16].[GET_DATA_ZYOUTAI_FOR_UPDATE$FUNC_SISYUTU_HUTAN_SAIKENSYA$IMPL]",
				"CREATE PROCEDURE [SAI16].[MANAGE_ANKEN$FUNC_ANKEN_YUUKOU_FLAG$IMPL]",
				"CREATE PROCEDURE [SAI16].[MANAGE_ANKEN$FUNC_CHECK_SISYUTU_RIYUU$IMPL]",
				"CREATE PROCEDURE [SAI16].[MANAGE_ANKEN$FUNC_CHECK_SYORI_TAISYOU_KOUZI$IMPL]",
				"CREATE PROCEDURE [SAI16].[MANAGE_ANKEN$FUNC_CHECK_TORIKESI$IMPL]",
				"CREATE PROCEDURE [SAI16].[MANAGE_GYOUSYA$FUNC_GET_SYUUKEI_ZYOUTAI$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$CHECK_ANKEN_CHECK_KAIHI$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20207$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20305$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20403$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20404$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20405$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20406$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20407$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20408$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20409$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20410$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20411$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20412$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20413$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20415$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20416$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20417$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20418$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20419$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_20420$IMPL]",
				"CREATE PROCEDURE [SAI16].[RULE_CHECK_LIBRARY$FUNC_KANSEI_BI$IMPL]",
				"CREATE PROCEDURE [ssma_oracle].[DBMS_SQL_FETCH_ROWS$IMPL]",
				"CREATE PROCEDURE [ssma_oracle].[utl_file_fopen$impl]",
				"CREATE PROCEDURE [SAI16].[CREATE_SUMMARY_DATA$PROC_ANKEN]"
		};
		
		Path startingDir = Paths.get(filePath);
		String pattern = namep;
//        for(int i=0;i<funcList.length;i++) {
//        	Finder finder = new Finder(pattern,funcList[i]);
//    		Files.walkFileTree(startingDir, finder);
//    		finder.done();
//        }
		
        Finder finder = new Finder(pattern);
		Files.walkFileTree(startingDir, finder);
		finder.done();
	}
}