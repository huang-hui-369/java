package com.gut.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetGeneratedKey {
	
	public void mysqlDemo() {
		
		String sql = "insert into user(name,password,email,birthday)  values('abc','123','abc@sina.com','1978-08-08')";
		
		try(Connection conn = JdbcUtils.getConnection();
				// 設置返回生成的自动主键
				PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS ); 	) {
			
			pstmt.executeUpdate();

	        ResultSet rs = pstmt.getGeneratedKeys();  // 得到插入行的主键

	        if(rs.next()) {
	             System.out.println("插入行的主键:" + rs.getObject(1));
	        }

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	
	}
		

		 

}
