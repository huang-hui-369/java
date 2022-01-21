package com.gut.apache.dbutils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.gut.jdbc.JdbcUtils;

/**
 * 使用QueryRunner 来进行SQL的执行，是线程安全的
 * 使用ResultSetHandler接口（BeanHandler等实现类） 来进行RowSet的解析
 * 
 *     
 *     常用数据库URL地址的写法：
 
        Oracle写法：jdbc:oracle:thin:@localhost:1521:sid

        SqlServer—jdbc:microsoft:sqlserver://localhost:1433; DatabaseName=sid

        MySql—jdbc:mysql://localhost:3306/sid

        常用属性：useUnicode=true&characterEncoding=UTF-8
 * 
 * @author huanghui
 *
 */
public class DbUtilsDemo {
	
	public static void execDdl() {
		// 1. 获取数据库连接
		try(Connection conn = JdbcUtils.getConnection()) {
			// 2. 准备SQL语句(建表语句）
			String sql = "CREATE TABLE Student(" + 
							"sno CHAR(9) PRIMARY KEY, " + 
							"sname CHAR(20), " + 
							"ssex CHAR(2), " + 
							"sage SMALLINT, " + 
							"sdept CHAR(20)" + 
							");";
			// 3. 实例化QueryRunner,并执行DDL语句
			QueryRunner qr = new QueryRunner();
			qr.execute(conn, sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void execDml() {
		// 1. 获取数据库连接
		try(Connection conn = JdbcUtils.getConnection()) {
			// 2. 准备SQL语句(插入记录）
			String sql = "INSERT INTO Student VALUES(?, ?, ?, ?, ?);";

			// 3. 实例化QueryRunner,并执行DML语句
			QueryRunner qr = new QueryRunner();
			qr.update(conn, sql, "201215130", "张三", "男", 20, "IS");
			
			// 2. 准备SQL语句(更新记录）
			// String sql = "UPDATE Student SET sname = ? WHERE sno = ?;";
			
			// 2. 准备SQL语句(删除记录）
			// String sql = "DELETE FROM Student WHERE sno = ?";
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void queryOneBean() {
		// 1. 获取数据库连接
		try(Connection conn = JdbcUtils.getConnection()) {
		
			// 2. 准备SQL语句(查询一条记录）
			String sql = "SELECT * FROM Student WHERE sno = ?;";

			// 3. 实例化QueryRunner,并执行DQL语句
			QueryRunner qr = new QueryRunner();
			Student  student = qr.query(conn, sql, new BeanHandler<Student>(Student.class), "201215128");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void queryOneObjectArray() {
		// 1. 获取数据库连接
		try(Connection conn = JdbcUtils.getConnection()) {
		
			// 2. 准备SQL语句(查询一条记录）
			String sql = "SELECT * FROM Student WHERE sno = ?;";

			// 3. 实例化QueryRunner,并执行DQL语句
			QueryRunner qr = new QueryRunner();
			Object[] row = qr.query(conn, sql, new ArrayHandler(), "201215128");
			for(Object fieldValue : row) {		// 每一个fieldValue表示第一行记录的一个分量（列值）
			    System.out.print(fieldValue + " ");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void queryOneMap() {
		// 1. 获取数据库连接
		try(Connection conn = JdbcUtils.getConnection()) {
		
			// 2. 准备SQL语句(查询一条记录）
			String sql = "SELECT * FROM Student WHERE sno = ?;";

			// 3. 实例化QueryRunner,并执行DQL语句
			QueryRunner qr = new QueryRunner();
			Map<String, Object> columns = qr.query(conn, sql, new MapHandler(), "201215128");
			columns.forEach((k,v)-> {
				System.out.format("%s:%s\n",k, v);
			});
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void queryBeanList() {
		// 1. 获取数据库连接
		try (Connection conn = JdbcUtils.getConnection() ) {
			// 2. 准备SQL语句(查询多条记录）
			String sql = "SELECT * FROM Student;";

			// 3. 实例化QueryRunner,并执行DQL语句
			QueryRunner qr = new QueryRunner();
			List<Student> list = qr.query(conn, sql, new BeanListHandler<Student>(Student.class));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void queryArrayObjectList() {
		// 1. 获取数据库连接
		try (Connection conn = JdbcUtils.getConnection() ) {
			// 2. 准备SQL语句(查询多条记录）
			String sql = "SELECT * FROM Student;";

			// 3. 实例化QueryRunner,并执行DQL语句
			QueryRunner qr = new QueryRunner();
			List<Object[]> rowList = qr.query(conn, sql, new ArrayListHandler());
			for(Object[] row : rowList) {
			    for(Object fieldValue : row) {
			        System.out.print(fieldValue + " ");
			    }
			    System.out.println();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void queryMapList() {
		// 1. 获取数据库连接
		try (Connection conn = JdbcUtils.getConnection() ) {
			// 2. 准备SQL语句(查询多条记录）
			String sql = "SELECT * FROM Student;";

			// 3. 实例化QueryRunner,并执行DQL语句
			QueryRunner qr = new QueryRunner();
			List<Map<String, Object>> list = qr.query(conn, sql, new MapListHandler());
			
			for(Map<String, Object> columns : list) {
				columns.forEach((k,v)-> {
					System.out.format("%s:%s\n",k, v);
				});
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void queryCount() {
		// 1. 获取数据库连接
		try (Connection conn = JdbcUtils.getConnection() ) {
			// 2. 准备SQL语句(查询多条记录）
			String sql = "SELECT count(1) FROM Anken";

			// 3. 实例化QueryRunner,并执行DQL语句
			QueryRunner qr = new QueryRunner();
			BigDecimal count = qr.query(conn, sql, new ScalarHandler<BigDecimal>());
			System.out.println(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
public static void main(String[] args) throws SQLException {
		
		DbUtilsDemo.queryCount();
	}
		

}
