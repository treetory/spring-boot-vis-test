package com.treetory.test.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.treetory.test.common.annotation.BaseDs;

@Configuration
@MapperScan(basePackages = {MyBatisConfig.BASE_PACKAGE}, annotationClass = BaseDs.class, sqlSessionFactoryRef = "baseSqlSessionFactory")
public class BaseMyBatisConfig extends MyBatisConfig {

	@Bean
	public SqlSessionFactory baseSqlSessionFactory(@Qualifier("baseDataSource") DataSource baseDataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		configureSqlSessionFactory(sessionFactoryBean, baseDataSource);
		return sessionFactoryBean.getObject();
	}
	
}
