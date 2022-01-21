package com.gut.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

public class MssqlConnectionDemo {
	
	
	/**
	 *
	 * 只要Driver类一加载，Driver类对象就被注册到DriverManager中了
	 * 
	 * 参照 SQLServerDriver的源代码 有 static 语句块 
	 * 
	 * 
	 *  // Register with the DriverManager
	    static {
	        try {
	            register();
	        } catch (SQLException e) {
	            if (drLogger.isLoggable(Level.FINER) && Util.isActivityTraceOn()) {
	                drLogger.finer("Error registering driver: " + e);
	            }
	        }
	    }
	 * 
	 *  public static void register() throws SQLException {
	        if (!isRegistered()) {
	            mssqlDriver = new SQLServerDriver();
	            DriverManager.registerDriver(mssqlDriver);
	        }
	    }
	 * 
	
	 * @return Connection
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	static Connection getConnection1ByDriverClass() throws ClassNotFoundException, SQLException {
		// url：
		String url = "jdbc:sqlserver://localhost:1433;databaseName=saitama";
		// 数据库用户名
		String user = "SAI16";
		// 数据库密码
		String password = "SAI16";

		// 1. 加载Driver类，Driver类对象将自动被注册到DriverManager类中
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		// 2. 连接数据库，返回连接对象
		Connection conn = DriverManager.getConnection(url, user, password);
		
		return conn;
	}
	
	static Connection getConnection2ByDriverManager() throws SQLException {
		// 参数：
		// jdbc协议:postgresql子协议://主机地址:数据库端口号/要连接的数据库名
		String url = "jdbc:sqlserver://localhost:1433;databaseName=saitama";
		// 数据库用户名
		String user = "SAI16";
		// 数据库密码
		String password = "SAI16";

		// 1.  创建驱动程序类对象
		Driver driver = new SQLServerDriver();

		// 2. 注册驱动程序（可以注册多个驱动程序）
		DriverManager.registerDriver(driver); // 可以省略吧

		// 3. 连接数据库，返回连接对象
		Connection conn = DriverManager.getConnection(url, user, password);
		
		return conn;
	}
	
	static Connection getConnection3ByDriver() throws SQLException {
		// 参数：
		// jdbc协议:postgresql子协议://主机地址:数据库端口号/要连接的数据库名
		String url = "jdbc:sqlserver://localhost:1433;databaseName=saitama";
		// 数据库用户名
		String user = "SAI16";
		// 数据库密码
		String password = "SAI16";
				
		// 1.  创建驱动程序类对象
		Driver driver = new SQLServerDriver();

		// 2. 设置用户名和密码
		Properties prop = new Properties();
		prop.setProperty("user", user);
		prop.setProperty("password", password);

		// 3. 连接数据库，返回连接对象
		Connection conn = driver.connect(url, prop);
		return conn;
	}
	

	
	static void printMetaData(Connection conn) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData();
		System.out.format("db name : %s\n", dbmd.getDatabaseProductName());
		System.out.format("db version : %s\n", dbmd.getDatabaseProductVersion());
		System.out.format("jdbc name : %s\n", dbmd.getDriverName());
		System.out.format("jdbc version : %s\n", dbmd.getDriverVersion());
		
		
	}
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		try(Connection conn = MssqlConnectionDemo.getConnection3ByDriver()) {
			MssqlConnectionDemo.printMetaData(conn);
		}
		
	}

}
