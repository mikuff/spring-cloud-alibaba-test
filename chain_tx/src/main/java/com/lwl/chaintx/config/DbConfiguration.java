package com.lwl.chaintx.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.lwl.chaintx.mapper.dsone"}, sqlSessionFactoryRef = "dsOneSqlSessionFactory")
@MapperScan(basePackages = {"com.lwl.chaintx.mapper.dstwo"}, sqlSessionFactoryRef = "dsTwoSqlSessionFactory")
public class DbConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.ds-one")
    @Primary
    public DataSourceProperties dsOneProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource dsOneDataSource() {
        return dsOneProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.ds-two")
    public DataSourceProperties dsTwoProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dsTwoDataSource() {
        return dsTwoProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }


    @Bean
    public SqlSessionFactory dsOneSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dsOneDataSource());
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return factoryBean.getObject();

    }

    @Bean
    public SqlSessionTemplate dsOneSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(dsOneSqlSessionFactory());
        return template;
    }


    @Bean
    public SqlSessionFactory dsTwoSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dsTwoDataSource());
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate dsTwoSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(dsTwoSqlSessionFactory());
        return template;
    }


    @Bean(name = "dsOneTransactionManager")
    public DataSourceTransactionManager dsOneTransactionManager() {
        return new DataSourceTransactionManager(dsOneDataSource());
    }

    @Bean(name = "dsTwoTransactionManager")
    public DataSourceTransactionManager dsTwoTransactionManager() {
        return new DataSourceTransactionManager(dsTwoDataSource());
    }


    @Bean
    public PlatformTransactionManager chainedTransactionManager() {
        return new ChainedTransactionManager(dsOneTransactionManager(), dsTwoTransactionManager());
    }
}
