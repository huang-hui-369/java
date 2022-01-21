package com.gut.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class CallProcedure {
	
	/**
	 * CREATE PROCEDURE demoSp (IN inputParam VARCHAR(255) ,INOUT  inOutParam varchar(255) )

      BEGIN

             SELECT CONCAT ( 'ZYXW---- ', inputParam ) into  inOutParam ;

     END
	 */
	public void demo() {
		// (1) 得到CallableStatement，并调用存储过程：

		try(	Connection conn = JdbcUtils.getConnection();
				CallableStatement cstmt = conn.prepareCall("{call demoSp(?, ?)}");) {
			
			// (2) 设置参数，注册返回值，得到输出
			// 设置in参数
			 cstmt.setString(1, "abcdefg");
			// 设置out参数
		     cstmt.registerOutParameter(2, Types.VARCHAR);    //类型参数值参见JDK中：java.sql.Types

		      cstmt.execute();

		        System.out.println(cstmt.getString(2));
		} catch (SQLException e) {
			e.printStackTrace();
		}

             







       
	}

}
