## 19.SpringCloud Alibaba Sentinel 实现熔断和限流1
https://www.bilibili.com/video/BV18E411x7eT?p=111
https://www.bilibili.com/video/BV18E411x7eT?p=112
https://www.bilibili.com/video/BV18E411x7eT?p=113
https://www.bilibili.com/video/BV18E411x7eT?p=114
https://www.bilibili.com/video/BV18E411x7eT?p=115
https://www.bilibili.com/video/BV18E411x7eT?p=116
https://www.bilibili.com/video/BV18E411x7eT?p=117
https://www.bilibili.com/video/BV18E411x7eT?p=118
https://www.bilibili.com/video/BV18E411x7eT?p=119



### Sentinel
#### 官网

https://github.com/alibaba/Sentinel

[Sentinel中文版](https://github.com/alibaba/Sentinel/wiki/介绍)


![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901090402710-480043417.png)


>约定大于配置大于编码,都有可以写在代码里但是这次还是主要学习使用配置和注解的方式,尽量少写代码


#### 是什么

A powerful flow control component enabling reliability, resilience and monitoring for microservices. (面向云原生微服务的高可用流控防护组件)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901090700942-311526262.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901090829942-1190060610.png)


#### 去哪下

https://github.com/alibaba/Sentinel/releases/download/1.7.0/sentinel-dashboard-1.7.0.jar

#### 能干嘛

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901094545406-130043996.png)


#### 怎么玩

[Sentinel指导文档](https://spring-cloud-alibaba-group.github.io/github-pages/greenwich/spring-cloud-alibaba.html#_spring_%20cloud_%20alibaba_sentinel)

服务中的各种问题

    服务雪崩
    服务降级
    服务熔断
    服务限流


### 安装Sentinel控制台
#### sentinel组件由2部分构成 
  后台
  前台8080
 

- 核心库(Java客户端)不依赖任何框架/库,能够运行于所有Java运行时环境,同时对Dubbo/
Spring Cloud等框架也有较好的支持。

- 控制台(Dashboard) 基于Spring Boot开发,打包后可以直接运行,不需要额外的Tomcat等应用容器。


#### 安装步骤  
- 下载 (https://github.com/alibaba/Sentinel/releases/download/1.7.0/sentinel-dashboard-1.7.0.jar)
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901100126356-909611259.png)

下载到本地 sentinel-dashboard-1.7.0.jar


- 运行命令

前提 (Java8环境OK、8080端口不能被占用)

命令 (java -jar sentinel-dashboard-1.7.0.jar)
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901100511223-1189695033.png)


- 访问Sentinel管理页面 (http://localhost:8080/#/login)
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901100552727-1165429571.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901100629525-1402460915.png)

>账户密码都是sentinel/sentinel


### 初始化演示工程
#### 启动Nacos8848

http://localhost:8848/nacos/#/login

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901103226487-1328956552.png)


#### Module
- 新建Module (springcloud-alibaba-sentinel-service8401)

- POM
```xml
<dependencies>
    <!-- 引入自己定义的api通用包,可以使用Payment支付Entity -->
    <dependency>
        <groupId>com.qcl.springcloud</groupId>
        <artifactId>springcloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <!--SpringCloud ailibaba nacos和 SpringCloud ailibaba sentinel 最好一起放 -->
    <!--SpringCloud ailibaba nacos -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    <!--SpringCloud ailibaba sentinel-datasource-nacos 后续做持久化用到-->
    <dependency>
        <groupId>com.alibaba.csp</groupId>
        <artifactId>sentinel-datasource-nacos</artifactId>
    </dependency>
    <!--SpringCloud ailibaba sentinel -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
    </dependency>
    <!--openfeign-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <!-- SpringBoot整合Web组件+actuator -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <!--日常通用jar包配置-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
        <version>5.3.2</version>
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
  port: 8401

spring:
  application:
    name: springcloud-alibaba-sentinel-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # Nacos服务注册中心地址
    sentinel:
      transport:
        dashboard: localhost:8080 # 配置Sentinel dashboard地址
        port: 8719 # 默认8719端口,假如被占用会自动从8719开始依次+1扫描,直至找到未被占用的端口
        
#监控
management:
  endpoints:
    web:
      exposure:
        include: '*'
```


- 主启动

**com.qcl.springcloud.alibaba.SentinelMain8401**


- 业务类 FlowLimitController

**com.qcl.springcloud.alibaba.controller.FlowLimitController**


#### 启动Sentinel8080

java -jar sentinel-dashboard-1.7.0.jar


#### 启动微服务8401
main函数启动


#### 启动8401微服务后查看sentinel控制台

什么都没有

##### sentinel采用的是懒加载说明

- 执行一次访问即可
http://localhost:8401/testA

http://localhost:8401/testB

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901103650268-914223582.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901103627043-44302397.png)

重新刷新即可看到监控的微服务
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901103708629-157442936.png)


效果
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901103916207-1206136088.png)

>sentinel正在监控微服务8401


### 流控规则

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901104127029-210218897.png)


#### 基本介绍

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901104253350-597908679.png)


##### 进一步解释说明
- 资源名：唯一名称,默认请求路径

- 针对来源：Sentinel可以针对调用者进行限流,填写微服务名,默认default(不区分来源)

- 闽值类型单机阀值：
    - QPS(每秒钟的请求数量)：当调用该ap的QPS达到闽值的时候,进行限流
    - 线程数：当调用该api的线程数达到闽值的时候,进行限流

- 是否集群：不需要集群

- 流控模式：
    - 直接：api达到限流条件时,直接限流
    - 关联：当关联的资源达到闽值时,就限流自己
    - 链路：只记录指定链路上的流量(指定资源从入口资源进来的流量,如果达到阈值,就进行限流)【ap级别的针对来源】

- 流控效果：
    - 快速失败：直接失败,抛异常
    - Warm Up：根据codeFactor(冷加载因子,默认3)的值,从闽值/codeFactor,经过预热时长,才达到设置的QPS阈值
    - 排队等待：匀速排队,让请求以匀速的速度通过,阀值类型必须设置为QPS,否则无效


#### 流控模式
##### 直接(默认)
`第一种添加流控方式：簇点链路--> +流控 即可`
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901105838439-607350017.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901105939113-916165828.png)

>表示一秒钟内查询1次,若超过次数1,就直接-快速失败,报默认错误


`第二种添加流控方式：留控规则--> +新增留控规则`
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901110030372-923341398.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901112930089-894427829.png)


`添加完流控之后`
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901110003064-431689625.png)


- 直接--》快速失败 (系统默认)

- 配置及说明
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901111758585-983398466.png)

>表示一秒钟内查询 /testA接口 1次,若超过次数1,就直接-快速失败,报默认错误

- 测试
快速点击访问http://localhost:8401/testA

结果

`出现Blocked by Sentinel (flow limiting) 限流`
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901111419049-1264601739.png)


思考
  直接调用默认报错信息,技术方面OK but是否应该有我们自己的后续处理？(类似有个fallback的兜底方法？)


- QPS和线程数是两种不同的限流效果
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901112403381-831155256.png)


##### 关联

- 是什么

当关联的资源达到阈值时,就限流自己

当与B关联的资源C达到阀值后,就限流B自己

C惹事,B挂了 (比如：支付服务的接口资源不够用了,就通过关联的接口下订单的接口不让请求进入)



- 配置B限流规则

设置效果
  当关联资源/testC的qps阀值超过1时,就限流/testB的Rest访问地址,当关联资源到闽值后限制配置好的资源名

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901114200437-2119058988.png)


postman模拟并发密集访问testC
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901115323481-749540444.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901114904446-1220673292.png)


将访问地址添加到新线程组
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901144214509-204019023.png)


20个线程每次间隔0.3s访问一次
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901115028603-363618457.png)

大批量线程高并发访问C,导致B失效了


运行后发现testB挂了 (http://localhost:8401/testB)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901144017427-495730806.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901144012443-1194065023.png)



##### 链路
多个请求调用了同一个微服务



#### 流控效果
##### 直接--》快速失败(默认的流控处理)

- 直接失败,抛出异常(Blocked by Sentinel (flow limiting))

- 源码 

**com.alibaba.csp.sentinel.slots.block.flow.controller.DefaultController**


##### 预热
- 说明

公式：阈值除以coldFactor(默认值为3),经过预热时长后才会达到阈值

- 官网

[限流-冷启动官网](https://hub.fastgit.org/alibaba/Sentinel/wiki/%E9%99%90%E6%B5%81---%E5%86%B7%E5%90%AF%E5%8A%A8) 

默认coldFactor为3,即请求QPS从threshold/3开始,经预热时长逐渐升至设定的QPS阈值。

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901145958944-1888162213.png)


- 源码

**com.alibaba.csp.sentinel.slots.block.flow.controller.WarmUpController**

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901150946827-1817306446.png)


- WarmUp配置

默认coldFactor为3,即请求QPS从(threshold/3)开始,经多少预热时长才逐渐升至设定的QPS闽值。
案例,阀值为10+预热时长设置5秒。
系统初始化的阀值为10/3约等于3,即阀值刚开始为3；然后过了5秒后阀值才慢慢升高恢复到10

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901153757904-2376429.png)


- 多次点击 http://localhost:8401/testC

刚开始慢慢不行,后续慢慢OK

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901154706246-1598905173.png)


- 应用场景

如：秒杀系统在开启的瞬间,会有很多流量上来,很有可能把系统打死,预热方式就是把为了保护系统,可慢慢的把流量放进来,慢慢的把阀值增长到设置的阀值。

##### 排队等待
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901154929925-1656487811.png)

匀速排队,让请求以均匀的速度通过,阀值类型必须设成QPS,否则无效。
设置含义：/testA每秒1次请求,超过的话就排队等待,等待的超时时间为20000毫秒。

- 匀速排队,阈值必须设置为QPS

- 官网

[匀速排队官网图](https://hub.fastgit.org/alibaba/Sentinel/wiki/%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901155653811-973052709.png)


[流量控制-匀速排队模式](https://hub.fastgit.org/alibaba/Sentinel/wiki/%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6-%E5%8C%80%E9%80%9F%E6%8E%92%E9%98%9F%E6%A8%A1%E5%BC%8F)


这种方式主要用于处理间隔性突发的流量,例如消息队列。想象一下这样的场景,在某一秒有大量的请求到来,而接下来的几秒则处于空闲状态,我们希望系统能够在接下来的空闲期间逐渐处理这些请求,而不是在第一秒直接拒绝多余的请求。

>注意：匀速排队模式暂时不支持 QPS > 1000 的场景。


- 源码

**com.alibaba.csp.sentinel.slots.block.flow.controller.RateLimiterController**


- 测试

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901160315795-2132209608.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901161509904-529676580.png)

postman设置 (一共10个线程 一秒钟过一个线程)
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901161617875-1438266371.png)


后端控制台输出
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901161614339-693886312.png)
