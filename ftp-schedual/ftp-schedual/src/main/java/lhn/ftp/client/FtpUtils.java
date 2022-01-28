package lhn.ftp.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
ftpClient.listFiles() 可以返回当前ftp服务器路径下的所有文件。
ftpClient.changeWorkingDirectory("pathName") 只能单级进入， pathName不能传全路径,也不能传多级目录。
ftpClient.changeToParentDirectory() 返回上一级目录。
ftpClient.printWorkingDirectory() 应该可以打印当前所在目录
ftpClient.retrieveFile(remoteFilename,os) 将ftp服务器当前目录下的 remoteFilename文件写入到 os 中。
ftpClient.storefile(fileName,is); 将输入流 is 上传到 ftp 服务器目录下的 fileName
 */

public class FtpUtils {
	
	private String hostname;
	private int port;
	private String username;
	private String password;
	private String encoding;
	
	private FTPClient ftpClient = new FTPClient();   
	
	private static final Logger logger = LoggerFactory.getLogger(FtpUtils.class);
	
	public FtpUtils(String hostName, int port, String userName, String password, String encoding) {
		super();
		this.hostname = hostName;
		this.port = port;
		this.username = userName;
		this.password = password;
		this.encoding = encoding;
	}



	public FtpUtils(String hostName, int port, String userName, String password) {
		this(hostName, port, userName, password, "utf-8");
	}
	
	public FtpUtils(String hostName, String userName, String password) {
		this(hostName, 21, userName, password, "utf-8");
	}


	

	public boolean connect() throws IOException {
		ftpClient.connect(hostname, port);     
        ftpClient.setControlEncoding(encoding);     
        if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){     
            if(ftpClient.login(username, password)){     
                logger.info("ftp conect {} {} sucessed", hostname, port);
            	return true;     
            }     
        }
        logger.info("ftp {} {} failed", hostname, port);
        disconnect();     
        return false;     
	}
	
	/**   
     * disconnect from ftp server   
     * @throws IOException   
     */    
    public void disconnect() throws IOException {     
        if(ftpClient.isConnected()){     
            ftpClient.disconnect();
            logger.info("ftp disconnect from {} {}", hostname, port);
        }     
    }     
    
    /**   
     * download a file from ftp server 
     * @param remoteFilePath  "/test/a.txt"
     * @param localFilePath   "d:/tmp/a.txt"
     * @throws IOException   
     */    
    public boolean downloadFile(String remoteFilePath,String localFilePath) throws IOException{ 
    	
    	logger.info("start download file {}", remoteFilePath);
    	
    	// PassiveMode     
        ftpClient.enterLocalPassiveMode();     
        // BINARY_FILE     
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);     
        boolean result;     
             
        // check remoteFile
        FTPFile[] ftpFiles = ftpClient.listFiles(remoteFilePath);     
        if(ftpFiles.length != 1) {
        
        	logger.info("file {} not exist in ftp server", remoteFilePath);     
            return false;   
        }     
             
        long remoteFileSize = ftpFiles[0].getSize(); 
        File localFile = new File(localFilePath); 
        // if local file exists download start local file size
        if(localFile.exists()){     
            long localFileSize = localFile.length();     
            //      
            if(localFileSize >= remoteFileSize){     
            	logger.info("local file exists abort download file");     
                return false;     
            }     
              
            
            FileOutputStream out = new FileOutputStream(localFile,true);     
            ftpClient.setRestartOffset(localFileSize);  
            result = ftpClient.retrieveFile(remoteFilePath, out);
            out.close();
        } else {
        	FileOutputStream out = new FileOutputStream(localFile); 
        	result = ftpClient.retrieveFile(remoteFilePath, out);
            out.close();
        }
               
        logger.info("suceed download and save file to {}", localFilePath);
        return result;     
    }   
    
    /**
     * @param remotePathStr "/test"
     * @param localPathStr "d:/tmp"
     * @throws IOException
     */
    public void downloadAllFile(String remotePathStr, String localPathStr) throws IOException {
    	logger.info("start download all file from {}", remotePathStr);
    	FTPFile[] ftpFiles = null;
    	try {
    		
    		// get all file path list
    		ftpFiles = ftpClient.listFiles(remotePathStr);
    		
    		String localFilePath = null;
        	String ftpFilePath = null;
        	String ftpFileName = null;
        	for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
    	    	FTPFile ftpFile = ftpFiles[i];
    	    	ftpFileName = ftpFile.getName();
    	    	
    	    	if (ftpFile.isFile()) {
    	    		// build remote file path
    	    		ftpFilePath = buildRemotePath(remotePathStr , ftpFileName);
    		    	// build local file path
    	    		localFilePath = Paths.get(localPathStr, ftpFile.getName()).toString();
    		    	logger.info("download file : "+ ftpFilePath);
    		    	// ダウンロードファイル
    		    	try {
    		    		downloadFile(ftpFilePath, localFilePath);
    		    	} catch(Exception e) {
    		    		logger.error("download file {} failed", localFilePath);
    		    		logger.error("", e);
    		    	}
    		    	
    	    	}else if(ftpFile.isDirectory()){
    	    		// is folder
    	    		Path localFolderPath = Paths.get(localPathStr, ftpFileName);
    	    		String remotePath = buildRemotePath(remotePathStr, ftpFileName);
    	    		// mkdir for local folder
    	    		if(!localFolderPath.toFile().mkdir()) {
    	    			logger.info("create folder failed : "+ localFolderPath.toString());
    	    		}
    	    		
    	    		logger.info("process folder : "+ remotePath);
    	    		// 
    	    		downloadAllFile(remotePath, localFolderPath.toString());
    		    	
    	    	}
    	    	
        	}
        	
        	logger.info("succed download all file to {}", localPathStr);
        	
    	} catch (Exception e) {
    		logger.error("download all file from {} failed", remotePathStr);
    		logger.error("", e);
    	}
    	
    }
    
    private String buildRemotePath(String oldpath, String filepath) {
    	String newpath;
    	if(oldpath.endsWith("/")) {
    		newpath =  oldpath + filepath;
    	} else {
    		newpath =  oldpath + "/" + filepath;
    	}
    	
    	return newpath;
    }

}
