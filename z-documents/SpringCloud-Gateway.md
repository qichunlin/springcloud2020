## 11.Zuul路由网关

zuul是netflix 公司的
这一节不说

### 概述简介
### 路由基本配置
### 路由访问映射规则
### 查看路由信息
### 过滤器


## 12.Gateway新一代网关 (Spring社区)
https://www.bilibili.com/video/BV18E411x7eT?p=65
https://www.bilibili.com/video/BV18E411x7eT?p=66
https://www.bilibili.com/video/BV18E411x7eT?p=67
https://www.bilibili.com/video/BV18E411x7eT?p=68
https://www.bilibili.com/video/BV18E411x7eT?p=69
https://www.bilibili.com/video/BV18E411x7eT?p=70
https://www.bilibili.com/video/BV18E411x7eT?p=71
https://www.bilibili.com/video/BV18E411x7eT?p=72
https://www.bilibili.com/video/BV18E411x7eT?p=73


### 概述简介
#### 官网

[上一代zuul 1.X](https://github.com/Netflix/zuul/wiki)
[当前SpringCloudGateway官网](https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.1.RELEASE/reference/html)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827160149725-1042110581.png)


#### 是什么
Cloud全家桶中有个很重要的组件就是网关,在1.x版本中都是采用的Zuul网关；
但在2.x版本中,zuul的升级一直跳票,SpringCloud最后自己研发了一个网关替代Zuul,
那就是SpringCloud Gateway 一句话：gateway是原zuul1.x版的替代

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827160913171-213767697.png)



Gateway是在Spring生态系统之上构建的API网关服务,基于Spring 5,Spring Boot 2和Project Reactor等技术。
Gateway旨在提供一种简单而有效的方式来对API进行路由,以及提供一些强大的过滤器功能,例如：熔断、限流、重试等
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827161038318-1757787408.png)


SpringCloud Gateway 是Spring Cloud的一个全新项目,基于Spring 5.0+Spring Boot 2.0和Project Reactor 等技术开发的网关,它旨在为微服务架构提供一种简单有效的统一的API路由管理方式。
SpringCloud Gateway 作为Spring Cloud 生态系统中的网关,目标是替代Zuul,在Spring Cloud2.0以上版本中,没有对新版本的Zuul2.0以上最新高性能版本进行集成,仍然还是使用的Zuul 1.x非Reactor模式的老版本。而为了提升网关的性能,SpringCloud Gateway是基于WebFlux框架实现的,而WebFlux框架底层则使用了高性能的Reactor模式通信框架Netty。
Spring Cloud Gateway的目标提供统一的路由方式且基于Filter链的方式提供了网关基本的功能,例如：安全,监控/指标,和限流。

`源码架构`
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827165550681-561520881.png)


Webflux和reactor-netty非阻塞式响应式编程高性能框架

>SpringCloud Gateway 使用的Webflux中的reactor-netty响应式编程组件,底层使用了Netty通讯框架。


#### 能干嘛
反向代理

鉴权

流量控制

熔断

日志监控


#### 微服务架构中网关在哪里
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827165848869-1930356792.png)


#### 有Zuul了怎么又出了gateway
##### 我们为什么选择Gateway?

- Netflix不太靠谱、Zuul2.0一直跳票,迟迟不发布

  一方面因为Zuul1.0已经进入了维护阶段,而且Gateway是SpringCloud团队研发的,是亲儿子产品,值得信赖。
而且很多功能Zuul都没有用起来也非常的简单便捷。
Gateway是基于`异步非阻塞模型`上进行开发的,性能方面不需要担心。虽然Netflix早就发布了最新的Zuul2.x,但Spring Cloud 貌似没有整合计划。而且Netflix相关组件都宣布进入维护期；不知前景如何？
多方面综合考虑Gateway是很理想的网关选择。


- Spring Cloud Gateway 具有如下特性：

   基于Spring Framework 5,Project Reactor 和Spring Boot 2.0进行构建；
   动态路由：能够匹配任何请求属性；可以对路由指定 Predicate(断言)和Filter(过滤器)；
   集成Hystrix的断路器功能；
   集成Spring Cloud 服务发现功能；
   易于编写的Predicate(断言)和Filter(过滤器)；
   请求限流功能；支持路径重写。


- Spring Cloud Gateway与Zuul的区别

  在SpringCloud Finchley正式版之前,Spring Cloud 推荐的网关是Netflix提供的Zuul：
    1、Zuul 1.x,是一个基于阻塞l/O的API Gateway
    2、Zuul 1.x基于Servlet 2.5使用阻塞架构它不支持任何长连接(如WebSocket)Zuul的设计模式和Nginx较像,每次I/O操作都是从工作线程中选择一个执行,请求线程被阻塞到工作线程完成,但是差别是Nginx用C++实现,Zuul用Java实现,而JVM本身会有第一次加载较慢的情况,使得Zuul的性能相对较差。
    3、Zuul 2.x理念更先进,想基于Netty非阻塞和支持长连接,但SpringCloud目前还没有整合。Zuul2.x的性能较Zuul1.x有较大提升
。在性能方面,根据官方提供的基准测试,Spring Cloud Gateway的RPS(每秒请求数)是Zuul的1.6倍。
    4、Spring Cloud Gateway 建立在Spring Framework 5、Project Reactor 和Spring Boot2之上,使用非阻塞API。
    5、Spring Cloud Gateway还支持WebSocket,并且与Spring紧密集成拥有更好的开发体验



##### Zuul 1.x模型

  SpringCloud中所集成的Zuul版本,采用的是Tomcat容器,使用的是传统的Servlet IO处理模型。
  Servlet的生命周期？servlet由servlet container进行生命周期管理。
      container启动时构造servlet对象并调用servlet init() 进行初始化；
      container运行时接受请求,并为每个请求分配一个线程(一般从线程池中获取空闲线程)然后调用service()。
      container关闭时调用servlet destory() 销毁servlet；

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827171128896-1632606957.png)


- 上述模式的缺点：
   
   servlet是一个简单的网络IO模型,当请求进入servlet container时,servlet container就余为其绑定一个线程,在并发不高的场景下这种模型是适用的。
但是一旦高并发(比如用jmeter压),线程数量就会上涨,而线程资源代价是昂贵的(上线文切换,内存消耗大)严重影响请求的处理时间。在一些简单业务场景下,不
希望为每个request分配一个线程,只需要1个或几个线程就能应对极大并发的请求,这种业务场景下servlet模型没有优势
   
   所以Zuul 1.X是基于servlet之上的一个阻塞式处理模型,即spring实现了处理所有request请求的一个servlet(DispatcherServlet)并由该servlet阻
塞式处理处理。所以Springcloud Zuul无法摆脱servlet模型的弊端。


##### Gateway模型

- WebFlux是什么
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827172421440-1961176942.png)

[SpringWebFlux官网](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827172707734-287431704.png)


- 说明

   传统的Web框架,比如说：struts2,springmvc等都是基于Servlet API与Servlet容器基础之上运行的。
在Servlet3.1之后有了异步非阻塞的支持。而WebFlux是一个典型非阻塞异步的框架,它的核心是基于Reactor的相关API实现的。相对于传统的web框架来说,它可以运行在诸如Netty,Undertow及支持Servlet3.1的容器上。非阻塞式+函数式编程（Spring5必须让你使用用java8）
Spring WebFlux 是Spring 5.0引入的新的响应式框架,区别于Spring MVC,它不需要依赖ServletAPI,它是完全异步非阻塞的,并且基于Reactor 来实现响应式流规范。


### 三大核心概念
#### Route(路由)

路由是构建网关的基本模块,它由ID,目标URI,一系列的断言和过滤器组成,如果断言为true则匹配该路由


#### Predicate(断言)

参考的是Java8的java.util.function.Predicate开发人员可以匹配HTTP请求中的所有内容（例如请求头或请求参数）,如果请求与断言相匹配则进行路由


#### Filter(过滤器)

指的是Spring框架中GatewayFilter的实例,使用过滤器,可以在请求被路由前或者之后对请求进行修改。


#### 总体

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827173415965-1129100075.png)


>web请求,通过一些匹配条件,定位到真正的服务节点。并在这个转发过程的前后,进行一些精细化控制。
>predicate就是我们的匹配条件；
>而filter,就可以理解为一个无所不能的拦截器。有了这两个元素,再加上目标uri,就可以实现一个具体的路由了


### Gateway工作流程
#### 官网总结

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827173640257-401140288.png)

- 客户端向 Spring Cloud Gateway 发出请求。然后在Gateway Handler Mapping 中找到与请求相匹配的路由,将其发送到Gateway Web Handler。
- Handler 再通过指定的过滤器链来将请求发送到我们实际的服务执行业务逻辑,然后返回。
- 过滤器之间用虚线分开是因为过滤器可能会在发送代理请求之前（“pre"）或之后（“post"）执行业务逻辑。


>Filter在“pre"类型的过滤器可以做参数校验、权限校验、流量监控、日志输出、协议转换等,
>在“post"类型的过滤器中可以做响应内容、响应头的修改,日志的输出,流量监控等有着非常重要的作用。


#### 核心逻辑

>路由转发+执行过滤器链


### 入门配置
#### 新建Module (springcloud-gateway-gateway9527)


#### POM
```xml
<dependencies>
    <!--gateway-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <!--eureka-client-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <!-- 引入自己定义的api通用包,可以使用Payment支付Entity -->
    <dependency>
        <groupId>com.qcl.springcloud</groupId>
        <artifactId>springcloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <!--一般基础配置类-->
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


#### YML
```yml
server:
  port: 9527

#第一种路由网关配置
spring:
  application:
    name: springcloud-gateway

eureka:
  instance:
    hostname: springcloud-gateway-service
    prefer-ip-address: true
  client: #服务提供者provider注册进eureka服务列表内
    service-url:
      register-with-eureka: true
      fetch-registry: true
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
```


#### 业务类

无

#### 主启动类

**com.qcl.springcloud.GateWayMain9527**


#### 9527网关如何做路由映射那???

springcloud-provider-payment8001看看controller的访问地址 (-->com.qcl.springcloud.controller.PaymentController 的 query    lb)  
我们目前不想暴露8001端口,希望在8001外面套一层9527


#### YML新增网关配置
```yml
spring:
  application:
    name: springcloud-gateway
  cloud:
    gateway:
      routes:
        - id: payment_routh #payment_route    #路由的ID,没有固定规则但要求唯一,建议配合服务名
          uri: http://localhost:8001          #匹配后提供服务的路由地址
          predicates:
            - Path=/api/payment/query/**         # 断言,路径相匹配的进行路由

        - id: payment_routh2 #payment_route    #路由的ID,没有固定规则但要求唯一,建议配合服务名
          uri: http://localhost:8001          #匹配后提供服务的路由地址
          predicates:
            - Path=/api/payment/lb/**         # 断言,路径相匹配的进行路由   http://localhost:9527/api/payment/lb
                     
```


#### 测试

启动7001

启动8001 (springcloud-provider-payment8001)

启动9527网关
访问说明
  添加网关前 http://localhost:8001/api/payment/query/1
  添加网关后 http://localhost:9527/api/payment/query/1

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827181926691-1181543418.png)


![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827181911694-759702066.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827220613904-1170895891.png)




报错 `Spring MVC found on classpath, which is incompatible with Spring Cloud Gateway at this time. Please remove spring-boot-starter-web dependency.`


解决方案
```

<!--Springcloudgateway不需要web相关模块启动会报错(特别注意)-->
<!--web启动器-->
<!--<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>-->
<!--监控-->
<!-- <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>-->
```

#### YML配置说明

Gateway网关路由有两种配置方式

  1.在配置文件yml中配置 (参考前面的步骤)
  
  2.代码中注入RouteLocator的Bean
     官网案例
     ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827214651590-684072594.png)
     
   百度国内新闻网址,需要外网 (http://news.baidu.com/guonei)
   自己写一个 (百度新闻---业务需求[通过9527网关访问到外网的百度新闻地址]---编码[com.qcl.springcloud.config.GatewayConfig])

http://localhost:9527/guonei

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827220658339-412235393.png)


http://localhost:9527/guoji

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827220842330-2050082598.png)



### 通过微服务名实现动态路由

大致效果图

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827221229758-952009663.png)


  默认情况下Gateway会根据注册中心注册的服务列表,以注册中心上微服务名为路径创建动态路由进行转发,从而实现动态路由的功能


  启动：一个eureka7001 + 两个服务提供者8001/8002

  POM (添加依赖：spring-cloud-starter-netflix-eureka-client)
    

  YML
```yml
#需要注意的是uri的协议为lb,表示启用Gateway的负载均衡功能。
# lb://serviceName是spring cloud gateway在微服务中自动为我们创建的负载均衡uri

spring:
  application:
    name: springcloud-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true #开启从注册中心动态创建路由的功能,利用微服务名进行路由
      routes:
        - id: payment_routh #payment_route    #路由的ID,没有固定规则但要求唯一,建议配合服务名
          #uri: http://localhost:8001          #匹配后提供服务的路由地址
          uri: lb://springcloud-payment-service #匹配后提供服务的路由地址  http://localhost:9527/api/payment/query/1
          predicates:
            - Path=/api/payment/query/**         # 断言,路径相匹配的进行路由

        - id: payment_routh2 #payment_route    #路由的ID,没有固定规则但要求唯一,建议配合服务名
          #uri: http://localhost:8001          #匹配后提供服务的路由地址
          uri: lb://springcloud-payment-service #匹配后提供服务的路由地址
          predicates:
            - Path=/api/payment/lb/**         # 断言,路径相匹配的进行路由   http://localhost:9527/api/payment/lb
```  


   测试：http://localhost:9527/payment/lb  8001/8002两个端口切换



### Predicate的使用
#### 是什么

启动gateway9527微服务会发现一些日志：

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827222815708-1242889213.png)


#### Route Predicate Factories这个是什么

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827223225010-1682118408.png)


Spring Cloud Gateway将路由匹配作为Spring WebFlux HandlerMapping基础架构的一部分。
Spring Cloud Gateway包括许多内置的Route Predicate工厂。所有这些Predicate都与HTTP请求的不同属性匹配。多个Route Predicate工厂可以进行组合
Spring Cloud Gateway 创建 Route对象时,使用RoutePredicateFactorx创建 Predicate对象,Predicate对象可以赋值给Route。Spring Cloud Gateway 包含许多内置的Route Predicate Factories。
所有这些谓词都匹配HTTP请求的不同属性。多种谓词工厂可以组合,并通过逻辑and。



#### 常用的 Route Predicate 

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827223413839-530693013.png)

- 1.After Route Predicate  (- After=2020-04-22T12:51:37.485+08:00[Asia/Shanghai]) -->(通过java8自带的生成时区为 After节点  参考类TestZone)


- 2.Before Route Predicate (- Before=2020-04-22T12:51:37.485+08:00[Asia/Shanghai])


- 3.Between Route Predicate (- Betwen=2020-04-22T12:51:37.485+08:00[Asia/Shanghai],2020-04-25T12:51:37.485+08:00[Asia/Shanghai])


- 4.Cookie Route Predicated (不带cookies访问、带cookies访问)

Cookie Route Predicate需要两个参数,一个是Cookie name,一个是正则表达式。
路由规则会通过获取对应的Cookie name 值和正则表达式去匹配,如果匹配上就会执行路由,如果没有匹配上则不执行

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827224144411-1167316043.png)

测试访问：curl http://localhost:9527/api/payment/lb --cookie "username=qcl"


- 5.Header Route Predicate (- Header=X-Request-Id, \d+)

两个参数：一个是属性名称和一个正则表达式,这个属性值和正则表达式匹配则执行。

测试访问：curl http://localhost:9527/api/payment/lb --cookie "username=qcl" -H "X-Request-Id:123"


- 6.Host Route Predicate (curl http://localhost:9527/api/payment/lb?username=31 -H "Host: www.qcl.com")


- 7.Method Route Predicate (- Method=GET)


- 8.Path Route Predicate (- Path=/api/payment/lb/**)


- 9.Query Route Predicate (- Query=username, \d+)

测试访问：http://localhost:9527/api/payment/lb?username=1


 
### Filter的使用
#### 是什么

路由过滤器可用于修改进入的HTTP请求和返回的HTTP响应，路由过滤器只能指定路由进行使用。
Spring Cloud Gateway 内置了多种路由过滤器，他们都由GatewayFilter的工厂类来产生


#### SpringCloudGateway的Filter

生命周期 Only Two 
   pre
   post
   
种类 Only Two
  GatewayFilter  [GatewayFilter官网使用说明地址](https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.1.RELEASE/reference/html/#gatewayfilter-factories)
  GlobalFilter   [GlobalFilter官网使用说明地址](https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.1.RELEASE/reference/html/#global-filters)


#### 常用的GatewayFilter

AddRequestParameter


修改YML
```yml

#常用的GatewayFilter 案例
filters:
- AddRequestParameter=X-Request-Id, 1024 #过滤器工厂会在匹配的请求头加上一对请求头，名称为×-Request-Id值为1024

```



#### 自定义过滤器
##### 自定义全局GlobalFilter

- 两个主要接口介绍 (implements GlobalFilter,Ordered)

- 能干嘛 (全局日志记录、统一网关鉴权)

- 案例代码

**com.qcl.springcloud.filter.MyLogGatewayFilter**


- 测试
http://localhost:9527/api/payment/query/1 (错误)
http://localhost:9527/api/payment/query/1?uname=qcl  (正确)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827220419344-1354894220.png)
