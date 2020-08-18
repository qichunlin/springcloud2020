## 5.Eureka的注册与发现
https://www.bilibili.com/video/BV18E411x7eT?p=15
https://www.bilibili.com/video/BV18E411x7eT?p=16
https://www.bilibili.com/video/BV18E411x7eT?p=17
https://www.bilibili.com/video/BV18E411x7eT?p=18
https://www.bilibili.com/video/BV18E411x7eT?p=19


### Eureka基础知识

#### 什么是服务治理
  Spring Cloud 封装了Netflix公司开发的Eureka模块来实现服务治理在传统的rpc远程调用框架中,
管理每个服务与服务之间依赖关系比较复杂,管理比较复杂,所以需要使用服务治理,管理服务于服务之
间依赖关系,可以实现服务调用、负载均衡、容错等,实现服务发现与注册。


#### 什么是服务注册
  Eureka采用了CS的设计架构,Eureka Server作为服务注册功能的服务器,它是服务注册中心。
而系统中的其他微服务,使用Eureka的客户端连接到Eureka Server并维持心跳连接。这样系统的
维护人员就可以通过Eureka Server来监控系统中各个微服务是否正常运行。

  在服务注册与发现中,有一个注册中心。当服务器启动的时候,会把当前自己服务器的信息比如服
务地址通讯地址等以别名方式注册到注册中心上。另一方(消费者服务提供者),以该别名的方式去注
册中心上获取到实际的服务通讯地址,然后再实现本地RPC调用RPC远程调用框架核心设计思想:在于
注册中心,因为使用注册中心管理每个服务与服务之间的一个依赖关系(服务治理概念)。在任何rpc
远程框架中,都会有一个注册中心(存放服务地址相关信息(接口地址))


下左图是Eureka系统架构,右图是Dubbo的架构
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200817223104206-756851358.png)


#### Eureka两组件

Eureka包含两个组件：Eureka Server和Eureka Client 

Eureka Server提供服务注册服务
    各个微服务节点通过配置启动后,会在EurekaServer中进行注册,这样EurekaServer中的服务注册表中将会存储所有
可用服务节点的信息,服务节点的信息可以在界面中直观看到。


EurekaClient通过注册中心进行访问
    是一个Java客户端,用于简化Eureka Server的交互,客户端同时也具备一个内置的、使用轮询(round-robin)负载算法
的负载均衡器。在应用启动后,将会向Eureka Server发送心跳(默认周期为30秒)。如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳,EurekaServer将会从服务注册表中把这个服务节点移除（默认90秒）


### 单机Eureka构建步骤
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200818231814412-1219936592.png)

#### 生成Eureka Server端服务注册中心 
- 建Module (springcloud-eureka-server7001)

![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403151852223-1404689738.png)

![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403151925460-1141631399.png)

- 改POM

**1.X 和 2.X 对比说明**
```xml
<!-- 以前的老版本(当前使用2018) -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>


<!--现在新版本(当前使用2020.2)-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
<!--自定义api通用包-->
<dependency>
    <groupId>com.qcl.springcloud</groupId>
    <artifactId>springcloud-api-commons</artifactId>
    <version>${project.version}</version>
</dependency>
<!--boot web actuator-->
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
```

- 改YML
```yml
server:
  port: 7001

#erueka集群原理:相互注册,相互守望
eureka:
  instance:
    hostname: eureka7001.com #eureka服务端实例名称
  client:
    #表示不向注册中心注册自己
    register-with-eureka: false
    #false表示自己就是注册中心，我的职责就是维护服务实例,并不区检索服务
    fetch-registry: false
    service-url:
      #设置与Eureka Server交互的地址查询服务和注册服务都要依赖这个地址
      defaultZone: http://eureka7002.com:7002/eureka/

  #禁用eureka的自我保护机制
#  server:
#    enable-self-preservation: false

```


- 主启动

com.qcl.springcloud.EurekaMain7001


- 测试

浏览器访问：http://localhost:7001/

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200818233057803-1818250154.png)

>没有服务注册进去



#### Eureka Client端springcloud-provider-payment8001将注册进Eureka Server成为服务提供者provider 

修改Module：springcloud-provider-payment8001


修改POM
**1.X 和 2.X 对比说明**
```xml
<!-- 以前老版本，别再使用 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>

<!--现在新版本，当前使用 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```


修改yml
```yml
spring:
  application:
    name: springcloud-payment-service #服务名称

eureka:
  client:
    #表示向注册中心注册自己 默认为true
    register-with-eureka: true
    #是否从EurekaServer抓取已有的注册信息，默认为true,单节点无所谓,集群必须设置为true才能配合ribbon使用负载均衡
    fetch-registry: true
    service-url:
      # 入驻地址
      defaultZone: http://localhost:7001/eureka/   #单机版
  #服务名称
  instance:
    instance-id: payment8001
    #访问路径显示IP地址
    prefer-ip-address: true
```


修改主启动

(添加注解 @EnableEurekaClient)


测试
先启动eureka server
http:localhost:7001
微服务注册名说明

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200818234028021-1921160779.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200818234015922-743492129.png)



##### 自我保护机制
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200818234123989-1788687786.png)


#### Eureka Client端springcloud-consumer-order80将注册进Eureka Server成为服务消费者consumer 

步骤跟 springcloud-provider-payment8001 一样也是使用eureka client端

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200818234425345-2022782382.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200818234417996-1699792700.png)



#### bug

Failed to bind properties under ' eureka.client.service-url' to java.util.Map <java.lang.String, java.lang.String>


解决办法 (层次缩进和空格,两者不能少)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200818234615782-395618468.png)



### 集群Eureka构建步骤
### actuator微服务信息完善
### 服务发现Discovery
### Eureka自我保护
