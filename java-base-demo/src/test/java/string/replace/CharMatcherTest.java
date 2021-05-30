package string.replace;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.CharMatcher;

public class CharMatcherTest {
	
	@Test
	// 空白替换
	public void testRelpaceWhiteSpace() {
		// 將WhiteSpace（空格，\r,\n,\t 对应换成一个#，一对一换)
		String strWithWhiteSpace = "hello world\r\r\ryou are here\n\ntake it\t\t\teasy";
		// breakingWhitespace 是爲了間隔詞語的Whitespace
		// CharMatcher.whitespace() whitespace包含breakingWhitespace
		String str = CharMatcher.breakingWhitespace().replaceFrom(strWithWhiteSpace,'#');
		// hello#world###you#are#here##take#it###easy
		System.out.println(str); 
		assertEquals("hello#world###you#are#here##take#it###easy", str);
	}
	
	@Test
	// 连续空白缩成一个字符
	public void testRelpaceWhiteSpace2() {
		// 將WhiteSpace（空格，\r,\n,\t 对应换成一个#，一对一换)
		String strWithWhiteSpace = "hello world\r\r\ryou are here\n\ntake it\t\t\teasy";
		// breakingWhitespace 是爲了間隔詞語的Whitespace
		// CharMatcher.whitespace() whitespace包含breakingWhitespace
		String str = CharMatcher.breakingWhitespace().collapseFrom(strWithWhiteSpace,'#');
		// hello#world###you#are#here##take#it###easy
		System.out.println(str); 
		assertEquals("hello#world#you#are#here#take#it#easy", str);
	}
	
	// 去掉前后空白和缩成一个字符
	@Test
	public void testRelpaceWhiteSpaceTrim() {
		// 將WhiteSpace（空格，\r,\n,\t 对应换成一个#，一对一换)
			String strWithWhiteSpace = " \thello world\r\r\ryou are here\n\ntake it\t\t\teasy \n";
			// breakingWhitespace 是爲了間隔詞語的Whitespace
			// CharMatcher.whitespace() whitespace包含breakingWhitespace
			String str = CharMatcher.breakingWhitespace().trimAndCollapseFrom(strWithWhiteSpace,'#');
			// hello#world###you#are#here##take#it###easy
			System.out.println(str); 
			assertEquals("hello#world#you#are#here#take#it#easy", str);
	}
	
	// 保留数字
	@Test
	public void testRetainDigital() {
		// 將WhiteSpace（空格，\r,\n,\t 对应换成一个#，一对一换)
		String strWithWhiteSpace = "123hello world\r\r\ryou 45are here\n\ntake it\t\t\teasy \n678";
		// breakingWhitespace 是爲了間隔詞語的Whitespace
		// CharMatcher.whitespace() whitespace包含breakingWhitespace
		String str = CharMatcher.ascii().inRange('0', '9').retainFrom(strWithWhiteSpace);
		// hello#world###you#are#here##take#it###easy
		System.out.println(str); 
//		assertEquals("hello#world#you#are#here#take#it#easy", str);
	}
	
	

}
