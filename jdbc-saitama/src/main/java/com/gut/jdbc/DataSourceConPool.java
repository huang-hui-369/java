package com.gut.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author huanghui
 *
 * 数据库连接池 使用DataSource 获取Connection
 * 
 * 
 *
 */
public class DataSourceConPool {
	
	
	/**
	 *  DBCP 是 Apache 软件基金组织下的开源连接池实现
	 *  
	 *  ----  dbcpconfig.properties  ----  ----  ----  ---- 
	 *  
	 *  driverClassName=com.mysql.jdbc.Driver

		url=jdbc:mysql://localhost:3306/day16
		
		username=root
		
		password=root 
		
		#<!-- 初始化连接 -->
		
		initialSize=10 
		
		#最大连接数量
		
		maxActive=50 
		
		#<!-- 最大空闲连接 -->
		
		maxIdle=20 
		 
		
		#<!-- 最小空闲连接 -->
		
		minIdle=5 
		
		#<!-- 超时等待时间以毫秒为单位 6000毫秒/1000等于60秒 -->
		maxWait=60000 
		
		#JDBC驱动建立连接时附带的连接属性属性的格式必须为这样：[属性名=property;] 
		
		#注意："user" 与 "password" 两个属性会被明确地传递，因此这里不需要包含他们。
		
		connectionProperties=useUnicode=true;characterEncoding=utf8
		 
		
		#指定由连接池所创建的连接的自动提交（auto-commit）状态。
		
		defaultAutoCommit=true
		 
		
		#driver default 指定由连接池所创建的连接的只读（read-only）状态。
		
		#如果没有设置该值，则“setReadOnly”方法将不被调用。（某些驱动并不支持只读模式，如：Informix）
		
		defaultReadOnly=
		 
		
		#driver default 指定由连接池所创建的连接的事务级别（TransactionIsolation）。
		 
		
		#可用值为下列之一：（详情可见javadoc。）NONE,READ_UNCOMMITTED, READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE
		
		defaultTransactionIsolation=READ_COMMITTED
		
	 */
	public void getDBCPDataSource() throws IOException {
		
		 InputStream in = DataSourceConPool.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");

	
         Properties prop = new Properties();

         prop.load(in);

         // BasicDataSourceFactory factory = new BasicDataSourceFactory();

         // dataSource = factory.createDataSource(prop);
	}
	
	
	public void getDataSourceByJndi() {
		
//		 Context initCtx = new InitialContext();
//
//		 
//
//         Context envCtx = (Context) initCtx.lookup("java:comp/env");
//
//
//
//         dataSource = (DataSource)envCtx.lookup("jdbc/datasource");
	}
	
	
	
	/**
	
	DataSource 就一个方法
	
	Connection getConnection() throws SQLException;
	
	如果自己通过实现DataSource接口来实现ConnectionPool该怎么实现。
	
	一个简单的实现，如下就是创建DataSource时，创建多给Connection 保存到链表中。

	public class JdbcPool implements DataSource {

      private static LinkedList<Connection> list = new LinkedList<Connection>();

      private static Properties config = new Properties();

      static{

            try {

                  config.load(JdbcUtils_DBCP.class.getClassLoader().getResourceAsStream("db.properties"));

                        // 以配置文件方式 读取数据库配置信息。 

                  Class.forName(config.getProperty("driver"));

                  for(int i=0;i<10;i++){

                        Connection conn = DriverManager.getConnection(config.getProperty("url"), config.getProperty("username"), config.getProperty("password"));

                        list.add(conn);

                  }

            } catch (Exception e) {

                  throw new ExceptionInInitializerError(e);

            }


      }
	
		
	一个问题，就是调用Connection.close()时，实际并不关闭连接，而是将connecton 返回到链表中，这个该怎么实现呢	
	
	
	方法一 
	
	就是创建 UnCloseConnection 设置一个变量来保存原有的con，设置一个变量来保存connection容器 实现 Connection 所有接口 并且重写 close方法
	
	public void close() {
	  将con 返回到connection容器中
	}
	
	方法二 利用 aop proxy
	
	 proxyConn = (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(), conn.getClass().getInterfaces(),

                     new InvocationHandler() {

                           //此处为内部类，当close方法被调用时将conn还回池中,其它方法直接执行

                             public Object invoke(Object proxy, Method method,Object[] args) throws Throwable {

                                           判断方法名为"close"时，将conn还回池中
                                           if (method.getName().equals("close")) {

                                                 pool.addLast(conn);

                                                 return null;

                                           }
                                           
                                           其它方法则直接执行

                                         return method.invoke(conn, args);

                            }

                 }    

        );
	
	 *
	 */

}
