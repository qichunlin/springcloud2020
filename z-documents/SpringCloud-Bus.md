## 14.SpringCloud Bus 消息总线
https://www.bilibili.com/video/BV18E411x7eT?p=78
https://www.bilibili.com/video/BV18E411x7eT?p=79
https://www.bilibili.com/video/BV18E411x7eT?p=80
https://www.bilibili.com/video/BV18E411x7eT?p=81
https://www.bilibili.com/video/BV18E411x7eT?p=82


### 概述
### RabbitMQ环境配置

分布式自动刷新配置功能

Spring Cloud Bus 配合 Spring Cloud Config使用可以实现配置的动态刷新。


#### 是什么

Bus支持两种消息代理：RabbitMQ和Kafka


1.Spring Cloud Bus 配合Spring Cloud Config 使用可以实现配置的动态刷新。

2.Spring Cloud Bus是用来将分布式系统的节点与轻量级消息系统链接起来的框架,它整合了Java的事件处理机制和消息中间件的功能。

3.Spring Clud Bus目前支持RabbitMQ和Kafka。

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829110142475-762648601.png)



#### 能干嘛

Spring Cloud Bus能管理和传播分布式系统间的消息,就像一个分布式执行器,可用于广播状态更改、事件推送等,也可以当作微服务间的通信通道

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829110455193-1314512422.png)



#### 为何被称为总线
##### 什么是总线

在微服务架构的系统中,通常会使用轻量级的消息代理来构建一个共用的消息主题,并让系统中所有微服务实例都连接上来。由于该主题中产生的消息会被所有实例监听和消费,所以称它为消息总线。在总线上的各个实例,都可以方便地广播一些需要让其他连接在该主题上的实例都知道的消息。

##### 基本原理

ConfigClient实例都监听MQ中同一个topic（默认是springCloudBus)。当一个服务刷新数据的时候,它会把这个信息放入到Topic中,这样其它监听同一Topic的服务就能得到通知,然后去更新自身的配置。

[ActiveMQ教程(MQ消息中间件快速入门)视频地址](https://www.bilibili.com/video/av55976700?from=search&seid=15010075915728605208)



### RabbitMQ环境配置

1.安装Erlang,下载地址： http://erlang.org/download/otp_win64_21.3.exe

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829111303484-2072958213.png)


2.安装RabbitMQ,下载地址：https://dl.bintray.com/rabbitmq/all/rabbitmq-server/3.7.14/:rabbitmq-server-3.7.14.exe  (https://dl.bintray.com/rabbitmq/all/rabbitmq-server/3.7.14/)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829111728786-620471935.png)


3.进入RabbitMQ安装目录下的sbin目录

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829111620320-981986174.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829111829381-793049999.png)


4.在安装rabbitmq的sbin文件夹下运行 rabbitmq-plugins enable rabbitmq_management  输入以下命令启动管理功能

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829112026312-1819926094.png)


4.1 可视化插件

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829111728786-620471935.png)


5.访问地址查看是否安装成功：
   
   http://localhost:15672/
   
   输入账号密码并登录：guest guest

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829112239874-1834292134.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829112300914-396990901.png)



### SpringCloud Bus动态刷新全局广播
#### 必须先具备良好的RabbitMQ环境

#### 演示广播效果,增加复杂度,再以3355为模板再制作一个3366

- 新建Module (springcloud-config-client-3366)

- POM

```xml
<dependencies>
    <!--添加消息总线RabbitMQ支持-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bus-amqp</artifactId>
    </dependency>
    <!--Config的客户端-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

- YML
```yml
server:
  port: 3366

spring:
  application:
    name: springcloudconfig-client
  cloud:
    #Config客户端配置
    config:
      label: master #分支名称
      name: springcloudconfig #配置文件名称
      profile: dev #读取后缀名称   上述3个综合：master分支上springcloudconfig-dev.yml的配置文件被读取http://config-3344.com:3344/master/config-dev.yml
      uri: http://localhost:3344 #配置中心地址

#rabbitmq相关配置 15672是Web管理界面的端口；5672是MQ访问的端口
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

#服务注册到eureka地址
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7001/eureka
  instance:
    prefer-ip-address: true


# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

- 主启动



- 业务类


#### 设计思想

1)利用消息总线触发一个客户端/bus/refresh,而刷新所有客户端的配置

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829113226522-1244458444.png)


2)利用消息总线触发一个服务端ConfigServer的/bus/refresh端点,而刷新所有客户端的配置

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829113308117-1570249004.png)


图二的架构显然更加适合,图一不适合的原因如下：
   
   打破了微服务的职责单一性,因为微服务本身是业务模块,它本不应该承担配置刷新的职责。
   破坏了微服务各节点的对等性。
   有一定的局限性。例如,微服务在迁移时,它的网络地址常常会发生变化,此时如果想要做到自动刷新,那就会增加更多的修改



#### 给springcloud-config-center-3344配置中心服务端添加消息总线支持

- POM
```xml
<!--添加消息总线RabbitMQ支持-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```


- YML
```yml
#rabbitmq相关配置
rabbitmq:
  host: localhost
  port: 5672
  username: guest
  password: guest

#服务注册到eureka地址
eureka:
  client:
    service-url:
      #配置单机版
      defaultZone: http://localhost:7001/eureka
      #集群版本
      #defaultZone: http://localhost:7001/eureka,http://localhost:7002/eureka
  instance:
    prefer-ip-address: true
    instance-id: springcloud-config

##rabbitmq相关配置,暴露bus刷新配置的端点
management:
  endpoints: #暴露bus刷新配置的端点
    web:
      exposure:
        include: 'bus-refresh'
```


#### 给springcloud-config-client-3355客户端添加消息总线支持

- POM

```xml
<!--添加消息总线RabbitMQ支持-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```

- YML
```yml
#rabbitmq相关配置
rabbitmq:
  host: localhost
  port: 5672
  username: guest
  password: guest

#服务注册到eureka地址
eureka:
  client:
    service-url:
      #配置单机版
      defaultZone: http://localhost:7001/eureka
      #集群版本
      #defaultZone: http://localhost:7001/eureka,http://localhost:7002/eureka
  instance:
    prefer-ip-address: true
    instance-id: springcloud-config

##rabbitmq相关配置,暴露bus刷新配置的端点
management:
  endpoints: #暴露bus刷新配置的端点
    web:
      exposure:
        include: "*"
```


#### 给springcloud-config-client-3366客户端添加消息总线支持

- POM

```xml
<!--添加消息总线RabbitMQ支持-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```

- YML
```yml
#rabbitmq相关配置
rabbitmq:
  host: localhost
  port: 5672
  username: guest
  password: guest

#服务注册到eureka地址
eureka:
  client:
    service-url:
      #配置单机版
      defaultZone: http://localhost:7001/eureka
      #集群版本
      #defaultZone: http://localhost:7001/eureka,http://localhost:7002/eureka
  instance:
    prefer-ip-address: true
    instance-id: springcloud-config

##rabbitmq相关配置,暴露bus刷新配置的端点
management:
  endpoints: #暴露bus刷新配置的端点
    web:
      exposure:
        include: "*"
```


#### 测试

- 发送POST请求 实现一次发送,处处生效(通过服务端去通知所有的客户端)

curl -X POST "http://localhost:3344/actuator/bus-refresh"

实现了自动版全局广播的动态刷新配置


- 配置中心
   http://config-3344.com:3344/springcloudconfig-dev.yml


- 客户端
   http://localhost:3355/configInfo
   
   http://localhost:3366/configInfo

>获取配置信息,发现都已经刷新了   

成功


>注意：rabbitMQ默认有一个topic叫springCloudBus

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829114800537-646232301.png)



#### 一次修改,广播通知,处处生效



### SpringCloud Bus动态刷新定点通知
#### Bus动态刷新定点通知(不想全部通知  指定某一个实例生效)

公式: curl -X POST "http://localhost:配置中心的端口号/actuator/bus-refresh/{destination}"

/bus-refresh 请求不再发送到具体的服务实例上,而是发给config server并通过 destination参数类指定需要更新配置的服务或实例

只通知3355配置刷新
公式：curl -X POST "http://localhost:配置中心的端口号/actuator/bus-refresh/微服务的名称:微服务的端口号"

案例：curl -X POST "http://localhost:3344/actuator/bus-refresh/springcloudconfig-client:3355"


>简单一句话：指定某一个实例生效而不是全部



#### 通知总结All
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200829115305232-1778120103.png)
