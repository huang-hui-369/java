package huang.river.nio;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import huang.river.nio.WatchDir;

public class WatchDirTest {

	@Test
	public void test() {
		boolean recursive = true;
        // register directory and process its events
        Path dir = Paths.get(System.getProperty("user.dir"));
        try {
			new WatchDir(dir, recursive).processEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
