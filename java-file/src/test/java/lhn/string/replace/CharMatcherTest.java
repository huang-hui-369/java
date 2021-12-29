package lhn.string.replace;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.CharMatcher;

public class CharMatcherTest {
	
	@Test
	// �հ��滻
	public void testRelpaceWhiteSpace() {
		// ��WhiteSpace���ո�\r,\n,\t ��Ӧ����һ��#��һ��һ��)
		String strWithWhiteSpace = "hello world\r\r\ryou are here\n\ntake it\t\t\teasy";
		// breakingWhitespace �Ǡ����g���~�Z��Whitespace
		// CharMatcher.whitespace() whitespace����breakingWhitespace
		String str = CharMatcher.breakingWhitespace().replaceFrom(strWithWhiteSpace,'#');
		// hello#world###you#are#here##take#it###easy
		System.out.println(str); 
		assertEquals("hello#world###you#are#here##take#it###easy", str);
	}
	
	@Test
	// �����հ�����һ���ַ�
	public void testRelpaceWhiteSpace2() {
		// ��WhiteSpace���ո�\r,\n,\t ��Ӧ����һ��#��һ��һ��)
		String strWithWhiteSpace = "hello world\r\r\ryou are here\n\ntake it\t\t\teasy";
		// breakingWhitespace �Ǡ����g���~�Z��Whitespace
		// CharMatcher.whitespace() whitespace����breakingWhitespace
		String str = CharMatcher.breakingWhitespace().collapseFrom(strWithWhiteSpace,'#');
		// hello#world###you#are#here##take#it###easy
		System.out.println(str); 
		assertEquals("hello#world#you#are#here#take#it#easy", str);
	}
	
	// ȥ��ǰ��հ׺�����һ���ַ�
	@Test
	public void testRelpaceWhiteSpaceTrim() {
		// ��WhiteSpace���ո�\r,\n,\t ��Ӧ����һ��#��һ��һ��)
			String strWithWhiteSpace = " \thello world\r\r\ryou are here\n\ntake it\t\t\teasy \n";
			// breakingWhitespace �Ǡ����g���~�Z��Whitespace
			// CharMatcher.whitespace() whitespace����breakingWhitespace
			String str = CharMatcher.breakingWhitespace().trimAndCollapseFrom(strWithWhiteSpace,'#');
			// hello#world###you#are#here##take#it###easy
			System.out.println(str); 
			assertEquals("hello#world#you#are#here#take#it#easy", str);
	}
	
	// ��������
	@Test
	public void testRetainDigital() {
		// ��WhiteSpace���ո�\r,\n,\t ��Ӧ����һ��#��һ��һ��)
		String strWithWhiteSpace = "123hello world\r\r\ryou 45are here\n\ntake it\t\t\teasy \n678";
		// breakingWhitespace �Ǡ����g���~�Z��Whitespace
		// CharMatcher.whitespace() whitespace����breakingWhitespace
		String str = CharMatcher.ascii().inRange('0', '9').retainFrom(strWithWhiteSpace);
		// hello#world###you#are#here##take#it###easy
		System.out.println(str); 
//		assertEquals("hello#world#you#are#here#take#it#easy", str);
	}
	
	

}
