package com.gut.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
public class JdbcUtils {
	private static String driverClassName;		// 数据库驱动类名
	private static String url;				// 连接数据库的url
	private static String user;				// 数据库用户名
	private static String password;			// 数据库密码
	
	static {
		// 加载jdbcConf.properties中的数据库相关参数
		InputStream is = JdbcUtils.class.getResourceAsStream("/jdbc.properties");
		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		driverClassName = properties.getProperty("driverClassName");
		url = properties.getProperty("url");
		user = properties.getProperty("username");
		password = properties.getProperty("password");
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
			Properties props = new Properties();
			props.setProperty("user", user);
			props.setProperty("password", password);
			props.setProperty("remarksReporting", "true");
//			conn = DriverManager.getConnection(url, user, password);
			conn = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	static void printMetaData(Connection conn) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData();
		System.out.println("---- MetaData -------");
		System.out.format("db name : %s\n", dbmd.getDatabaseProductName());
		System.out.format("db version : %s\n", dbmd.getDatabaseProductVersion());
		System.out.format("jdbc name : %s\n", dbmd.getDriverName());
		System.out.format("jdbc version : %s\n", dbmd.getDriverVersion());
	}
	
	static void printCatlog(Connection conn) throws SQLException {
		System.out.println("---- Catlog -------");
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getCatalogs();
		
		printResultSet(rs);
		
	}
	
	static void printSchema(Connection conn) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getSchemas();
		System.out.println("---- Schemas -------");
		printResultSet(rs);
	}
	
	static void printTypeInfo(Connection conn) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getTypeInfo();
		System.out.println("---- TypeInfo -------");
		printResultSet(rs);
	}
	
	static void printTableType(Connection conn) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getTableTypes();
		System.out.println("---- TableTypes -------");
		printResultSet(rs);
	}
	
	
	static void printAllProcedure(Connection conn) throws SQLException {
		// PROCEDURE_TYPE : [2] is function
		// PROCEDURE_TYPE : [1] is procedure
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getProcedures(conn.getCatalog(), "SAI16", "%");
		System.out.println("---- all Procedure -------");
		printResultSet(rs);
	}
	
	static void printProcedureCloumn(Connection conn, String procCat, String procedurName) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getProcedureColumns(procCat, "SAI16", procedurName, "%");
		System.out.format("----procedure %s.%s all column -------\n", procCat, procedurName);
		printResultSet(rs);
	}
	
	static void printProcedurePackegeBody(Connection conn, String schema, String packageName) throws SQLException {
		
		System.out.format("----procedure package %s source -------\n", packageName);
	
		String sql = "SELECT TEXT\r\n"
				+ "  FROM ALL_SOURCE s\r\n"
				+ "  WHERE s.OWNER = ? \r\n"
				+ "  AND   s.NAME = ?\r\n"
				+ "  AND   s.TYPE = 'PACKAGE BODY'\r\n"
				+ "  ORDER BY s.LINE";
		System.out.print("CREATE OR REPLACE ");
		try(PreparedStatement pstmt = conn.prepareStatement(sql);) {
			int i = 0;
			pstmt.setObject(++i, schema);
			pstmt.setObject(++i, packageName);
			try(ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					System.out.println(rs.getString(1));
				}
			}
		}
		
		
	}
	
	static void printAllTable(Connection conn) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getTables("null", "SAI16", "%", new String[] { "TABLE" });
		System.out.println("---- AllTable -------");
		printResultSet(rs);
	}
	
	
	
	static void printTableColumns(Connection conn, String tableName) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getColumns(conn.getCatalog(), "SAI16", tableName, "%");
		System.out.format("----table %s all columns -------\n", tableName);
		printResultSet(rs);
	}
	
	static void printTablePK(Connection conn, String tableName) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getPrimaryKeys(conn.getCatalog(), "SAI16", tableName);
		System.out.format("----table %s PrimaryKeys -------\n", tableName);
		printResultSet(rs);
	}
	
	static void printTableImportedKeys(Connection conn, String tableName) throws SQLException {
		// 本表參照其他表的字段所建的外鍵
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getImportedKeys(conn.getCatalog(), "SAI16", tableName);
		System.out.format("----table %s ImportedKeys -------\n", tableName);
		printResultSet(rs);
	}
	
	static void printTableExportedKeys(Connection conn, String tableName) throws SQLException {
		// 其他表參照本表的字段所建的外鍵
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getExportedKeys(conn.getCatalog(), "SAI16", tableName);
		System.out.format("----table %s ExportedKeys-------\n", tableName);
		printResultSet(rs);
	}
	
	static void printTableIndex(Connection conn, String tableName, boolean unique, boolean approximate) throws SQLException {
		// 其他表參照本表的字段所建的外鍵
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getIndexInfo(conn.getCatalog(), "SAI16", tableName, unique, approximate);
		System.out.format("----table %s index --%b--%b---\n", tableName, unique, approximate);
		printResultSet(rs);
	}
	
	static void printTableDDL(Connection conn, String tableName) throws SQLException, IOException {
		System.out.format("----table %s ddl -----\n", tableName);
		// 其他表參照本表的字段所建的外鍵
		try(PreparedStatement pstmt = conn.prepareStatement("SELECT DBMS_METADATA.GET_DDL(?, ?) FROM DUAL");) {
			
			int i = 0;
			pstmt.setObject(++i, "TABLE");
			pstmt.setObject(++i, tableName);
			try(ResultSet rs = pstmt.executeQuery();) {
				if(rs.next()) {
					// get ddl reader
					try(Reader reader = rs.getCharacterStream(1);) {

						StringWriter sw = new StringWriter();
						
						int len = 0;

						char buffer[] = new char[1024];

						while ((len = reader.read(buffer)) > 0) {
							sw.write(buffer, 0, len);
						}
						System.out.println(sw.toString());
					}
				}
			}
			
			
		} 
	}
	
	// transaction isolation
	
	// typemap
	
	static void printResultSet(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd =  rs.getMetaData();
		int rowCnt = 0;
		int colCnt = rsmd.getColumnCount();
		while(rs.next()) {
			for(int i=0;i<colCnt;i++) {
				System.out.format("%s : [%s], ", rsmd.getColumnName(i+1), rs.getObject(i+1));
			}
			System.out.println();
			rowCnt++;
		}
		System.out.format("row cnt : %d \n", rowCnt);
	}
	
	// 用于执行DML语句（insert, update, delete）
	public static int update(Connection conn, String sql, Object...args) {
		// 获取预编译SQL语句中参数的个数
		int count = 0;
		try {
			// 预编译SQL
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// 为预编译SQL语句设置参数值
			count = pstmt.getParameterMetaData().getParameterCount();
			for(int i = 0; i < args.length; i++) {
				pstmt.setObject(i + 1, args[i]);
			}
			// 执行SQL语句, 返回影响行数
			count = pstmt.executeUpdate();
			// 释放资源
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	// 用于执行DQL语句,返回一行结果
	public static Object[] query(Connection conn, String sql, Object...args) {
		List<Object> list = new ArrayList<Object>();
		try {
			// 预编译SQL
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// 为预编译SQL语句设置参数值
			int count = pstmt.getParameterMetaData().getParameterCount();
			for(int i = 0; i < args.length; i++) {
				pstmt.setObject(i + 1, args[i]);
			}
			ResultSet rs = pstmt.executeQuery();
			// 将结果封装到集合中
			int columnCount = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					Object value = rs.getObject(i);
					list.add(value);
				}
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 返回数组结果
		return list.toArray();
	}
	
	public static void main(String[] args) throws SQLException {
		
		try(Connection conn = JdbcUtils.getConnection()) {
//			JdbcUtils.printMetaData(conn);
//			
//			JdbcUtils.printCatlog(conn);
//			JdbcUtils.printSchema(conn);;
//			JdbcUtils.printTypeInfo(conn);;
//			JdbcUtils.printTableType(conn);
//			JdbcUtils.printAllTable(conn);
//			JdbcUtils.printTableColumns(conn, "ANKEN_RIREKI");
//			JdbcUtils.printTablePK(conn, "ANKEN_RIREKI");
//			JdbcUtils.printTableImportedKeys(conn, "ANKEN_RIREKI");
//			JdbcUtils.printTableExportedKeys(conn, "ANKEN_RIREKI");
//			JdbcUtils.printTableIndex(conn, "ANKEN_RIREKI", true, false);
//			JdbcUtils.printTableDDL(conn, "ANKEN_RIREKI");
//			JdbcUtils.printAllProcedure(conn);
//			JdbcUtils.printProcedureCloumn(conn, "DATA_IMPORT", "PROC_KENSETU_KOBETU_KOUHAN");
			JdbcUtils.printProcedurePackegeBody(conn, "SAI16", "DATA_IMPORT");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
