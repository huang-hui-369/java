package lhn.ftp.client.schedual;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lhn.ftp.client.FtpUtils;

public class DownLoadlFtpTask implements Runnable {
	
	private String taskName;
	
	private Properties conf;
	
	private String taskPrefix;
	
	private FtpUtils ftp; 
	
	private String host;
	private String port;
	private String username;
	private String password;
	private String encoding;
	private String remotepath;
	private String localpath;
	private String taskstart;
	
	private static final Logger logger = LoggerFactory.getLogger(DownLoadlFtpTask.class);
	
	public DownLoadlFtpTask(Properties conf, String taskPrefix) {
		super();
		this.conf = conf;
		this.taskPrefix = taskPrefix.trim();
	}

	public void start(ScheduledExecutorService schedul) {
		taskName = conf.getProperty(taskPrefix+".taskname").trim();
		taskstart = conf.getProperty(taskPrefix+".start").trim();
		host = conf.getProperty(taskPrefix+".ftp.host").trim();
		port = conf.getProperty(taskPrefix+".ftp.port").trim();
		username = conf.getProperty(taskPrefix+".ftp.username").trim();
		password = conf.getProperty(taskPrefix+".ftp.password").trim();
		encoding = conf.getProperty(taskPrefix+".ftp.encoding").trim();
		remotepath = conf.getProperty(taskPrefix+".ftp.remotepath").trim();
		localpath = conf.getProperty(taskPrefix+".ftp.localpath").trim();
		logger.info("task init {},{},{},{},{},{},{},{},{}" 
				,taskName
				,taskstart
				,host
				,port
				,username
				,password
				,encoding
				,remotepath
				,localpath
				);
		int iport = Integer.parseInt(port);
		ftp = new FtpUtils(host, iport, username, password);
		
		long waitSecond = waitSecond(taskstart);
		logger.info("wait {} seconds to run task", waitSecond);
		schedul.scheduleAtFixedRate(this, waitSecond, 3*3600, TimeUnit.SECONDS);
		
	}

	@Override
	public void run() {
		try {
			logger.info("task {} run", taskName);
			ftp.connect();
			ftp.downloadAllFile(remotepath, localpath);
		} catch (Exception e) {
			logger.error("download ftp failed", e);
		} finally {
			try {
				ftp.disconnect();
			} catch (IOException e) {
				logger.error("disconnect ftp failed", e);
			}
		}
		
	}
	
	private long waitSecond(String startHms) {
		
		DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime start = LocalTime.parse(startHms, timeFormatter);
		LocalDateTime now = LocalDateTime.now();
		
		long diff = 0;
		// 如果现在时刻>启动时刻，（启动时刻+1天） - 现在时刻
		if(now.toLocalTime().compareTo(start) > 0) {
			LocalDateTime s1 = LocalDateTime.of(LocalDate.now(), start).plusDays(1);
			diff = Duration.between(now, s1).getSeconds();
		} else { // 如果现在时刻<启动时刻，启动时刻 - 现在时刻
			diff = Duration.between(now.toLocalTime(), start).getSeconds();
		}
		
		return diff;
	}
	

}
