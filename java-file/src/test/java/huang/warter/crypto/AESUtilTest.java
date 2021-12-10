package huang.warter.crypto;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.RandomAccess;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Test;

import util.RandomUtils;

public class AESUtilTest {

	@Test
	public void testEncode() {
		String text = "123abc";
		String keystr = RandomUtils.getAsciiInstance().getString(32);
		String ivStr = RandomUtils.getAsciiInstance().getString(16);
		
		System.out.println("key:" + keystr);
		System.out.println("iv:" + ivStr);
		AESUtil aesutil;
		try {
			aesutil = AESUtil.getInstance(keystr.getBytes(), ivStr.getBytes());
			String encodedText = aesutil.encode(text, null);
			System.out.println("encrypt: " + encodedText);

			String decrypt = aesutil.decode(encodedText, null);
			System.out.println("decrypt:" + decrypt);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
	   
	}

	@Test
	public void testDecode() {
		fail("Not yet implemented");
	}

	@Test
	public void testEncodeFile() {
		String srcFilePath = "D:\\tmp\\out\\業者情報管理システムドキュメント.zip";
		String encoedFilePath = "D:\\tmp\\out\\業者情報管理システムドキュメント.zzz";
		String keystr = RandomUtils.getPasswordInstance().getString(32);
		String ivStr = RandomUtils.getPasswordInstance().getString(16);
		
		System.out.println("key:" + keystr);
		System.out.println("iv:" + ivStr);
		AESUtil aesutil;
		try {
			aesutil = AESUtil.getInstance(keystr.getBytes(), ivStr.getBytes());
			aesutil.encodeFile(srcFilePath, encoedFilePath);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDecodeFile() {
		String encoedFilePath = "d:/tmp/out/業者情報管理システムドキュメント.zzz";
		String destFilePath = "d:/tmp/out/業者情報管理システムドキュメント2.zip";
		String keystr = ">W63e>z*87j5^>%reQ.uMo>0fN}S^UoK";
		String ivStr = "!K$6jX:{P4fLe_%T";
		
		System.out.println("key:" + keystr);
		System.out.println("iv:" + ivStr);
		AESUtil aesutil;
		try {
			aesutil = AESUtil.getInstance(keystr.getBytes(), ivStr.getBytes());
			aesutil.decodeFile(encoedFilePath, destFilePath);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IOException e) {
			e.printStackTrace();
		}
	}

}
