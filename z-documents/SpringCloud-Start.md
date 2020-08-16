## 1.SpringCloud 微服务架构理论入门
https://www.bilibili.com/video/BV18E411x7eT?p=1
https://www.bilibili.com/video/BV18E411x7eT?p=2
https://www.bilibili.com/video/BV18E411x7eT?p=3
https://www.bilibili.com/video/BV18E411x7eT?p=4


### 什么是微服务架构
微服务架构是一种架构模式，它提倡将单一应用程序划分成一组小的服务，服务之间互相协调、互相配合，
为用户提供最终价值。每个服务运行在其独立的进程中，服务与服务间采用轻量级的通信机制互相协作
(通常是基于HTTP协议的RESTfulAPl)。每个服务都围绕着具体业务进行构建，并且能够被独立的部署
到生产环境、类生产环境等。另外，应当尽量避免统一的、集中式的服务管理机制，对具体的一个服务
而言,应根据业务上下文,选择合适的语言、工具对其进行构建。
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816204643637-1734533868.png)


基于分布式的微服务架构体系?
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816204840642-245687525.png)

参考
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816205055604-1433682070.png)


springCloud架构图

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816204948099-1815457665.png)


SpringCloud = 分布式微服务架构的一站式的解决方案,是多种微服务架构落地技术的集合体,俗称微服务全家桶
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816205144220-722551689.png)


#### SpringCloud 集成优质项目
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816210242337-1613908646.png)



#### 大厂的一些技术架构
- Jd
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816210401449-118784330.png)


- Ali
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816210523482-477561795.png)

- jd物流
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816210555639-733137204.png)


![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816210619548-1745865545.png)



### SpringCloud技术栈
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816210718527-1127261485.png)


天上飞的理念,必然有落地的实现
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816211112274-855754591.png)


总结
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816211216131-1106787591.png)



## 2.从2.2.x 和H 版开始说起

上篇：SpringBoot 2.X版和SpringCloud H版
下篇：SpringCloud Alibab


### SpringBoot版本选择

#### git源码地址
https://github.com/spring-projects/spring-boot/releases/


#### SpringBoot2.0 新特性
https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.0-Release-Notes

通过上面官网发现，Boot官方强烈建议你升级到2.X以上版本


#### 官网看Boot版本
- springboot(截至2019.10.26)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816212311358-1720395515.png)

- springboot(截至2020.2.15)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816212409108-286428511.png)


### SpringCloud版本选择

#### git源码地址
https://github.com/spring-projects/spring-cloud

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816212619320-754588446.png)


#### 官网
https://spring.io/projects/spring-cloud


#### 官网看Cloud版本
- Cloud命名规则

SpringCloud的版本关系
    Spring Cloud 采用了英国伦敦地铁站的名称来命名,并由地铁站名称字母A-Z依次类推的形式来发布迭代版本SpringCloud是一个由许多子项目组成的综合项目,
各子项目有不同的发布节奏。为了管理SpringCloud与各子项目的版本依赖关系,发布了一个清单,其中包括了某个SpringCloud版本对应的子项目版本。为了避
免SpringCloud版本号与子项目版本号混淆,SpringCloud版本采用了名称而非版本号的命名,这些版本的名字采用了伦敦地铁站的名字,根据字母表的顺序来对应
版本时间顺序。例如Angel是第一个版本,Brixton是第二个版本。
    当SpringCloud的发布内容积累到临界点或者一个重大BUG被解决后,会发布一个“service releases"版本,简称SRX版本,比如Greenwich.SR2就是SpringCloud
发布的Greenwich版本的第2个SRX版本。


springcloud(截至2019.10.26)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816213130266-1739447857.png)


springboot(截至2020.2.15)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816213218984-534063710.png)




### SpringCloud和SpringCloud之间的依赖关系怎么看
https://spring.io/projects/spring-cloud#overview

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816213518346-324288858.png)


#### 依赖
Finchley 是基于Spring Boot 2.0.x构建的不再Boot 1.5.x 
Dalston 和Edgware是基于Spring Boot 1.5.x构建的，不支持Spring Boot 2.0.x 
Camden 构建于Spring Boot 1.4.x，但依然能支持Spring Boot 1.5.x

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816213640808-1744499797.png)


#### 更详细的版本对应方法
https://start.spring.io/actuator/info

结果
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816213804670-1218155419.png)


查看Json串返回结果
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816213943893-1018781558.png)


### SpringCloud技术选型定稿
- springcloud：Hoxton.SR1

- springboot：2.2.2.RELEASE

- springcloudalibaba：2.1.0.RELEASE

- Java：java8

- Maven：3.5及以上

- Mysql：5.7及以上


由cloud决定boot版本
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816214525428-958458700.png)

2.X版本常用的POM
```xml
<!--spring boot 2.2.2-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>2.2.2.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
<!--spring cloud Hoxton.SR1-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-dependencies</artifactId>
    <version>Hoxton.SR1</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
<!--spring cloud alibaba 2.1.0.RELEASE-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2.2.0.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```


## 3.关于Cloud各种组件的停更/升级/替换

### 由停更引发的问题
#### 停更不停用
- 被动修复bugs

- 不再接受合并请求

- 不再发布新版本


#### 明细条目
- 以前
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816215130956-370154270.png)


- 2020现在
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816221134704-1008836037.png)


### 参考资料官网
#### SpringCloud资料

[springcloud英文文档](https://cloud.spring.io/spring-cloud-static/Hoxton.SR1/reference/htmlsingle/)

[springCloud中文文档](https://www.bookstack.cn/read/spring-cloud-docs/docs-index.md)


#### SpringBoot资料
[springboot英文文档](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/)