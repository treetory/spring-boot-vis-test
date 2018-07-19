/**
 * 
 */
package com.treetory.test.common.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @author ysjeon
 *
 */
public abstract class MyBatisConfig {
	
	public static final String BASE_PACKAGE = "com.treetory.test.mvc.mapper";
	public static final String TYPE_ALIASES_PACKAGE = "com.treetory.test.mvc.domain";
	public static final String CONFIG_LOCATION_PATH = "classpath:META-INF/mybatis/mybatis-config.xml";
	public static final String MAPPER_LOCATIONS_PATH = "classpath:META-INF/mybatis/mapper/**/*.xml";
	
	protected void configureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, DataSource dataSource) throws IOException {
		PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
		sessionFactoryBean.setConfigLocation(pathResolver.getResource(CONFIG_LOCATION_PATH));
		sessionFactoryBean.setMapperLocations(pathResolver.getResources(MAPPER_LOCATIONS_PATH));
	}
}