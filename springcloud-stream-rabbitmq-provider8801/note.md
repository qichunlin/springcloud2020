理论---实操---小总结


四种消息中间件
    ActiveMQ    
    RabbitMQ    
    RocketMQ    
    Kafka 
    
### 为什么引入cloud Stream消息驱动?解决痛点
```
一个系统可能存在两种甚至多种MQ,(切换---维护---开发)学习起来没那么容易,有没有那么一种技术诞生
让我们不再关注具体MQ技术的细节,我们只需要用一种适配绑定的方式,自动的给我
们在各种MQ内切换.
```

- cloud stream 能做到在四种消息中间件之间来回切换使用

> 总结：屏蔽底层消息中间件的差异,降低切换成本,统一消息的编程模型

    
- [消息驱动文档官网](https://spring.io/projects/spring-cloud-stream)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200811084727387-1990706193.png)


### 什么是SpringCloud Stream?
- 1.官方定义Spring Cloud Stream是一个构建消息驱动微服务的框架
- 2.应用程序通过inputs或者outputs来与Spring Cloud Stream中binder对象交互
通过我们配置binding(绑定),而Spring Cloud Stream的binder对象负责与消息中间件交互。
 所以我们只需要搞清楚如何与Spring Cloud Stream交互就可以方便使用消息驱动的方式

 
 通过Spring Integration来连接消息代理中间件以实现消息事件驱动,Spring Cloud Stream为一些供应商的消息中间件
 产品提供了个性化自动化配置实现,引入了发布---订阅、消费组、分区的三个核心概念。目前仅支持RabbitMQ、Kafka


### SpringCloud Stream API文档
[英文版](https://docs.spring.io/spring-cloud-stream/docs/3.1.0.M1/reference/html/)

[中文版](https://m.wang1314.com/doc/webapp/topic/20971999.html)


### 设计思想
#### 标准的MQ
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200811131617256-1742255954.png)

- 生产者/消费者之间靠消息媒介传递**信息**内容(Message)
- 消息必须走特定的**通道**(消息通道 Message channel)
- 消息通道里的消息如何被消费呢?谁负责收发**处理**(消息通道 Message channel的子接口SubscribableChannel,由MessageHandle消息处理器所订阅)
   

#### 为什么使用CloudStream?
比方说我们用到了RabbitMQ和Kafka,由于消息中间件的架构上的不同,像RabbitMQ由exchange,kafka有Topic和Partitions分区   
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200811132629468-1792248847.png)

这些中间件的差异性导致我们实际项目开发给我们造成了一下困扰,我们如果用了两个消息队列的其中一种,后面的业务需求,我们想往另外一种消息队列进行迁移,这时候无疑就是一个灾难性的,一大堆东西都要重新做,因为他跟我们的系统耦合了,这时候SpringCloudStream给我们提供了一种解耦合的方式  


##### stream凭什么可以统一底层差异
在没有绑定器这个概念的情况下，我们的SpringBoot应用要直接与消息中间件进行信息交互的时候，由于各消息中间件构建的初衷不同，它们的实现细节上会有较大的差异性通过定义绑定器作为中间层，完美地实现了应用程序与消息中间件细节之间的隔离。
通过向应用程序暴露统一的Channel通道，使得应用程序不需要再考虑各种不同的消息中间件实现。

>通过定义绑定器Binder作为中间层,实现了应用程序与消息中间件之间细节之间的隔离


##### Binder
OUTPUT  对应于生产者  
INTPUT  对应于消费者   

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200811133508718-1337902302.png)

>通过定义绑定器Binder作为中间层,实现了应用程序与消息中间件之间细节之间的隔离


#### Stream中的消息通信方式遵循了发布-订阅模式
topic主题进行广播
- 在RabbitMQ中就是Exchange
- 在Kafka中就是Topic

    
### Spring Cloud Stream 标准流程

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200811213157052-1814243834.png)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200811213205722-1404067256.png)

#### Binder
很方便的连接中间件,屏蔽差异


#### Channel
通道,是队列Queue的一种抽象,在消息通讯系统中就是实现存储和转发的媒介,通过Channel对队列进行配置


#### Source和Sink
简单的可理解为参照对象是Spring Cloud Stream自身,从Stream发布消息就是输出,接受消息就是输入


### 编码API和常用注解
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200811214748583-1818730126.png)

Middleware中间件，目前只支持RabbitMQ和Kafka Binder是应用与消息中间件之间的封装，目前实行了Kafka和RabbitMQ的Binder，通过BinderBinder可以很方便的连接中间件，可以动态的改变消息类型（对应于Kafka的topic，RabbitMQ的exchange），这些都可以通过配置文件来实现
@lnput注解标识输入通道，通过该输入通道接收到的消息进入应用程序
@Output注解标识输出通道，发布的消息将通过该通道离开应用程序
@StreamListener 监听队列，用于消费者的队列的消息接收
@EnableBinding 指信道channel和exchange绑定在一起
    
https://www.bilibili.com/video/BV18E411x7eT?p=87


### 模块搭建说明
springcloud-stream-rabbitmq-provider8801  消息驱动之生产者(作为生产者进行发消息模块)
springcloud-stream-rabbitmq-consumer8802  消息驱动之消费者(作为消息接收模块)
springcloud-stream-rabbitmq-consumer8803  消息驱动之消费者(作为消息接收模块)
    
### 消息驱动之生产者
新建Module springcloud-stream-rabbitmq-provider8801

POM
```
<!--引入spring-cloud-starter-stream-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
</dependency>
```

YML
```yml
server:
  port: 8801

spring:
  application:
    name: springcloud-stream-provider
  cloud:
      stream:
        binders: # 在此处配置要绑定的rabbitmq的服务信息;
          defaultRabbit: # 表示定义的名称,用于于binding整合
            type: rabbit # 消息组件类型
            environment: # 设置rabbitmq的相关的环境配置
              spring:
                rabbitmq:
                  host: localhost
                  port: 5672
                  username: guest
                  password: guest
        bindings: # 服务的整合处理
          output: # 这个名字是一个通道的名称
            destination: studyExchange # 表示要使用的Exchange名称定义
            content-type: application/json # 设置消息类型，本次为json，文本则设置“text/plain”
            binder: defaultRabbit # 设置要绑定的消息服务的具体设置

eureka:
  client: # 客户端进行Eureka注册的配置
    service-url:
      defaultZone: http://localhost:7001/eureka
  instance:
    lease-renewal-interval-in-seconds: 2 # 设置心跳的时间间隔（默认是30秒）
    lease-expiration-duration-in-seconds: 5 # 如果现在超过了5秒的间隔（默认是90秒）
    instance-id: send-8801.com  # 在信息列表时显示主机名称
    prefer-ip-address: true     # 访问的路径变为IP地址
```

主启动类
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200811215952025-485426998.png)

业务类
  发送消息接口(com.qcl.springcloud.service.IMessageProvider)
  发送消息接口实现类(com.qcl.springcloud.service.impl.MessageProviderImpl)
  Controller(com.qcl.springcloud.controller.SendMessageController)
  
测试
  启动7001 eureka
  启动rabbitMq (rabbitmq-plugins enable rabbitmq_ management)  访问 http://localhost:15672/ guest guest
  启动8801 
  访问 http://localhost:8801/sendMessage


https://www.bilibili.com/video/BV18E411x7eT?p=88
    
   