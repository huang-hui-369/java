package com.gut.sjdbc.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {
	
	
	@Autowired
	private Environment env;

    // 主数据源配置 oracle数据源配置文件
    @Primary
    @Bean(name = "oraDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.ora")
    public DataSourceProperties oraDataSourceProperties() {
        return new DataSourceProperties();
    }

    // 主数据源 oracle数据源
    @Primary
    @Bean(name = "oraDataSource")
    public DataSource oraDataSource(@Qualifier("oraDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    // ms sql server数据源配置文件
    @Bean(name = "msqDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.msq")
    public DataSourceProperties msqDataSourceProperties() {
        return new DataSourceProperties();
    }

    // ms sql server数据源
    @Bean("msqDataSource")
    public DataSource msqDataSource(@Qualifier("msqDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
    
    @Bean("tDataSource")
    public DataSource tDataSource () {
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	
    	String driverClassName = env.getProperty("spring.datasource.msq.driverClassName");
    	String url = env.getProperty("spring.datasource.msq.url");
    	String userName = env.getProperty("spring.datasource.msq.username");
    	String password = env.getProperty("spring.datasource.msq.password");
    	
    	System.out.println("spring.datasource.msq.driverClassName:" + driverClassName);
    	
    	dataSource.setDriverClassName(driverClassName);
    	dataSource.setUrl(url);
    	dataSource.setUsername(userName);
    	dataSource.setPassword(password);
    	
    	return dataSource;
    }
 
}