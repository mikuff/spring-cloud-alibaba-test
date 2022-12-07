package com.lwl.ticketservice.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class ChainTxConfig {


    // db事务，并且优先使用db事务
    @Primary
    @Bean("dbTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // 开启rabbit mq事务提交
    @Bean("rabbitTransactionManager")
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    // rabbitmq 和 db 的链式事务
    @Bean("rabbitAndDbTransactionManager")
    public PlatformTransactionManager chainedTransactionManager(
            ConnectionFactory connectionFactory,
            DataSourceTransactionManager dataSourceTransactionManager) {
        return new ChainedTransactionManager(rabbitTransactionManager(connectionFactory), dataSourceTransactionManager);
    }
}
