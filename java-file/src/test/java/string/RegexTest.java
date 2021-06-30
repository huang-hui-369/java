package string;

import static org.junit.Assert.assertNotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegexTest {

	@Test
	public void test() {
	//  ![](../../img/xxx.png) \s*([../]*)(\/[\d+-+\d]+.{png,jpg}\))
		String str = "![](../../img/2020-10-13-14-57-46.png) asdf\n";
//		Pattern imgPattern = Pattern.compile("(!\\[\\]\\()\\s*(\\.\\.[/\\])+(img[/\\](\\d+-)+\\d\\.{png,jpg}\\))");
		Pattern imgPattern2 = Pattern.compile("(!\\[\\]\\()\\s*((\\.\\.[/\\\\])+)(img[/\\\\](\\d+-)+\\d+\\.(png|jpg))\\)");
		Matcher m = imgPattern2.matcher(str);
		System.out.println(m.find());
		for(int i=0; i< m.groupCount(); i++) {
			System.out.println(m.group(i));
		}
		System.out.println(m.group(2) + m.group(4));
	}
	
	@Test
	public void test2() {
		String str = "  \t OPEN CURS_00; asdf\n";
		Pattern openCusrPattern = Pattern.compile("open\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
		Matcher m = openCusrPattern.matcher(str);
		System.out.println(m.find());
		System.out.println(m.groupCount());
		System.out.println(m.group());
	}
	
	@Test
	public void testMultiFlg() {
		
		// 正则表达式默认是贪婪匹配
		// 懒惰限定符
//		*?	重复任意次，但尽可能少重复
//				+?	重复1次或更多次，但尽可能少重复
//				??	重复0次或1次，但尽可能少重复
//				{n,m}?	重复n到m次，但尽可能少重复
//				{n,}?	重复n次以上，但尽可能少重复
		
		String str = " OPEN CURS_00; asdf\r\nasdfsafd\nOPEN CURS_01\n \tfench CURS_00\n \taaa\n";
		str+= "aaaa\n"+str;
		Pattern openCusrPattern = Pattern.compile("open\\s+(\\w+).*?open\\s+(\\w+).*?fench\\s+(\\w+)\\s*", Pattern.MULTILINE|Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
//		System.out.println(str);
		Matcher m = openCusrPattern.matcher(str);
		System.out.println(m.find());
		System.out.println(m.group(0));
		System.out.println(m.start());
		System.out.println(m.end());
//		for(int i=1; i<=m.groupCount(); i++) {
//			System.out.println(m.group(i));
//		}
		
		System.out.println(m.find());
		System.out.println(m.group(0));
		System.out.println(m.start());
		System.out.println(m.end());
		System.out.println(str.length());
		
//		for(int i=1; i<=m.groupCount(); i++) {
//			System.out.println(m.group(i));
//		}
		
	}
	
	@Test
	public void testAssert() {
		assertNotNull("aa is null", null);
	}
	
}
