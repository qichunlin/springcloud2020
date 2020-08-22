## 10.Hystrix断路器
https://www.bilibili.com/video/BV18E411x7eT?p=47
https://www.bilibili.com/video/BV18E411x7eT?p=48
https://www.bilibili.com/video/BV18E411x7eT?p=49
https://www.bilibili.com/video/BV18E411x7eT?p=50



### 概述

#### 分布式系统面临的问题是什么

复杂分布式体系结构中的应用程序有数十个依赖关系,每个依赖关系在某些时候将不可避免地失败。

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200821090804289-533974559.png)


服务雪崩
多个微服务之间调用的时候,假设微服务A调用微服务B和微服务C,微服务B和微服务C又调用其它的微服务,这就是所谓的T扇出"。如果扇出的链路上某个微服务的调用响应时间过长或者不可用,对微服务A的调用就会占用越来越多的系统资源,进而引起系统崩溃,所谓的“雪崩效应".
对于高流量的应用来说,单一的后端依赖可能会导致所有服务器上的所有资源都在几秒钟内饱和。比失败更糟糕的是,这些应用程序还可能导致服务之间的延迟增加,备份队列,线程和其他系统资源紧张,导致整个系统发生更多的级联故障。这些都表示需要对故障和延迟进行隔离和管理,以便单个依赖关系的失败,不能取消整个应用程序或系统。
所以,通常当你发现一个模块下的某个实例失败后,这时候这个模块依然还会接收流量,然后这个有问题的模块还调用了其他的模块,这样就会发生级联故障,或者叫雪崩。

#### 能干嘛

Hystrix是一个用于处理分布式系统的延迟和容错的开源库,在分布式系统里,许多依赖不可避免的会调用失败,比如超时、异常等,Hystrix能够保证在一个依赖出问题的情况下,不会导致整体服务失败,避免级联故障,以提高分布式系统的弹性。
“断路器”本身是一种开关装置,当某个服务单元发生故障之后,通过断路器的故障监控（类似熔断保险丝）,向调用方返回一个符合预期的、可处理的备选响应（FallBack）,而不是长时间的等待或者抛出调用方无法处理的异常,这样就保证了服务调用方的线程不会被长时间、不必要地占用,从而避免了故障在分布式系统中的蔓延,乃至雪崩。

服条降级
服务熔断
接近实时的监控


#### 官网资料

[使用官网](https://github.com/Netflix/Hystrix/wiki/How-To-Use)


#### Hystrix官宣,停更进维

https://github.com/Netflix/Hystrix

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200822094707772-2130484591.png)


被动修复bugs
不再接受合并请求
不再发布新版本


### Hystrix重要概念
#### 服务降级(fallback)

服务器忙，请稍后再试，不让客户端等待并立刻返回一个友好提示，fallback

哪些情况会出发降级
    程序运行异常
    超时
    服务熔断触发服务降级
    线程池/信号量打满也会导致服务降级



#### 服务熔断(break)

类比保险丝达到最大服务访问后，直接拒绝访问，拉闸限电，然后调用服务降级的方法并返回友好提示

就是保险丝 (服务的降级->进而熔断->恢复调用链路)


#### 服务限流(flowlimit)

秒杀高并发等操作，严禁一窝蜂的过来拥挤，大家排队，一秒钟N个，有序进行


### hystrix案例
#### 构建

- 新建springcloud-provider-hystrix-payment8001

- POM
```xml
<dependencies>
    <!--hystrix-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    </dependency>
    <!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
    <dependency>
        <groupId>com.qcl.springcloud</groupId>
        <artifactId>springcloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <!--eureka client-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <!--web-->
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
  port: 8001

spring:
  application:
    name: cloud-provider-hystrix-payment

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      #defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
      defaultZone: http://eureka7001.com:7001/eureka
```
- 主启动

**com.qcl.springcloud.PaymentHystrixMain8001**

- 业务类

service

**com.qcl.springcloud.service.PaymentService**


controller

**com.qcl.springcloud.controller.PaymentController**



- 正常测试

启动eureka7001

启动springcloud-provider-hystrix-payment8001

访问
    http://localhost:8001/payment/hystrix/ok/1
    http://localhost:8001/payment/hystrix/timeout/1

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200822101406893-833028207.png)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200822101423159-998202661.png)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200822101432172-666604551.png)


上述module均OK
    以上述为根基平台，从正确->错误->降级熔断->恢复

高并发测试
故障现象和导致原因
上诉结论
如何解决？解决的要求
(服务降级)
(服务熔断)
(服务限流)


### hystrix工作流程
### 服务监控hystrixDashboard