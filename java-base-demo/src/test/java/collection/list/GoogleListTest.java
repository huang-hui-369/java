package collection.list;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

public class GoogleListTest {
	
	@Test
	public void testInts1() {
		// 转换成List
		List<Integer> list = Ints.asList(1,3,6,9);
		String str = Joiner.on(',').join(list);
		// 1,3,6,9
		System.out.println(str);
		assertEquals("1,3,6,9", str);
	}
	
	@Test
	public void testInts2() {
		String str = Ints.join(",",1,3,6,9);
		// 1,3,6,9
		System.out.println(str);
		assertEquals("1,3,6,9", str);
	}
	
	@Test
	public void testInts3() {
		// 数组合并
		int[] int1 = Ints.concat(new int[]{1,3,5}, new int[]{2,4,6});
		System.out.println(Ints.join(",", int1));
		
		// 最大，最小
		System.out.println(Ints.max(int1)); // 6
		System.out.println(Ints.min(int1)); // 1
		// 1,3,6,9
		
	}
	
	
}
