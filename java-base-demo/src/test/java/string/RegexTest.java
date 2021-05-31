package string;

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
}
