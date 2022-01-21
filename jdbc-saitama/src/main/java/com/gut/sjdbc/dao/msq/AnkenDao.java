package com.gut.sjdbc.dao.msq;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AnkenDao {
	
	//@Resource(name="msqJdbcTemplate")
	@Resource(name="tJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	public long getCount() {
		String sql = "select count(1) from Anken";
		long cnt = jdbcTemplate.queryForObject(sql, Long.class);
		
		return cnt;
	}
 	

}
