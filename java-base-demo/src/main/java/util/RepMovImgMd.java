package util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;

public class RepMovImgMd {
	
	/**
	 * 查找指定path下的md文件，不递归查找子目录下的。
	 * 在md文件中
	 * 查找 ![](../../img/xxx.png) 
	 * 改为 --> ![](img/xxx.png) 
	 * 同时移动 ../../img/xxx.png
	 * 到 --> img/xxx.png 下
	 * @param path 
	 */
	public void process(String path) {
		Path dir = Paths.get(path);
		// 1. 查找md文件
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.md")) {
			for (Path entry : stream) {
				System.out.format("process file [%s] start\n", entry.toAbsolutePath().toString());
				processMd(entry);
				System.out.format("process file [%s] end\n", entry.toAbsolutePath().toString());
			}
		} catch (IOException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can // only be thrown by newDirectoryStream.
			System.err.println(x);
		}
		
	}
	
	private void processMd(Path md) {
		Path dest = md;
		Path basep = md.getParent();
		Path src = Paths.get(md.toAbsolutePath().toString() + ".bak");
		try {
			// 2. 备份xxx.md文件 --> xxx.md.bak
			Files.move(md, src, StandardCopyOption.REPLACE_EXISTING );
			// 读出所有行
			 List<String> lineList = Files.readAllLines(src);
			 // 写的行
			 Map<Integer,String> newLineMap = Maps.newLinkedHashMap();
			 String newLine = null;
			// 处理每一行
			 for(int i=0; i<lineList.size(); i++ ) {
				// 替换一行
				 newLine = processLine(lineList.get(i), basep);
				 if(newLine!=null) {
					 newLineMap.put(i, newLine);
					 System.out.format("replace  [%d %s]\n",i, newLine);
				 } 
			 }
			// 6. 新的内容写入文件
			 if(newLineMap.size()>0) {
				 newLineMap.forEach((idx,line) -> {
					 lineList.set(idx, line);
				 });
				 Files.write(dest, lineList, StandardOpenOption.CREATE_NEW);
			 }
				 
			// 7. 删除备份md文件
//			 Files.deleteIfExists(dest);
		} catch (IOException e) {
			e.printStackTrace(System.err);
//			try {
//				Files.copy(dest, md);
//				Files.deleteIfExists(dest);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
		}
	}
	
	
	private String processLine(String line, Path basep) throws IOException {
		
		String txt = line.replaceAll("\\\\", "/");
		String ret = null;
		// 3. 查找 ![](../../img/xxx.png)
		Pattern imgPattern2 = Pattern.compile("(!\\[\\]\\()\\s*((\\.\\.[/\\\\])+)(img[/\\\\](\\d+-)+\\d+\\.(png|jpg))\\)");
		Matcher m = imgPattern2.matcher(txt);
		if(m.find()) {
			System.out.format("found [%s]\n", m.group(0));
			// group(0) ![](../../img/2020-10-13-14-57-46.png)
			// ![](
			// ../../
			// ../
			// img/2020-10-13-14-57-46.png
			String img = m.group(2) + m.group(4);
			Path imgSrcPath = basep.resolve(img).normalize();
			if(Files.exists(imgSrcPath)) {
				// 4. move ../../img/xxx.png ./img/xxx.png
				Path imgDirPath = basep.resolve("img");
				// 确定../../img/xxx.png文件存在
				if(Files.notExists(imgDirPath)) {
					Files.createDirectories(imgDirPath);
				}
				Path imgFilePath = basep.resolve(m.group(4));
				Files.move(imgSrcPath, imgFilePath, StandardCopyOption.REPLACE_EXISTING);
				// 5. 修改 --> ![](img/xxx.png)
				ret = m.replaceAll( m.group(1) + m.group(4) + ")" );
			}
		}
		return ret;
	}

}
