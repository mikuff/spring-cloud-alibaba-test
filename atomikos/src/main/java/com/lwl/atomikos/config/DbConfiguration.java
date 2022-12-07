package com.lwl.atomikos.config;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;


@Configuration
public class DbConfiguration {

    // 数据源dsOne
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid.ds-one")
    public DataSourceProperties dsOneProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource dsOneDataSource() {

        // 数据源配置信息
        DataSourceProperties dsOneProperties = dsOneProperties();

        // druid连接池数据源
        DruidXADataSource  dataSource = new DruidXADataSource();
        dataSource.setDriverClassName(dsOneProperties.getUsername());
        dataSource.setUrl(dsOneProperties.getUrl());
        dataSource.setUsername(dsOneProperties.getUsername());
        dataSource.setPassword(dsOneProperties.getPassword());

        // 包装atomikos数据源
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName("ds-one");
        atomikosDataSourceBean.setXaDataSource(dataSource);
        return atomikosDataSourceBean;
    }

    // 数据源dsTwo
    @Bean
    @ConfigurationProperties(prefix = "spring.ds-two")
    public DataSourceProperties dsTwoProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dsTwoDataSource() {
        // 数据源配置信息
        DataSourceProperties dsTwoProperties = dsTwoProperties();

        // druid连接池数据源
        DruidXADataSource  dataSource = new DruidXADataSource();
        dataSource.setDriverClassName(dsTwoProperties.getUsername());
        dataSource.setUrl(dsTwoProperties.getUrl());
        dataSource.setUsername(dsTwoProperties.getUsername());
        dataSource.setPassword(dsTwoProperties.getPassword());

        // 包装atomikos数据源
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName("ds-two");
        atomikosDataSourceBean.setXaDataSource(dataSource);
        return atomikosDataSourceBean;
    }


    // 用于使用的是mybatis，因此通过数据dsOne构建MybatisSqlSessionFactoryBean
    // 如果是jdbc则直接构建jdbcTemplate
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

    // dsTwo构建MybatisSqlSessionFactoryBean
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

}
