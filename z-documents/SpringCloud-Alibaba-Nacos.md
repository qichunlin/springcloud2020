## SpringCloud Alibaba Nacos 服务注册与配置中心
https://www.bilibili.com/video/BV18E411x7eT?p=96
https://www.bilibili.com/video/BV18E411x7eT?p=97
https://www.bilibili.com/video/BV18E411x7eT?p=98
https://www.bilibili.com/video/BV18E411x7eT?p=99
https://www.bilibili.com/video/BV18E411x7eT?p=100


### Nacos简介

#### 为什么叫Nacos
前四个字母分别为Naming(服务命令管理)和Configuration(配置)的前两个字母,最后的s为Service。


#### 是什么
- 一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。
- Nacos:Dynamic Naming and Configuration Service 
- Nacos就是注册中心+配置中心的组合(等价于 Nacos = Eureka + Config + Bus)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815155149198-761489899.png)


#### 能干嘛?
替代Eureka作为服务注册中心
替代Config作为服务配置中心


#### 去哪下
GitHub:https://github.com/com.qcl.springcloud.alibaba/Nacos

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815155719985-875781383.png)


同步使用Nacos的1.1.4版本
https://github.com/com.qcl.springcloud.alibaba/nacos/releases/tag/1.1.4



[官网文档](https://nacos.io/zh-cn/)



#### 各种注册中心比较
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815160403130-619077322.png)

据说Nacos 在阿里巴巴内部有超过10万的实例运行,已经过了类似双十一等各种大型流量的考验


### 安装并运行Nacos
1.本地Java8+Maven环境

2.先从官网下载Nacos

3.解压安装包,直接运行bin文件夹下面的startup.cmd

4.命令运行成功后直接访问 http://localhost:8848/nacos (默认账户密码都是 nacos)
http://localhost:8848/nacos/#/login

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815171525183-1015201847.png)


![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815171655715-1967580477.png)



### Nacos作为注册中心演示

#### 官方文档
https://spring-cloud-com.qcl.springcloud.alibaba-group.github.io/github-pages/hoxton/en-us/index.html
https://spring.io/projects/spring-cloud-com.qcl.springcloud.alibaba#learn

#### 基于Nacos的服务提供者
- 新建Module (springcloud-com.qcl.springcloud.alibaba-provider-payment9001)

- POM
    - 父工程POM
```xml
<!--spring cloud com.qcl.springcloud.alibaba 2.2.0.RELEASE-->
<dependency>
    <groupId>com.com.qcl.springcloud.alibaba.cloud</groupId>
    <artifactId>spring-cloud-com.qcl.springcloud.alibaba-dependencies</artifactId>
    <version>2.2.0.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
``` 
 
   本模块POM
```xml
<!--SpringCloud ailibaba nacos -->
<dependency>
    <groupId>com.com.qcl.springcloud.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-com.qcl.springcloud.alibaba-nacos-discovery</artifactId>
</dependency>
```  

- YML
```yml
server:
  port: 9001

spring:
  application:
    name: nacos-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #配置Nacos地址


# 把监控的东西暴露出来
management:
  endpoints:
    web:
      exposure:
        include: '*'

```

- 主启动类(com.qcl.springcloud.com.qcl.springcloud.alibaba.PaymentMain9001)


- 业务类(com.qcl.springcloud.com.qcl.springcloud.alibaba.controller.PaymentController)

- 测试
http://localhost:9001/payment/nacos/1

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815172543639-1191546568.png)


nacos控制台
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815172358229-1789544985.png)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815172431750-967839993.png)


>nacos服务注册中心+服务提供者9001已完成



- 为了演示nacos负载均衡,需要拷贝一份9001的微服务为9002

如果不想重复进行体力劳动,直接拷贝虚拟端口
端口虚拟映射实现： -DServer.port=9011
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815170218456-559063604.png)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815170315502-1483795178.png)


![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815172431750-967839993.png)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815172650822-1690936505.png)



#### 基于Nacos的服务消费者
- 新建Module (springcloud-alibaba-consumer-order83)

- POM
为什么nacos支持负载均衡
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815173816086-1165996614.png)

```xml
<!--SpringCloud ailibaba nacos -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>

```

- YML
```yml
server:
  port: 83

spring:
  application:
    name: nacos-order-consumer
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848

#消费者将要去访问的微服务名称(注册成功进nacos的微服务提供者)
service-url:
  nacos-user-service: http://nacos-payment-provider

```


- 主启动(com.qcl.springcloud.alibaba.OrderNacosMain83)

- 业务类
(com.qcl.springcloud.alibaba.config.ApplicationContextConfig)
(com.qcl.springcloud.alibaba.controller.OrderNacosController)

- 测试
nacos控制台
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815174915181-1764537321.png)


浏览器访问--> http://localhost:83/consumer/payment/nacos/999
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815175104225-1956244677.png)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815175111808-938700527.png)


>注意：如果出现下图的错误,则是少了@LoadBalanced 注解 因为是负载均衡
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815175249544-1171335457.png)




#### 服务注册中心对比
各种注册中心对比
- Nacos全景图 (https://nacos.io/zh-cn/docs/what-is-nacos.html)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815175855354-1094701376.png)
   
- nacos和CAP
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815180045033-1995898334.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815180159120-1412886344.png)


- 切换
nacos支持AP和CP模式的切换

C是所有节点在同一时间看到的数据是一致的；而A的定义是所有的请求都会收到响应。


何时选择使用何种模式?
一般来说,如果不需要存储服务级别的信息且服务实例是通过nacos-client注册,并能够保持心跳上报,那么就可以选择AP模式。当前主流的服务如 Spring cloud和Dubbo服务,都适用于AP模式,AP模式为了服务的可能性而减弱了一致性,因此AP模式下只支持注册临时实例。
如果需要在服务级别编辑或者存储配置信息,那么CP是必须,K8S服务和DNS服务则适用于CP模式。
CP模式下则支持注册持久化实例,此时则是以Raft 协议为集群运行模式,该模式下注册实例之前必须先注册服务,如果服务不存在,则会返回错误。


>切换模式：curl -X PUT 'SNACOS_SERVER:8848/nacos/v1/ns/operator/switches?entry=serverMode&value=CP'




### Nacos作为服务配置中心-基础配置
- 新建Module (springcloud-alibaba-config-nacos-client3377)

- POM
```xml
<!--nacos-config-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
<!--nacos-discovery-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```


- YML
application.yml
```yml
spring:
  profiles:
    active: dev # 表示开发环境
    #active: test # 表示测试环境
    #active: info
```

bootstrap.xml

```yml
# nacos配置
server:
  port: 3377

spring:
  application:
    name: nacos-config-client
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #Nacos服务注册中心地址
      config:
        server-addr: localhost:8848 #Nacos作为配置中心地址
        file-extension: yaml #指定yaml格式的配置
        group: DEV_GROUP
        namespace: 7d8f0f5a-6a53-4785-9686-dd460158e5d4


# ${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
# nacos-config-client-dev.yaml

# nacos-config-client-test.yaml   ----> config.info
```

为什么需要两个配置文件?
Nacos同springcloud-config一样,在项目初始化时,要保证先从配置中心进行配置拉取,拉取配置之后,才能保证项目的正常启动。
springboot中配置文件的加载是存在优先级顺序的,bootstrap优先级高于application



- 主启动 (com.qcl.springcloud.alibaba.NacosConfigClientMain3377)

- 业务类 (com.qcl.springcloud.alibaba.controller.ConfigClientController)

@RefreshScope //支持Nacos的动态刷新功能。

通过Spring Cloud 原生注解GRefreshscope实现配置自动更新



#### 在Nacos中添加配置信息
nacos中的匹配规则
理论：
    Nacos中的dataid的组成格式及与SpringBoot配置文件中的匹配规则
    官网:https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815182736316-1609366243.png)



### Nacos集群和持久化配置

