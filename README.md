# spring-cloud-alibaba-test
spring cloud alibaba 测试学习

atomikos 模块是测试jta事务,XA规范的2pc实现

chain-tx 模块是测试mysql+mysql的事务,base efforts 1pc(链式事务)  

mq-db 模块是测试mysql+rabbitmq的事务,base efforts 1pc(链式事务)  

user-service,order-server,ticket是测试分布式事务中消息驱动(消息事务+最终一致性)
