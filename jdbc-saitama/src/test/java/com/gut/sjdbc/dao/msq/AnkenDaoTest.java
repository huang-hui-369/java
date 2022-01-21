package com.gut.sjdbc.dao.msq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gut.sjdbc.dao.msq.AnkenDao;


// @JdbcTest
@SpringBootTest
class AnkenDaoTest {

	@Autowired	
	AnkenDao dao;
	
	@Test
	void testGetCount() {
		System.out.println(dao.getCount());
	}

}
