package com.gut.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FetchSize {
	
	
	
	/**
	 * @param fetchSize
	 * 
	 * java端从数据库读取100W数据进行后台业务处理。

　　1： 分页读取出来。缺点：需要排序后分页读取，性能低下。

　　2： 利用 cusor讀取， jdbc中有个重要的参数fetchSize 建立长连接，利用服务端游标，一組一組流式返回给java client 端。

	pstmt.setFetchSize(0); 程序自己設置合適的FetchSize
 
	 * 
	 */
	public static void getAll(int fetchSize) {
        try {
            long beginTime=System.currentTimeMillis();
            int totalCount=0;
            
            String sql = "select * from test";
            
            try( Connection conn = JdbcUtils.getConnection();
            		PreparedStatement pstmt = conn.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);) {
            	 
            	conn.setAutoCommit(false); //为了设置fetchSize,必须设置为false
                 pstmt.setFetchSize(fetchSize);
                
                 try(ResultSet rs = pstmt.executeQuery();) {
                	
                     while (rs.next()) {
                             totalCount++;
                     }
                 }
                
            }
            long endTime=System.currentTimeMillis();
            System.out.println("totalCount:"+totalCount+";fetchSize:"+fetchSize+";耗时:"+(endTime-beginTime)+"ms");
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

}
