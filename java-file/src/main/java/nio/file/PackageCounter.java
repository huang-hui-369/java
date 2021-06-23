package nio.file;

import java.nio.file.Path;
import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class PackageCounter {

	// filename: 
	Multimap<String, Collection<CounterResult>> funcMap = ArrayListMultimap.create();
	
	public void process (String path) {
		
	}
	
	
	public void process1file(Path fpath) {
		
	}
	
	public class CounterResult {
		String funcName;
		int lineNum;
	}
}
