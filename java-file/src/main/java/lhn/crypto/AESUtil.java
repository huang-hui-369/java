package lhn.crypto;

import javax.annotation.Nullable;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 分组密码体制: 将明文切成一段一段的来加密,并且每一段为16字节，不够16字节就填充 
 * 
 * Padding： 有三种模式PKCS5、PKCS7和NOPADDING.  
 * 
 * 初始向量IV： 长度为128位16个字节，第一段数据是根据初始向量来加密, 后面每段数据的加密向量都是前面一段的密文. 
 * 
 * 密钥： 长度可以是128位16个字节、192位或者256位, 位数越高, 加密强度自然越大, 但是加密的效率会低,每一段数据都是根据这个密钥来加密。
 * 
 * 加密模式： 一共有四种加密模式, ECB(电子密码本模式)、CBC(密码分组链接模式)、CFB、OFB, 我们一般使用的是CBC模式.
 *         ECB模式只用到密钥来加密，所以相同的明文总是得到相同的密文。
 *         其他三种模式差别不大，都是用初始向量和密钥来加密。
 * 
 **/
public class AESUtil {

	 /**
     * 加密解密算法
     */
    private static final String ALGORITHM = "AES";
	
    /**
     * 默认密钥, 256位32个字节
     */
    public static final byte[] DEFAULT_SECRET_32KEY = "uBdUx82vPHkDKb284d7NkjFoNcKWBuka".getBytes();
    
    /**
     * 默认密钥, 128位16个字节
     */
    public static final byte[] DEFAULT_SECRET_16KEY = "uBdUx82vPHkDKb28".getBytes();

   
    /**
     * 初始向量IV, 初始向量IV的长度规定为128位16个字节, 初始向量的来源为随机生成.
     */
    private static final byte[] DEFAULT_IV = "c558Gq0YQK2QUlMc".getBytes();

    /**
     * 加密解密算法/加密模式/填充方式
     */
    private static final String CIPHER_ALGORITHM_DETAIL = "AES/CBC/PKCS5Padding";

    private static java.util.Base64.Encoder base64Encoder = java.util.Base64.getEncoder();
    private static java.util.Base64.Decoder base64Decoder = java.util.Base64.getDecoder();

    static {
        java.security.Security.setProperty("crypto.policy", "unlimited");
    }
    
    private byte[] key;
    private byte[] iv;
    private SecretKey secretKey;
    private Cipher cipher;
    
    public AESUtil(byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException {
    	this(key, DEFAULT_IV);
    }
    
  
    public AESUtil(byte[] key, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException {
    	assert key != null ;
    	assert (key.length ==16 || key.length ==24 || key.length ==32)
    	   : String.format("parameter key.length(%d) error", key.length);
    	assert iv != null ;
    	assert iv.length == 16 : String.format("parameter iv.length(%d) error", iv.length);
    	this.key = key;
    	this.iv = iv;
    	// 根据密钥和算法生成密钥对象
    	this.secretKey = new SecretKeySpec(key, ALGORITHM);
    	// 根据加密算法，加密模式，填充方式来得到一个加密器
    	this.cipher = Cipher.getInstance(CIPHER_ALGORITHM_DETAIL);
    	
    }
    
    public static AESUtil  getInstance(byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException {
    	return new AESUtil(key);
    }
 
    
    public static AESUtil  getInstance(byte[] key, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException {
    	return new AESUtil(key, iv);
    }

    /**
     * 
     * @param content 要加密的字符串,默认为utf-8编码
     * @param key     密钥
     * @param iv      初始化向量
     * @return        加密后的字符串
     * @throws InvalidAlgorithmParameterException 
     * @throws InvalidKeyException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */
    public String encode(String content, @Nullable Charset charset) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    	 Charset useCharset = StandardCharsets.UTF_8;
         if(charset !=null) {
         	useCharset = charset;
         }
         
    	// 根据密钥对象，初始向量，初始化加密器
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(this.iv));

        // 获取加密内容的字节数组
        byte[] byteEncode = content.getBytes(useCharset);

        // 使用加密器来加密字节数组
        byte[] byteAES = cipher.doFinal(byteEncode);

        // 将加密后的数据再用base64加密
        return base64Encoder.encodeToString(byteAES);
        
    }

    /**
     * AES解密
     * @throws InvalidAlgorithmParameterException 
     * @throws InvalidKeyException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */
    public String decode(String content, @Nullable Charset charset) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
       Charset useCharset = StandardCharsets.UTF_8;
        if(charset !=null) {
        	useCharset = charset;
        }
        
        // 根据密钥对象，初始向量，初始化加密器
    	cipher.init( Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(this.iv) );

        // 将加密并编码后的内容解码成字节数组
        byte[] byteContent = base64Decoder.decode(content);
        // 解密
        byte[] byteDecode = cipher.doFinal(byteContent);
        
        return new String(byteDecode, useCharset);
    }
    
    
    public void encodeFile(String filePath, String encodedFilePath) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException {
    	
    	enDecodeFile(filePath, encodedFilePath, Cipher.ENCRYPT_MODE);
         
    }
    
    public void decodeFile(String endodedFilePath, String destFilePath) throws InvalidKeyException, InvalidAlgorithmParameterException, IOException {
    	 
    	enDecodeFile(endodedFilePath, destFilePath, Cipher.DECRYPT_MODE);
    	
    }
    
    private void enDecodeFile(String srcFilePath, String destFilePath, int cryptMode ) throws InvalidKeyException, InvalidAlgorithmParameterException, IOException {
	  	
    	/* 打开加密文件 */
		 File srcFile = checkFile(srcFilePath, false);
		 /* 创建解密文件 */
		 File destFile = checkFile(destFilePath, true);
		 
		// 根据密钥对象，初始向量，初始化加密器
		 cipher.init(cryptMode, secretKey, new IvParameterSpec(iv));
		    
		 
		 try(InputStream in = new FileInputStream(srcFile);
			     OutputStream out = new FileOutputStream(destFile);
			     /*  encode by CipherOutputStream  */	 
			     CipherOutputStream cout = new CipherOutputStream(out, cipher);) {
			 
		     byte[] buffer = new byte[1024];
		     int nRead = 0;
		     while ((nRead = in.read(buffer)) != -1) {
		         cout.write(buffer, 0, nRead);
		         cout.flush();
		     }
		}       
   }
    
    private File checkFile(String filepath, boolean isCreate) throws IOException {
    	 File file = new File(filepath);
    	 
    	 if ( !file.exists() || !file.isFile() ) {
    		 if(isCreate) {
    			 // create file folder
    			 if (!file.getParentFile().exists()) {
    				 file.getParentFile().mkdirs();
    		     }
    			 // create file
    			 file.createNewFile();
        	 } else {
        		 throw new IllegalArgumentException(String.format("文件不存在{%0}", filepath));
        	 }
    	 }
	         
		 return file;
 			
    }
    

    public static void main(String[] args) {
    }

}
