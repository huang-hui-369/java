package lhn.util;

import static org.junit.Assert.*;

import org.junit.Test;

import lhn.util.RandomUtils;

public class RandomUtilsTest {

	@Test
	public void testGetRandomAscii() {
		RandomUtils random = RandomUtils.getPasswordInstance();
		System.out.println(random.getString(32));
		System.out.println(random.getCharArray(16));
		
		
//		StringBuffer sb = new StringBuffer();
//		// * 数字		 0 -- 9   30 -- 39
//		for(int i=0;i<10;i++) {
//			sb.append((char)('0' + i));
//		}
//		
//		// * 大写字母	 A -- Z   41 -- 5A
//		for(int i=0;i<26;i++) {
//			sb.append((char)('A' + i));
//		}
//		
//		// * 小写字母	 a -- z   61 -- 7A
//		for(int i=0;i<26;i++) {
//			sb.append( (char)('a'  + i));
//		}
//		
//		// 符号		 -_@ 
//		sb.append('-');
//		sb.append('_');
//		sb.append('@');
//		sb.append('#');
//		sb.append('!'); 
//		sb.append('%'); 
//		sb.append('&'); 
//		sb.append('*'); 
//		sb.append('?');  
//		
//		System.out.println(sb.toString());
		 
		
		
	}

}
