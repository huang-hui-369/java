package com.gut.sjdbc.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcTemplateConfig {

	 // JdbcTemplate主数据源ds1数据源
    @Primary
    @Bean(name = "oraJdbcTemplate")
    public JdbcTemplate oraJdbcTemplate(@Qualifier("oraDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // JdbcTemplate第二个ds2数据源
    @Bean(name = "msqJdbcTemplate")
    public JdbcTemplate msqJdbcTemplate(@Qualifier("msqDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
	
    // JdbcTemplate第二个ds2数据源
    @Bean(name = "tJdbcTemplate")
    public JdbcTemplate tJdbcTemplate(@Qualifier("tDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
	
}

