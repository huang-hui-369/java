package lhn.ftp.client.schedual;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SchedualManager {
	
	private static final String confFilePath = "ftpDownloadTask.properties";
	
	private static Properties conf = null;
	
	private static String[] tasks = null;
	
	private static List<DownLoadlFtpTask> taskList = new ArrayList<DownLoadlFtpTask>();
	
	private static final Logger log = LoggerFactory.getLogger(DownLoadlFtpTask.class);
	
	static {
		// read conf
		InputStream confInput = DownLoadlFtpTask.class.getClassLoader().getResourceAsStream(confFilePath);
		conf = new Properties();
		try {
			conf.load(confInput);
		} catch (IOException e) {
			log.error("read config " + confFilePath + "failed ", e);
		}
		tasks = conf.getProperty("task").split(",");
		
	}
	
	
	public static void start() {
		
		ScheduledExecutorService shcedualService =  Executors.newScheduledThreadPool(tasks.length);
		
		for(String task : tasks) {
			DownLoadlFtpTask ftptask = new DownLoadlFtpTask(conf, task);
			ftptask.start(shcedualService);
			taskList.add(ftptask);
		}
		
	}
	

}
