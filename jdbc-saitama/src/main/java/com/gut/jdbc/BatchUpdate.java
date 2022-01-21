package com.gut.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * @author huanghui
 * 
 *         实现批处理有两种方式，
 * 
 * 
 * 
 *         第一种方式： Statement.addBatch(sql) （其实是将sql语句 放在了一个 list 集合中。）
 * 
 * 
 * 
 *         第二种方式： PreparedStatement.addBatch() （其实是将sql语句 放在了一个 list 集合中。）
 * 
 * 
 * 
 *         执行批处理SQL语句
 * 
 * 
 * 
 *         executeBatch()方法：执行批处理命令
 * 
 * 
 * 
 *         clearBatch()方法：清除批处理命令（实际上是清除 List集合中的SQL语句，否则会造成内存溢出。）
 *
 */
public class BatchUpdate {

	public void updateBatchByStmt() {
		try (Connection conn = JdbcUtils.getConnection(); Statement stmt = conn.createStatement();) {

			String sql1 = "insert into user(name,password,email,birthday)  values('kkk','123','abc@sina.com','1978-08-08')";

			String sql2 = "update user set password='123456' where id=3";

			stmt.addBatch(sql1); // 把SQL语句加入到批命令中

			stmt.addBatch(sql2); // 把SQL语句加入到批命令中

			stmt.executeBatch();

			stmt.clearBatch();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateBatchByPstmt() {
		
		String sql = "insert into user(name,password,email,birthday) values(?,?,?,?)";
		
		try (Connection conn = JdbcUtils.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql);) {

		        for(int i=0;i<50000;i++){

		        	pstmt.setString(1, "aaa" + i);

		        	pstmt.setString(2, "123" + i);

		        	pstmt.setString(3, "aaa" + i + "@sina.com");

		        	pstmt.setDate(4,Date.valueOf(LocalDate.of(1980, 1, 1)));

		        	pstmt.addBatch();

		                if(i%1000==0){      //为防止(list集合) 内存溢出：设定每累加1000条数据就向数据库发送一次
		                	pstmt.executeBatch();
		                	pstmt.clearBatch();

		                }
		        }
		        pstmt.executeBatch(); //当剩余的条数小于1000条时就不会被发送到数据库，所以此处要在发送一次。

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
