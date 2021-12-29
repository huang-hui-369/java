package lhn.string;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

public class Map2Str {

	@Test
	public void testMap2Str() {
		Map<String, String> testMap = Maps.newLinkedHashMap();
        testMap.put("col1", "v1");
        testMap.put("col2", "v2");
        testMap.put("col3", "v3");
        String str = Joiner.on(",").withKeyValueSeparator(":").join(testMap);
        // col1:v1,col2:v2,col3:v3
        System.out.println(str);
        assertEquals("col1:v1,col2:v2,col3:v3", str);
 	}
	
	@Test
	public void testStr2Map() {
		String str = "col1:v1,col2:v2,col3:v3";
		Splitter.MapSplitter ms = Splitter.on(",").withKeyValueSeparator(':');
        Map<String, String> map = ms.split(str);
        for (String k : map.keySet()) {
            System.out.println(k + " -> " + map.get(k));
        }
        Map<String, String> eMap = Maps.newLinkedHashMap();
        eMap.put("col1", "v1");
        eMap.put("col2", "v2");
        eMap.put("col3", "v3");
        
        assertTrue(eMap.equals(map));
	}

}
