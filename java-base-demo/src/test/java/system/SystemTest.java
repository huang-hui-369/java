package system;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.google.common.base.Joiner;

public class SystemTest {

	@Test
	public void envTest() {
		Map<String, String> envMap = System.getenv();
		envMap.forEach( (k,v) -> {
			System.out.format("\"%s\":\"%s\"/n", k,v);
		});
	}
	
	@Test
	public void propTest() {
		Properties props = System.getProperties();
		String str = Joiner.on("/n").withKeyValueSeparator(":").join(props);
		System.out.println(str);
	}
	
	@Test
	public void propDirTest() {
		Properties props = System.getProperties();
		// 用户home目录
		System.out.println(props.get("user.home"));	// C:/Users/user1
		 // 用户temp目录
		System.out.println(props.get("java.io.tmpdir"));	// C:/Users/user1/AppData/Local/Temp/
		// 当前运行项目的根路径
		System.out.println(props.get("user.dir"));	// D:/work/github/java/java-base-demo
	}

}
