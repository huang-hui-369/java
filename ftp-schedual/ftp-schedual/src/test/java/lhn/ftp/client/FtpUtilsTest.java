package lhn.ftp.client;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import junit.framework.TestCase;

public class FtpUtilsTest extends TestCase {

	public void testDownloadAllFile() {
		FtpUtils ftp = new FtpUtils("127.0.0.1", 21, "test", "aaa");
		
		try {
			ftp.connect();
			ftp.downloadAllFile("/", "D:\\tmp\\aaa");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ftp.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	
	}
	
	public void testDateTime() {
		DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime start = LocalTime.parse("00:00:00", timeFormatter);
		LocalDateTime now = LocalDateTime.now();
		
		long diff = 0;
		
		if(now.toLocalTime().compareTo(start) > 0) {
			LocalDateTime s1 = LocalDateTime.of(LocalDate.now(), start).plusDays(1);
			diff = Duration.between(now, s1).getSeconds();
		} else {
			diff = Duration.between(now.toLocalTime(), start).getSeconds();
		}
		
		
		System.out.println(diff);
		
	}

}
