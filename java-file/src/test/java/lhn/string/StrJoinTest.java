package lhn.string;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.junit.Test;

import com.google.common.base.Joiner;

public class StrJoinTest {

	@Test
	public void testStrJoin() {
		String str = String.join("-", "column1", "column2", "column3");
		// column1-column2-column3
		System.out.println(str);
		
	}
	
	@Test
	public void testJoin1() {
		StringJoiner strJoiner = new StringJoiner(",");
		for(int i=0; i<6;i++) {
			strJoiner.add("column" + i);
		}
		String str = strJoiner.toString();
		// column0,column1,column2,column3,column4,column5
		System.out.println(str);
		assertEquals("column0,column1,column2,column3,column4,column5", str);
	}
	
	@Test
	public void testJoin2_full() {
		StringJoiner strJoiner = new StringJoiner(",","[","]");
		for(int i=0; i<6;i++) {
			if(i==3) {
				// add "null"
				strJoiner.add(null);
				continue;
			}
			strJoiner.add("column" + i);
		}
		String str = strJoiner.toString();
		// [column0,column1,column2,null,column4,column5]
		System.out.println(str);
		assertEquals("[column0,column1,column2,null,column4,column5]", str);
	}
	
	@Test
	public void join_emptyValue1() {
		StringJoiner strJoiner = new StringJoiner(",","[","]");
		String str = strJoiner.toString();
		// []
		System.out.println(str);
		assertEquals("[]", str);
	}
	
	@Test
	public void join_emptyValue2() {
		StringJoiner strJoiner = new StringJoiner(",","[","]");
		strJoiner.setEmptyValue("null");
		String str = strJoiner.toString();
		// null
		System.out.println(str);
		assertEquals("null", str);
	}
	
}
