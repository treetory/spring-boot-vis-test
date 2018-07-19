package com.treetory.test.common.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.treetory.test.common.properties.BaseDatabaseProperties;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(BaseDatabaseProperties.class)
public class BaseDatabaseConfig extends DatabaseConfig {

	@Autowired
	private BaseDatabaseProperties baseDatabaseProperties;
	
	@Primary
	@Bean(name = "baseDataSource", destroyMethod = "close")
	public DataSource dataSource() {
		org.apache.tomcat.jdbc.pool.DataSource baseDataSource = new org.apache.tomcat.jdbc.pool.DataSource();
		configureDataSource(baseDataSource, baseDatabaseProperties);
		return baseDataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager(@Qualifier("baseDataSource") DataSource baseDataSource) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(baseDataSource);
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	
    }

}
