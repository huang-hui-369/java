package lhn.string;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.junit.Test;

import com.google.common.base.Joiner;

public class StrJoinGoogleTest {

	String str = String.join("-", "column1", "column2", "column3");
	
	@Test
	public void googleJoinStr() {
		String str = Joiner.on("-").join("column1", "column2", "column3");
		// column1-column2-column3
		System.out.println(str);
		assertEquals("column1-column2-column3", str);
	}
	
	@Test
	public void googleJoinUseNull() {
		List<String> strList =new ArrayList<String>();
		for(int i=0; i<6;i++) {
			if(i==3) {
				// add "null"
				strList.add(null);
				continue;
			} 
			strList.add("column" + i);
		}
		Joiner strJoiner = Joiner.on(",").useForNull("null");
			String str = strJoiner.join(strList);
		// [column0,column1,column2,null,column3,column4,column5]
		System.out.println(str);
		assertEquals("column0,column1,column2,null,column4,column5", str);
	}
	
	@Test
	public void googleJoinSkipNull() {
		List<String> strList =new ArrayList<String>();
		for(int i=0; i<6;i++) {
			if(i==3) {
				// add "null"
				strList.add(null);
				continue;
			} 
			strList.add("column" + i);
		}
		Joiner strJoiner = Joiner.on(",").skipNulls();
			String str = strJoiner.join(strList);
		// [column0,column1,column2,null,column3,column4,column5]
		System.out.println(str);
		assertEquals("column0,column1,column2,column4,column5", str);
	}
	
}
