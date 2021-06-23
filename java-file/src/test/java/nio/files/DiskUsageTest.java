package nio.files;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

public class DiskUsageTest {

	long M = 1024*1024;
	
	long G = M*1024;
	
	@Test
	public void fileStoreTest() {
		Path targt = Paths.get("d:/");
		try {
			FileStore fs = Files.getFileStore(targt);
			long totalSpace = fs.getTotalSpace();
			long unUsedSpace = fs.getUnallocatedSpace();
			long usedSapce = totalSpace - unUsedSpace;
			long usableSpce = fs.getUsableSpace();
			
			Map<String, String> infos = Maps.newLinkedHashMap();
			infos.put("totalSpace", String.format("%d G", totalSpace/G));
			infos.put("unUsedSpace", String.format("%d G", unUsedSpace/G));
			infos.put("usedSpace", String.format("%d G", usedSapce/G));
			infos.put("usableSpace", String.format("%d G", usableSpce/G));
			
			String infoStr = Joiner.on("\n").withKeyValueSeparator(":").join(infos);
			System.out.println(infoStr);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
