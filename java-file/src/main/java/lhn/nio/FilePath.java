package lhn.nio;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.io.Files;

public class FilePath {
	
	/**
	 * @return 返回当前文件的执行路径
	 * @throws IOException 
	 */
	static Path getCurrentPath() throws IOException {
		// == Paths.get(System.getProperties().getProperty("user.dir"));
		return Paths.get(".").toRealPath();
	}
	
	/**
	 * @return user tmp path C:\Users\D2019-06\AppData\Local\Temp\
	 */
	static Path getTempPath() {
		return Paths.get(System.getProperties().getProperty("java.io.tmpdir"));
	}
	
	/**
	 * @return  user home C:/Users/user1 
	 */
	static Path getUserHome() {
		return Paths.get(System.getProperties().getProperty("user.home"));
	}
	
	/**
	 * @param path (C:\Users\D2019-06\AppData\Local\Temp\Reader.java)
	 * @return 根据文件路径，返回文件所在目录。(C:\Users\D2019-06\AppData\Local\Temp)
	 */
	static Path getFileDir(Path path) {
		return path.getParent();
	}
	
	/**
	 * @param path (C:\Users\D2019-06\AppData\Local\Temp\Reader.java)
	 * @return 根据文件路径，返回文件名不包含扩展名(Reader)
	 */
	static String getFileNameWithoutExt(String path) {
		return Files.getNameWithoutExtension(path);
	}
	
	/**
	 * @param path (C:\Users\D2019-06\AppData\Local\Temp\Reader.java)
	 * @return 根据文件路径，返回文件扩展名(java)
	 */
	static String getFileExt(String path) {
		return Files.getFileExtension(path);
	}
	
	 public static void main(String[] args) throws IOException {
		 System.out.println(FilePath.getCurrentPath());
		 Path p = Paths.get("D:\\github\\java\\java-file","FilePath.java");
		 System.out.println(FilePath.getFileDir(p));
		 System.out.println(FilePath.getFileNameWithoutExt("Reader.java"));
		 System.out.println(FilePath.getFileExt("Reader.java"));
		
	 }
	

}
