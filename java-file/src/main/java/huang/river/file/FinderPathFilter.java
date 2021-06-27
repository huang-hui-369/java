package huang.river.file;

import java.nio.file.Path;

public interface FinderPathFilter {
	boolean isMatch(Path p);
}
