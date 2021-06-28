package huang.river.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPattern {
	
	public static final String START = "^";
	
	public static final String END = "$";
	
	/**************   不可见字符  ****************/
	
	// 匹配一个换页符。等价于\x0c
	public static final String FORM_FEED = "\\f";
	// 匹配一个换行符。等价于\x0a
	public static final String 	LINE_FEED = "\\n";
	// 匹配一个回车符。等价于\x0d
	public static final String RETURN = "\\r";
	// 匹配一个制表符。等价于\x09
	public static final String TAB = "\\t";
	// 匹配一个垂直制表符。等价于\x0b
	public static final String VTAB = "\\v";
	// 匹配任何不可见字符，包括空格、制表符、换页符等等。等价于[ \f\n\r\t\v]。
	public static final String WHITE_SPACE = "\\s";
	
	// 匹配任何可见字符， [^ \t\r\n\v\f]
	public static final String WHITE_SPACE_NOT = "\\S";
	// 空行
	public static final String EMPTY_LINE = "^\\s*$";
	
	
	/**************   可见字符  ****************/
	// 匹配一个数字字符。等价于[0-9]
	public static final String DIGITAL = "\\d";
	// 匹配一个非数字字符。等价于[^0-9]
	public static final String DIGITAL_NOT = "\\D";
	
	// 任意大小写字母字符
	public static final String ALPHABET = "[A-Za-z]";
	
	// 任意小写字母字符
	public static final String LOWER = "[a-z]";
	// 任意大写字母字符
	public static final String UPPER = "[A-Z]";
	
	// 任意大小写字母数字字符 \p{Alnum}
	public static final String ALPHA_NUMBER = "[A-Za-z0-9]";
	// 任意非大小写字母数字字符
	public static final String ALPHA_NUMBER_NOT = "[^A-Za-z0-9]";
	
	// 匹配一个ASCII字符
	public static final String ASCII = "[\\x00-\\x7F]";
	// 匹配一个CONTROL字符
	public static final String CONTROL  = "[\\x00-\\x1F\\x7F]";
	// 匹配一个可见字符
	public static final String VISIBLE  = "[\\x21-\\x7E]";
	
	// 匹配一个可见字符+空格
	public static final String PRINT = "[\\x20-\\x7E]";
	
	// 匹配一个16进制数字字符
	public static final String HEX = "[A-Fa-f0-9]";
	
	// 匹配包括下划线的任何单词字符。类似但不等价于“[A-Za-z0-9_]”，这里的"单词"字符使用Unicode字符集。
	public static final String WORD = "\\w";
	// 匹配任何非单词字符。等价于“[^A-Za-z0-9_]”。
	public static final String WORD_NOT = "\\W";
	
	// 匹配除“\n”和"\r"之外的任何单个字符,要匹配包括“\n”和"\r"在内的任何字符，请使用像“[\s\S]”的模式。
	public static final String WORD_ANY = ".";
	
	
	/**************   匹配次数  ****************/
	//  匹配前面的子表达式任意次，包括0次 等价于{0,}
	public static final String TIME_ANY = "*";
	//  匹配前面的子表达式一次或多次(大于等于1次） 等价于{1,}
	public static final String TIME_1_N = "+";
	//  匹配前面的子表达式零次或一次。等价于{0,1}。
	public static final String TIME_0_1 = "？";
	
	// {n} n是一个非负整数。匹配确定的n次。
	// {n,} n是一个非负整数。至少匹配n次。
	// {n,m} m和n均为非负整数，其中n<=m。最少匹配n次且最多匹配m次。
	
	public static boolean isEmptyLine(String target) {
		return Pattern.matches(EMPTY_LINE, target);
	}
	
	public static Pattern getPattern(String regexStr) {
		return Pattern.compile(regexStr);
	}
	
	/**
	 * 部分匹配，总是从第一个字符进行匹配,匹配成功了不再继续匹配，匹配失败了,也不继续匹配。
	 */
	public static void lookAtDemo() {
		Pattern pattern = Pattern.compile("\\d{3,5}");
        String str = "123-34345-234-00";
        Matcher matcher = pattern.matcher(str);
        // 返回 true
        System.out.println(matcher.lookingAt());
        // 结果为 123 - 0
        System.out.println(matcher.group()+" - "+matcher.start()); // 123 - 0
        //第二次find匹配以及匹配的目标和匹配的起始位置
        System.out.println(matcher.lookingAt()); // true
        System.out.println(matcher.group()+" - "+matcher.start()); // 34345 - 4
       
	}
	
	/**
	 * find:部分匹配，从当前位置开始匹配，找到一个匹配的子串，将移动下次匹配的位置。
	 */
	public static void findDemo() {
		Pattern pattern = Pattern.compile("\\d{3,5}");
        String str = "123-34345-234-00";
        Matcher matcher = pattern.matcher(str);
        // 返回 true
        System.out.println(matcher.find());
        // 结果为 123 - 0
        System.out.println(matcher.group()+" - "+matcher.start()); // 123 - 0
        //第二次find匹配以及匹配的目标和匹配的起始位置
        System.out.println(matcher.find()); // true
        System.out.println(matcher.group()+" - "+matcher.start()); // 34345 - 4
       
	}
	
	/**
	 * 整个匹配，只有整个字符序列完全匹配成功，才返回True，否则返回False。
	 * 虽然结果返回的是false，但如果前部分匹配成功，将移动下次匹配的位置。
	 */
	public static void matchesDemo() {
		Pattern pattern = Pattern.compile("\\d{3,5}");
        String str = "123-34345-234-00";
        Matcher matcher = pattern.matcher(str);
        // 返回 false
        System.out.println(matcher.matches());
        // 测试匹配位置
        // 虽然结果返回的是false,但是因为123部分匹配成功了，所以会从123以后开始再次查找
        matcher.find();
        // 匹配到的是34345所以结果为 4
        System.out.println(matcher.start());
       
        str = "12345";
        matcher = pattern.matcher(str);
        System.out.println(matcher.matches());
	}
	
	

}
