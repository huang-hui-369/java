package com.gut.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author huanghui
 * 
 *         针对于大的文本数据Clob的更新，和读取
 * 
 *         针对于大的二进制数据Blob（照片，语音。。。）的更新，和读取
 *
 */
public class LargeObject {

	public void updateClob() {

		try (Connection conn = JdbcUtils.getConnection();) {

			String sql = "insert into testclob(id,resume) values(?,?)";

			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, "1");

			// 从文件读取数据
			File file = new File("src/1.txt");
			try (FileReader reader = new FileReader(file);) {
				pstmt.setCharacterStream(2, reader, (int) file.length());

				int num = pstmt.executeUpdate();

				if (num > 0) {

					System.out.println("插入成功！！");

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void getClob() {

		try (Connection conn = JdbcUtils.getConnection();) {

			String sql = "select id,resume from testclob where id='1'";

			PreparedStatement pstmt = conn.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				// String resume = rs.getString("resume");

				try (Reader reader = rs.getCharacterStream("resume");
						FileWriter writer = new FileWriter("c:\\1.txt");) {

					int len = 0;

					char buffer[] = new char[1024];

					while ((len = reader.read(buffer)) > 0) {

						writer.write(buffer, 0, len);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateBlob() {

		try (Connection conn = JdbcUtils.getConnection();) {

			String sql = "insert into testblob(id,image) values(?,?)";

			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, "1");

			File file = new File("src/1.jpg");

			try (FileInputStream in = new FileInputStream(file);) {
				pstmt.setBinaryStream(2, in, (int) file.length());

				pstmt.executeUpdate();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void getBlob() {

		try (Connection conn = JdbcUtils.getConnection();) {

			String sql = "select id,image from testblob where id='1'";

			ResultSet rs = conn.prepareStatement(sql).executeQuery();

			if (rs.next()) {

				try (InputStream in = rs.getBinaryStream("image");
						OutputStream out = new FileOutputStream("c:\\1.jpg");) {

					int len = 0;
					byte buffer[] = new byte[1024];

					while ((len = in.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void oracleBlob() {
//		Oracle中大数据处理
//
//        Oracle定义了一个BLOB字段用于保存二进制数据，但这个字段并不能存放真正的二进制数据，只能向这个字段存一个指针，然后把数据放到指针所指向的Oracle的LOB段中， LOB段是在数据库内部表的一部分。
//
//        因而在操作Oracle的Blob之前，必须获得指针（定位器）才能进行Blob数据的读取和写入。
//
//        如何获得表中的Blob指针呢？ 可以先使用insert语句向表中插入一个空的blob（调用oracle的函数empty_blob()  ），这将创建一个blob的指针，然后再把这个empty的blob的指针查询出来，这样就可得到BLOB对象，从而读写blob数据了。
//
//
//    Oracle中LOB类型的处理步骤
//
//    (1)  插入空blob ——  insert into test(id,image) values(?,empty_blob());
//
//    (2)  获得blob的cursor ——  select image from test where id= ? for update;  
//
//                                                      Blob b = rs.getBlob(“image”);
//
//        注意:  须加for update，锁定该行，直至该行被修改完毕，保证不产生并发冲突。
//
//    (3)  利用 io，和获取到的cursor往数据库读写数据
	}

}
