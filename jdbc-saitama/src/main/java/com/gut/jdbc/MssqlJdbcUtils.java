package com.gut.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author huanghui
 * 
 * -----------   Connection ——代表数据库的链接

    Jdbc程序中的Connection，它用于代表数据库的链接。Connection 是数据库编程中最重要的一个对象，客户端与数据库所有交互都是通过Connection 对象完成的，这个对象的常用方法：

        (1) createStatement()：创建向数据库发送sql的statement对象。

        (2) prepareStatement(sql) ：创建向数据库发送预编译sql的PrepareSatement对象。

        (3) prepareCall(sql)：创建执行存储过程的callableStatement对象。

        (4) setAutoCommit(boolean autoCommit)：设置事务是否自动提交。

        (5) commit() ：在链接上提交事务。

        (6) rollback() ：在此链接上回滚事务。

 

   -----------   Statement  ——向数据库发送SQL语句

 
    Jdbc程序中的Statement对象用于向数据库发送SQL语句， Statement对象常用方法：

 
        (1) executeQuery(String sql) ：用于向数据库发送查询语句。

        (2) executeUpdate(String sql)：用于向数据库发送insert、update或delete语句

        (3) execute(String sql)：用于向数据库发送任意sql语句

        (4) addBatch(String sql) ：把多条sql语句放到一个批处理中。

        (5) executeBatch()：向数据库发送一批sql语句执行。

        (6) clearBatch() ：清空此 Statement 对象的当前 SQL 命令列表。
 

   -----------   ResultSet  ——代表Sql语句的执行结果

         Jdbc程序中的ResultSet用于代表Sql语句的执行结果。Resultset封装执行结果时，采用的类似于表格的方式。ResultSet 对象维护了一个指向表格数据行的游标，初始的时候，游标在第一行之前，调用ResultSet.next() 方法，可以使游标指向具体的数据行，进行调用方法获取该行的数据。

    (1) ResultSet提供了对结果集进行滚动的方法：
            a、next()：移动到下一行

            b、Previous()：移动到前一行

            c、absolute(int row)：移动到指定行

            d、beforeFirst()：移动resultSet的最前面。

            e、 afterLast() ：移动到resultSet的最后面。

    (2) ResultSet既然用于封装执行结果的，所以该对象提供了用于获取数据的get方法：

 
       获取任意类型的数据

                getObject(int index)

                getObject(string columnName)


        获取指定类型的数据，例如：

                getString(int index)

                 getString(String columnName)
                 
                 。。。 
                 
   -----------   PreparedStatement
   
   -----------  CallableStatement
                 
 *
 */
public class MssqlJdbcUtils {
	private static String driverClassName;		// 数据库驱动类名
	private static String url;				// 连接数据库的url
	private static String user;				// 数据库用户名
	private static String password;			// 数据库密码
	
	static {
		// 加载jdbcConf.properties中的数据库相关参数
		InputStream is = MssqlJdbcUtils.class.getResourceAsStream("/jdbc.properties");
		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		driverClassName = properties.getProperty("mssql.driverClassName");
		url = properties.getProperty("mssql.url");
		user = properties.getProperty("mssql.username");
		password = properties.getProperty("mssql.password");
		// 加载Driver类，Driver类对象将自动被注册到DriverManager类中
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// 连接数据库，返回连接对象
	public static Connection getConnection() {
		Connection conn = null;
		try {
			System.out.format("debug driverClassName : %s\n", driverClassName);
			System.out.format("url: [%s]\n", url);
			System.out.format("user : [%s]\n", user);
			System.out.format("password : [%s]\n", password);
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	static void printMetaData(Connection conn) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData();
		System.out.format("db name : %s\n", dbmd.getDatabaseProductName());
		System.out.format("db version : %s\n", dbmd.getDatabaseProductVersion());
		System.out.format("jdbc name : %s\n", dbmd.getDriverName());
		System.out.format("jdbc version : %s\n", dbmd.getDriverVersion());
	}
	
	public static void main(String[] args) throws SQLException {
		
		try(Connection conn = MssqlJdbcUtils.getConnection()) {
			MssqlJdbcUtils.printMetaData(conn);
		}
	}
}
