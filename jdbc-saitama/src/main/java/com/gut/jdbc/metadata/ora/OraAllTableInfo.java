package com.gut.jdbc.metadata.ora;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class OraAllTableInfo {
	
	
//	二. 方法getTables的用法
//	原型：
//
//	ResultSet DatabaseMetaData.getTables(String catalog,String schema,String tableName,String []type)
//	此方法可返回结果集合ResultSet ，结果集中有5列， 超出会报越界异常
//
//
//	功能描述：得到指定参数的表信息
//
//	参数说明：
//	参数:catalog:目录名称，一般都为空.
//	参数：schema:数据库名，对于oracle来说就用户名
//	参数：tablename:表名称
//	参数：type :表的类型(TABLE | VIEW)
//
//	注意：在使用过程中，参数名称必须使用大写的。否则得到什么东西。
//
//	三. 方法getColumns的用法
//
//	功能描述：得到指定表的列信息。
//
//	原型：
//
//	ResultSet DatabaseMetaData getColumns(String catalog,String schema,String tableName,String columnName)
//	参数说明：
//
//	参数catalog : 类别名称
//	参数schema : 用户方案名称
//	参数tableName : 数据库表名称
//	参数columnName : 列名称
//
//	四、方法getPrimaryKeys的用法
//
//	功能描述：得到指定表的主键信息。
//
//	原型：
//
//	ResultSet DatabaseMetaData getPrimaryKeys(String catalog,String schema,String tableName)
//	参数说明：
//
//	参数catalog : 类别名称
//	参数schema : 用户方案名称
//	参数tableName : 数据库表名称
//
//	备注：一定要指定表名称，否则返回值将是什么都没有。
//
//	五、方法.getTypeInfo()的用法
//
//	功能描述：得到当前数据库的数据类型信息。
//
//	六、方法getExportedKeys的用法
//
//	功能描述：得到指定表的外键信息。
//
//	参数描述：
//	参数catalog : 类别名称
//	参数schema : 用户方案名称
//	参数tableName : 数据库表名称
	

	public void getAllTable(Connection conn) {
		
		DatabaseMetaData dbmd = conn.getMetaData();
		dbmd.get
		dbmd.getTables(catalog, schemaPattern, tableNamePattern, types)
	}
	
}
