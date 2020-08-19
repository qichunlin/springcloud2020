## 5.Eureka的注册与发现
https://www.bilibili.com/video/BV18E411x7eT?p=15
https://www.bilibili.com/video/BV18E411x7eT?p=16
https://www.bilibili.com/video/BV18E411x7eT?p=17
https://www.bilibili.com/video/BV18E411x7eT?p=18
https://www.bilibili.com/video/BV18E411x7eT?p=19
https://www.bilibili.com/video/BV18E411x7eT?p=20
https://www.bilibili.com/video/BV18E411x7eT?p=21
https://www.bilibili.com/video/BV18E411x7eT?p=22
https://www.bilibili.com/video/BV18E411x7eT?p=23
https://www.bilibili.com/video/BV18E411x7eT?p=24
https://www.bilibili.com/video/BV18E411x7eT?p=25
https://www.bilibili.com/video/BV18E411x7eT?p=26
https://www.bilibili.com/video/BV18E411x7eT?p=27


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
的负载均衡器。在应用启动后,将会向Eureka Server发送心跳(默认周期为30秒)。如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳,EurekaServer将会从服务注册表中把这个服务节点移除(默认90秒)


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
    #false表示自己就是注册中心,我的职责就是维护服务实例,并不区检索服务
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
<!-- 以前老版本,别再使用 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>

<!--现在新版本,当前使用 -->
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
    #是否从EurekaServer抓取已有的注册信息,默认为true,单节点无所谓,集群必须设置为true才能配合ribbon使用负载均衡
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

#### Eureka集群原理说明

Eureka Server端 (7001)
    服务注册：将服务信息注册进注册中心
    服务发现：从注册中心上获取服务信息
    实质：存key服务命取value调用地址


Service Provider (8001,80)


Eureka的工作流程
    1.先启动eureka注册中心
    2.启动服务提供者payment支付服务3支付服务启动后会把自身信息(比如服务地址以别名方式注册进eureka)
    4.消费者order服务在需要调用接口时,使用服务别名去注册中心获取实际的RPC远程调用地址5消费者获得调用地址后,底层实际是利用HttpClient技术一实现远程调用
    6.消费者获得服务地址后会缓存在本地jvm内存中,默认每间隔30秒更新一次服务调用地址

原理图

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819131035795-1971759339.png)



问题：微服务RPC远程服务调用最核心的是什么?
    高可用,试想你的注册中心只有一个only one,它出故障了那就会导致整个为服务环境不可用,所以
    解决办法：搭建Eureka注册中心集群,实现负载均衡+故障容错


#### Eureka Server集群环境构建步骤
- 建Module (参考 springcloud-eureka-server7001  新建Module》 (springcloud-eureka-server7002)

- 改POM

(直接赋值7001模块的pom文件)

- 修改映射配置

找到C:\Windows\System32\drivers\etc路径下的 host文件
修改映射配置添加进hosts文件
12.0.0.1 eureka7001.com
12.0.0.1 eureka7002.com



- 写YML(以前单机)
```yml
server:
  port: 7001

eureka:
  instance:
    hostname: localhost #eureka服务端实例名称
  client:
    #表示不向注册中心注册自己
    register-with-eureka: false
    #false表示自己就是注册中心,我的职责就是维护服务实例,并不区检索服务
    fetch-registry: false
    service-url:
      #设置与Eureka Server交互的地址查询服务和注册服务都要依赖这个地址
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```


7001

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
    #false表示自己就是注册中心,我的职责就是维护服务实例,并不区检索服务
    fetch-registry: false
    service-url:
      #设置与Eureka Server交互的地址查询服务和注册服务都要依赖这个地址
      defaultZone: http://eureka7002.com:7002/eureka/
```

7002

```yml
server:
  port: 7002

eureka:
  instance:
    hostname: eureka7002.com #eureka服务端实例名称
  client:
    #表示不向注册中心注册自己
    register-with-eureka: false
    #false表示自己就是注册中心,我的职责就是维护服务实例,并不区检索服务
    fetch-registry: false
    service-url:
      #设置与Eureka Server交互的地址查询服务和注册服务都要依赖这个地址
      #defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/  #单机版
      defaultZone: http://eureka7001.com:7001/eureka/
```


- 主启动

**com.qcl.springcloud.EurekaMain7002**


![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200818234417996-1699792700.png)



#### 将支付服务8001微服务发布到两台Eureka集群配置中

修改配置yml

```yml
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/   #集群版
```

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819132422831-859711356.png)



#### 将订单服务80微服务发布到两台Eureka集群配置中

同上配置


#### 测试01

先要启动EurekaServer,7001/7002服务

再要启动服务提供者provider,8001

再要启动消费者,80

http:/localhost/consumer/api/payment/get/1



#### 支付服务8001服务提供者集群环境搭建

- 建Module 参考springcloud-provider-payment8001 新建springcloud-provider-payment8002

- 改POM

(直接拷贝8001的pom文件)

- 写YML

(直接拷贝8001的yml文件,修改端口号即可)

- 主启动类

**com.qcl.springcloud.PaymentMain8002**

- 业务类

**com.qcl.springcloud.controller.PaymentController**


- 修改8001/8002的Controller

```
/**
 * 因为做了支付服务的集群,applicationName是一样的,但是可以通过端口号来区别是哪个服务
 */
@Value("${server.port}")
private String serverPort;
```


#### 负载均衡

- bug 订单服务访问地址不能写死

```
//public static final String PAYMENT_URL = "http://localhost:8001";
public static final String PAYMENT_URL = "http://SPRINGCLOUD-PAYMENT-SERVICE";
```

>注意：500错误 未知服务异常 (SPRINGCLOUD-PAYMENT-SERVICE)


- 使用@LoadBalance注解赋予RestTemplate负载均衡的能力

- ApplicationContextBean

**com.qcl.springcloud.config.ApplicationContextConfig**


#### 测试02

先要启动EurekaServer,7001/7002服务

再要启动服务提供者provider,8001/8002服务

http:/localhost/consumer/api/payment/get/1


结果
    负载均衡效果达到
    8001/8002端口交替出现

>Ribbon和Eureka整合后Consumer可以直接调用服务而不再关心地址和端口号,且该服务还有负载功能 (public static final String PAYMENT_URL = "http://SPRINGCLOUD-PAYMENT-SERVICE";)


### actuator微服务信息完善
#### 主机名称：服务名称修改

当前问题：含有主机名称


修改 springcloud-provider-payment8001
yml
修改部分
```yml
#服务名称
  instance:
    instance-id: payment8001
```

修改之后

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819211721790-1823384899.png)


查看为服务的健康状态

localhost:8001/actuator/health



#### 访问信息有IP信息提示

当前问题：没有IP提示

修改部分
```yml

#访问路径显示IP地址
    prefer-ip-address: true
```

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819211721790-1823384899.png)


>8001或者其他微服务如果需要上面两个配置直接加入配置文件和引入actuator 依赖就能实现


### 服务发现Discovery

#### 对于注册进入Eureka里面的微服务,可以通过服务发现来获得该服务的信息
比如端口 服务名称等等

#### 修改 springcloud-provider-payment8001的Controller

com.qcl.springcloud.controller.PaymentController


```
/**
 * 对外暴露的服务发现的一些信息
 */
@Resource
private DiscoveryClient discoveryClient;

@GetMapping("/discovery")
public Object discovery() {
    //获取EurekaServer上面所有的Application
    List<String> discoveryClientServices = discoveryClient.getServices();
    //SPRINGCLOUD-ORDER-SERVICE  SPRINGCLOUD-PAYMENT-SERVICE
    for (String application : discoveryClientServices) {
        log.info("******application******:{}", application);
    }

    //根据服务名称获取对应的实例信息
    List<ServiceInstance> discoveryClientInstances = discoveryClient.getInstances("SPRINGCLOUD-PAYMENT-SERVICE");
    // payment8002 , payment8001
    for (ServiceInstance serviceInstance : discoveryClientInstances) {
        log.info("******serviceInstance******:{}", serviceInstance);
        log.info("实例名称:{},主机名称:{},端口号:{},URI地址:{}",
                serviceInstance.getServiceId(),
                serviceInstance.getHost(),
                serviceInstance.getPort(),
                serviceInstance.getUri()
        );
    }

    return this.discoveryClient;
}

```


#### 8001主启动类

@EnableDiscoveryClient

**com.qcl.springcloud.PaymentMain8001**

测试

http://localhost:8001/api/payment/discovery


### Eureka自我保护

#### 故障现象

概述

  保护模式主要用于一组客户端和Eureka Server之间存在网络分区场景下的保护。一旦进入保护模式,
Eureka Server将会尝试保护其服务注册表中的信息,不再删除服务注册表中的数据,也就是不会注销任何微服务。
如果在Eureka Server的首页看到以下这段提示,则说明Eureka进入了保护模式：
EMERGENCY！EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT.
RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE



#### 导致原因

>一句话：某时刻某一个微服务不可用了,Eureka不会立刻清理,依旧会对该微服务的信息进行保存属于CAP里面的AP分支


为什么会产生Eureka自我保护机制？
  为了防EurekaClient可以正常运行,但是与EurekaServer网络不通情况下,EurekaServer不会立刻将EurekaClient服务剔除什么是白我保护模式？
默认情况下,如果EurekaServer在一定时间内没有接收到某个微服务实例的心跳,EurekaServer将会注销该实例(默认90秒)。但是当网络分区故障发生
(延时、卡顿、拥挤)时,微服务与EurekaServer之间无法正常通信,以上行为可能变得非常危险了——因为微服务本身其实是健康的,此时本不应该注
销这个微服务。Eureka通过“自我保护模式”来解决这个问题——当EurekaServer节点在短时间内丢失过多客户端时(可能发生了网络分区故障),那么
这个节点就会进入自我保护模式。

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819214042183-921761908.png)



在自我保护模式中,Eureka Server会保护服务注册表中的信息,不再注销任何服务实例。
它的设计哲学就是宁可保留错误的服务注册信息,也不盲目注销任何可能健康的服务实例。一句话讲解：好死不如赖活着综上,
自我保护模式是一种应对网络异常的安全保护措施。它的架构哲学是宁可同时保留所有微服务(健康的微服务和不健康的微服务
都会保留)也不盲目注销任何健康的微服务。使用自我保护模式,可以让Eureka集群更加的健壮、稳定。


#### 怎么禁止自我保护

- 注册中心eureakeServer端7001
  出厂默认,自我保护机制是开启的(eureka.server.enable-self-preservation=true)
  使用eureka.server.enable-self-preservation=false 可以禁用自我保护模式
  
  在eurekaServer端7001处设置关闭自我保护机制

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819215721032-1763613845.png)


- 生产者客户端eureakeClient端8001

默认
eureka.instance.lease-renewal-interval-in-seconds=30单位为秒(默认是30秒)
eureka.instance.lease-expiration-duration-in-seconds=90单位为秒(默认是90秒)

配置
```yml
instance:
    instance-id: payment8001
    #访问路径可以显示IP地址
    prefer-ip-address:true
    #Eureka客户端向服务端发送心跳前时间间隔，单位为（默认是30秒）
    lease-renewal-interval-in-seconds: 1
    #Eureka服务端在收到最后一次心跳后等待时间上限，单位为（默认是90秒，超时将剔除服务
    lease-expiration-duration-in-seconds: 2
```

- 测试

先关闭8001,发现马上被剔除


### Eureka停更说明

https://github.com/Netflix/eureka/wiki