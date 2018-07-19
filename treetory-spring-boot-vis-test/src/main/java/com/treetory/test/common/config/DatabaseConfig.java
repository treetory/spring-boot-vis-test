/**
 * 
 */
package com.treetory.test.common.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import com.treetory.test.common.properties.DatabaseProperties;

/**
 * @author ysjeon
 *
 */
public abstract class DatabaseConfig {
	
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfig.class);
	
    @Bean
    public abstract DataSource dataSource();

    protected void configureDataSource(org.apache.tomcat.jdbc.pool.DataSource dataSource, DatabaseProperties databaseProperties) {
    	LOG.debug("configureDataSource = {}", databaseProperties);
    	dataSource.setDriverClassName(databaseProperties.getDriverClassName());
    	dataSource.setUrl(databaseProperties.getUrl());
    	dataSource.setUsername(databaseProperties.getUserName());
    	dataSource.setPassword(databaseProperties.getPassword());
        dataSource.setMaxActive(databaseProperties.getMaxActive());
        dataSource.setMaxIdle(databaseProperties.getMaxIdle());
        dataSource.setMinIdle(databaseProperties.getMinIdle());
        dataSource.setMaxWait(databaseProperties.getMaxWait());
        dataSource.setTestOnBorrow(databaseProperties.isTestOnBorrow());
        dataSource.setTestOnReturn(false);
        dataSource.setValidationQuery(databaseProperties.getValidationQuery());
    }
}