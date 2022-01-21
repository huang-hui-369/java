package com.gut.jdbc;

import java.util.List;

/**
 * 
 * ｊｄｂｃ 分页的实现思想
 * 
 *  
 * 
 * @author huanghui
 *
 */
public class PageHelper {

	
	public void mySql() {
//			MySQL分页的实现语句：
//
//	        Select * from table limit M,N
//
//	        M：记录开始索引位置
//
//	        N：取多少条记录。

	}
	
	public void oracle() {
//		select * from (
//
//                select rownum r_, row_.*  from (
//
//                    select * from student order by id
//
//                ) row_ where rownum <=5
//
//                     ) where r_>=1
//
//       1位置：起始索引位置。
//       5位置：结束索引位置。
	}
	
	
	public List<Object> queryPage(String sql, PageConditon pageinfo, PageResult pageresult) {
		return null;
	}
	
	public class PageConditon {
		
		public int getPageSize() {

			return 0;

        }
		
		public int getPageIndex() {

			return 0;

        }
		
	}
	
	public class PageResult {
		
		public int getTotalCount() {

			return 0;

        }
		
		public int getTotalpage() {
			return 0;
		}
		
	}
	
	
	public class PageUtil {
		
		public long getStartIndex4NextPage(int currentPage) {
			return 0;
		}
		
		public long getEndIndex4NextPage(int currentPage) {
			return 0;
		}
		
		public long getStartIndex4PrevPage(int currentPage) {
			return 0;
		}
		
		public long getEndIndex4PrevPage(int currentPage) {
			return 0;
		}
		
	}
	
	
}
