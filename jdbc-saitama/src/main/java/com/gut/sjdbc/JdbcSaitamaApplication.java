package com.gut.sjdbc;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class JdbcSaitamaApplication implements CommandLineRunner {

	@Resource(name="oraJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(JdbcSaitamaApplication.class, args);
	}
	
	 @Override
    public void run(String... args) throws Exception {
		String sql = "select count(1) from Anken";
		long cnt = jdbcTemplate.queryForObject(sql, Long.class);
        System.out.println("hello" + cnt);
    }

}
