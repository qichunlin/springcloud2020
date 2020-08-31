## 18.SpringCloud Alibaba Nacos 服务注册与配置中心
https://www.bilibili.com/video/BV18E411x7eT?p=96
https://www.bilibili.com/video/BV18E411x7eT?p=97
https://www.bilibili.com/video/BV18E411x7eT?p=98
https://www.bilibili.com/video/BV18E411x7eT?p=99
https://www.bilibili.com/video/BV18E411x7eT?p=100
https://www.bilibili.com/video/BV18E411x7eT?p=101
https://www.bilibili.com/video/BV18E411x7eT?p=102
https://www.bilibili.com/video/BV18E411x7eT?p=103
https://www.bilibili.com/video/BV18E411x7eT?p=104
https://www.bilibili.com/video/BV18E411x7eT?p=105


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
[GitHub下载](https://github.com/com.qcl.springcloud.alibaba/Nacos)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815155719985-875781383.png)


同步使用Nacos的1.1.4版本
https://github.com/com.qcl.springcloud.alibaba/nacos/releases/tag/1.1.4


[官网文档-中文版](https://nacos.io/zh-cn/)
[官网文档-英文版](https://nacos.io/en-us/)


#### 各种注册中心比较
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815160403130-619077322.png)

据说Nacos 在阿里巴巴内部有超过10万的实例运行,已经过了类似双十一等各种大型流量的考验


### 安装并运行Nacos
- 1.本地Java8+Maven环境

- 2.先从官网下载Nacos

- 3.解压安装包,直接运行bin文件夹下面的startup.cmd

- 4.命令运行成功后直接访问 http://localhost:8848/nacos (默认账户密码都是 nacos)
http://localhost:8848/nacos/#/login

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815171525183-1015201847.png)


![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815171655715-1967580477.png)



### Nacos作为注册中心演示
#### 官方文档
https://spring-cloud-alibaba-group.github.io/github-pages/hoxton/en-us/index.html
https://spring.io/projects/spring-cloud-alibaba#learn

#### 基于Nacos的服务提供者
- 新建Module (springcloud-alibaba-provider-payment9001)

- POM
    - 父工程POM
```xml
<!--spring cloud alibaba 2.2.0.RELEASE-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2.2.0.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
``` 
 
   本模块POM
```xml
<!--SpringCloud alibaba nacos -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
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

- 主启动类(com.qcl.springcloud.alibaba.PaymentMain9001)


- 业务类(com.qcl.springcloud.alibaba.controller.PaymentController)

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


>切换模式：curl -X PUT '$NACOS_SERVER:8848/nacos/v1/ns/operator/switches?entry=serverMode&value=CP'


### Nacos作为服务配置中心
#### Nacos作为服务配置中心-基础配置
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

>先有共性再有个性


- 主启动 (com.qcl.springcloud.alibaba.NacosConfigClientMain3377)

- 业务类 (com.qcl.springcloud.alibaba.controller.ConfigClientController)

@RefreshScope //支持Nacos的动态刷新功能。

>通过Spring Cloud 原生注解@RefreshScope实现配置自动更新



##### 在Nacos中添加配置信息
###### nacos中的匹配规则

- 理论：

Nacos中的dataid的组成格式及与SpringBoot配置文件中的匹配规则

[Nacos文档官网](https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815182736316-1609366243.png)


- 实操
配置新增

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831113213187-1046424540.png)


Nacos界面配置对应(设置DataId)
 公式：${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
 prefix：默认为spring.application.name的值
 spring.profile.active：即为当前环境对应的profile,可以通过配置 spring.profile.active 来配置
 file.extension：为配置内容的数据格式,可以通过配置项 spring.cloud.nacos.config.file-extension来配置
  历史配置


##### 测试
- 启动前需要在nacos客户端-配置管理-配置管理栏目下面有对应的yaml配置文件

- 运行 springcloud-alibaba-config-nacos-client3377的主启动类

发现启动报错
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831142816035-473087517.png)

修改后缀的配置即可(nacos不支持yml只支持yaml)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831114125572-1647188852.png)


- 调用接口查看配置信息 (http://localhost:3377/config/info)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831113033532-1939957006.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200820235118966-387021895.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200820235104491-85910381.png)


##### 自带动态刷新

修改下Nacos中的yaml配置文件,再次调用查看配置的接口,就会发现配置已经刷新



#### Nacos作为配置中心-分类配置
##### 问题
- 多环境多项目管理

- Nacos的图形化管理界面

1.配置管理
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831114954475-276018997.png)

2.命名空间


- NameSpace+Group+DataID 三者关系?为什么这么设计

Namespace=public,Group=DEFAULT GROUP,默认Cluster是DEFAULT Nacos默认的命名空间是public,Namespace主要用来实现隔离。
比方说我们现在有三个环境：开发、测试、生产环境,我们就可以创建三个Namespace,不同的Namespace之间是隔离的。
Group默认是DEFAULT GROUP,Group可以把不同的微服务划分到同一个分组里面去Service就是微服务；一个Service可以包含多个Cluster(集群),Nacos默认Cluster是DEFAULT,Cluster是对指定微服务的一个虚拟划分。
比方说为了容灾,将Service微服务分别部署在了杭州机房和广州机房,这时就可以给杭州机房的Service微服务起一个集群名称(HZ)给广州机房的Service微服务起一个集群名称(GZ),还可以尽量让同一个机房的微服务互相调用,以提升性能。
最后是Instance,就是微服务的实例。


1.是什么?(设计思想)

  类Java里面的package名和类名
  最外层的namespace是可以用于区分部署环境的,Group和DataID逻辑上区分两个目标对象。


2.三者情况
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831115431459-107235865.png)

>默认情况：Namespace=public,Group=DEFAULT GROUP,默认Cluster是DEFAULT


Nacos默认的命名空间是public,Namespace主要用来实现隔离。
比方说我们现在有三个环境：开发、测试、生产环境,我们就可以创建三个Namespace,不同的Namespace之间是隔离的。

Group默认是DEFAULT_GROUP,Group可以把不同的微服务划分到同一个分组里面去

Service就是微服务；一个Service可以包含多个Cluster(集群),Nacos默认Cluster是DEFAULT,Cluster是对指定微服务的一个虚拟划分。
比方说为了容灾将Service微服务分别部署在了杭州机房和广州机房,这时就可以给杭州机房的Service微服务起一个集群名称(HZ),|
给广州机房的Service微服务起一个集群名称(GZ),还可以尽量让同一个机房的微服务互相调用,以提升性能。
最后是Instance,就是微服务的实例。


- Case

三种方案加载配置

1.DataID方案配置

指定spring.profile.active和配置文件的DataID来使不同环境下读取不同的配置

默认空间+默认分组+新建dev和test两个DataID
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831142440227-2118767961.png)

通过spring.profile.active属性就能进行多环境下配置文件的读取
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831142612072-1699582694.png)

测试 (http://localhost:3377/config/info)
dev环境
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831143048543-1152469789.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831143015933-1526181512.png)

test环境
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831143316928-1791377898.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831143156121-746919524.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831143355572-1854160855.png)


2.Group方案配置 (DataID一样,group不一样)
 通过Group实现环境区分 (新建group)
 ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831150712515-614726427.png)

 ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831150854201-184225773.png)


 在nacos图形界面控制台上面新建配置文件DataID
 ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831151001026-542529724.png)

 bootstrap+application (在config下增加一条group的配置即可。可配置为DEV_GROUP或TEST_GROUP)
 ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831151948238-1761804397.png)

 ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831151853535-109344747.png)


3.Namespace方案配置
新建dev/test的NameSpace
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831152541834-1473706192.png)


回到服务管理-服务列表查看
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831152656432-1378363367.png)


按照域名配置填写
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831152943669-394315589.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831153129756-192979543.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831153238843-180381595.png)


YML
```yml
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
        namespace: 9b6b0986-8803-457a-b6a0-28608bf9a1dd
```

测试
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831153407582-460450782.png)

>实现namespace下面的group下面的dataid
