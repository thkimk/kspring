package com.kkk.sbgtest.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class DataSourceConfig {
/*
    @Bean(name = "defaultDs")
    @Primary
    @ConfigurationProperties(prefix = "spring.default-ds.datasource")
    public DataSource defaultDs() {
        return DataSourceBuilder.create().build();
    }
*/

/*
    @Primary
    @Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Autowired @Qualifier("defaultDs") DataSource defaultDs, ApplicationContext applicationContext)
            throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(defaultDs);
        factoryBean.setConfigLocation(applicationContext.getResource("classpath:config.mybatis.xml"));
//        factoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper-primary/ ** / *.xml"));
        return factoryBean.getObject();
    }

    @Primary
    @Bean(name="sqlSession")
    public SqlSession sqlSession(@Autowired @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Primary
    @Bean(name="transactionManager")
    public DataSourceTransactionManager transactionManager(@Autowired @Qualifier("defaultDs") DataSource defaultDs) {
        return new DataSourceTransactionManager(defaultDs);
    }
*/

/*
    @Bean
    @ConfigurationProperties(prefix = "spring.second-ds.datasource")
    public DataSource secondDs() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name="secondSqlSessionFactory")
    public SqlSessionFactory secondSqlSessionFactory(@Autowired @Qualifier("secondDs") DataSource secondDs, ApplicationContext applicationContext)
            throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(secondDs);
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:secondmaps/ ** / *.xml"));
        return factoryBean.getObject();
    }

    @Bean(name="secondSqlSession")
    public SqlSession secondSqlSession(@Autowired @Qualifier("secondSqlSessionFactory") SqlSessionFactory secondSqlSessionFactory) {
        return new SqlSessionTemplate(secondSqlSessionFactory);
    }
*/

//    @Bean(name="secondaryTransactionManager")
//    public DataSourceTransactionManager secondaryTransactionManager(@Autowired @Qualifier("secondDs") DataSource secondDs) {
//        return new DataSourceTransactionManager(secondDs);
//    }


}
