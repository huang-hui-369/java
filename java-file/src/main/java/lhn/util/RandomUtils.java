package lhn.util;

import java.util.Random;

public class RandomUtils {
	
	private char[] initCharArray;
		
	private RandomUtils(InitCharArray init) {
		this.initCharArray = init.init();
	}
	
	
	public static RandomUtils getAsciiInstance() {
		return new RandomUtils(new AsciiInit());
	}
	
	public static RandomUtils getPasswordInstance() {
		return new RandomUtils(new PasswordInit());
	}
	
		
	public String getString(int len) {
		char[] charArray = getCharArray(len);
		return new String(charArray);
	}


	
	public byte[] getByteArray(int len) {
		char[] charArray = getCharArray(len);
		byte[] byteArray = new byte[len];
		
		for(int i=0; i<len; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		
		return byteArray;
	}

	/**
	 * 返回len长度的char数组，每一个char都在 ascii的 !（33） ~ }（125）之间
	 * @param len
	 * @return 
	 */
	public char[] getCharArray(int len) {
		char[] charArray = new char[len];
		Random random = new Random();
		
		for(int i=0; i<len; i++) {
			charArray[i] = initCharArray[random.nextInt(initCharArray.length)];
		}
		return charArray;
		
	}
   
}

interface InitCharArray {
	 char[] init();
}

class AsciiInit implements InitCharArray {

	// 33:! ~ 125:}
	
	@Override
	public char[] init() {
		char[] AsciiArray = new char[92];
		for(int i=0; i<92; i++) {
			AsciiArray[i] = (char) ('!'+i);
		}
		return AsciiArray;
	}
	
}

class PasswordInit implements InitCharArray {
	
	final char[] specArray = { '!', '#', '$', '%', '&', '(', ')', '*', '+', '-', 
			                   '_', '.', ':', ';', '<', '=', '>', '?', '@', '[',
			                   ']', '^', '_', '{', '|', '}'
			                  };
	final int digitalLen = 10;
	final int alphbetLen = 26*2;

	@Override
	public char[] init() {
		
		char[] asciiArray = new char[specArray.length+digitalLen+alphbetLen];
		
		System.arraycopy(specArray, 0, asciiArray, 0, specArray.length);
		int n = specArray.length;
		
		// * 数字		 0 -- 9   30 -- 39
		for(int i=0;i<10;i++) {
			asciiArray[n++] = (char)('0' + i);
		}
		
		// * 大写字母	 A -- Z   41 -- 5A
		for(int i=0;i<26;i++) {
			asciiArray[n++] = (char)('A' + i);
		}
		
		// * 小写字母	 a -- z   61 -- 7A
		for(int i=0;i<26;i++) {
			asciiArray[n++] = (char)('a'  + i);
		}
		
		return asciiArray;
	}
	
}

