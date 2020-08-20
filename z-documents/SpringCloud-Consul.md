## 7.Consul服务注册与发现
https://www.bilibili.com/video/BV18E411x7eT?p=31
https://www.bilibili.com/video/BV18E411x7eT?p=32
https://www.bilibili.com/video/BV18E411x7eT?p=33
https://www.bilibili.com/video/BV18E411x7eT?p=34
https://www.bilibili.com/video/BV18E411x7eT?p=35


### Consul简介
#### 是什么

[Consul官网](https://www.consul.io/intro)


#### 能干什么

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819232012803-900998429.png)


##### 服务发现
提供HTTP和DNS两种发现方式。


##### 健康检测
支持多种方式，HTTP、TCP、Docker、Shell脚本定制化


##### KV存储
Key、Value的存储方式


##### 多数据中心
Consul支持多数据中心


##### 可视化web界面
有图形化界面操作


#### 去哪下

[Consul下载地址](https://releases.hashicorp.com/consul/1.8.3/consul_1.8.3_windows_amd64.zip)


#### 怎么玩

[Consul相关文档](https://www.springcloud.cc/spring-cloud-consul.html)


### 安装并运行Consul
[官网安装说明](https://learn.hashicorp.com/consul/getting-started/install.html)


下载完成后只有一个consul.exe文件,硬盘路径下双击运行,查看版本号信息 (consul -version)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819232837334-1431087541.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819232823095-119191217.png)


使用开发者模式启动
    consul agent-dev
    通过以下地址可以访问Consul的首页：http://localhost:8500
    结果页面

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819233002331-1483107868.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819233030491-771893565.png)


### 服务提供者

- 新建Module支付服务 springcloud-providerconsul-payment8006


- POM
```xml
<dependencies>
    <!--引入自己的API通用包,可以使用Payment支付Entity-->
    <dependency>
        <groupId>com.qcl.springcloud</groupId>
        <artifactId>springcloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <!--consul-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    </dependency>
    <!--web启动器-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--监控-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <!--热部署-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <!--lombok-->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <!--SpringBoot测试-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```


- YML
```yml
#consul服务端口号
server:
  port: 8006
spring:
  application:
    name: consul-provider-payment

#consul注册中心地址
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}
        #hostname: 127.0.0.1

```


- 主启动类

**com.qcl.springcloud.PaymentMain8006**


- 业务类Controller

**com.qcl.springcloud.controller.PaymentController**


- 验证测试

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819233535358-447492473.png)


http://localhost:8006/payment/consul

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819233613944-1014402520.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819233705411-1290380151.png)



### 服务消费者

- 新建Module消费服务order80 springcloud-consumerconsul-order80


- POM
```xml
<dependencies>
    <!--引入自己的API通用包,可以使用Payment支付Entity-->
    <dependency>
        <groupId>com.qcl.springcloud</groupId>
        <artifactId>springcloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <!--consul-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    </dependency>
    <!--web启动器-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--监控-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <!--热部署-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <!--lombok-->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <!--SpringBoot测试-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

</dependencies>

```


- YML
```yml
#consul服务端口号
server:
  port: 80
spring:
  application:
    name: cloud-provider-order
#consul注册中心地址
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}
        #hostname: 127.0.0.1
```


- 主启动类

**com.qcl.springcloud.OrderMainConsul80**


- 配置Bean

**com.qcl.springcloud.config.ApplicationContextConfig**


- Controller

**com.qcl.springcloud.controller.OrderConsulController**


- 验证测试
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819234525968-797908294.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819234512121-1208398161.png)


- 访问测试地址

http://localhost:8006/payment/consul
http://localhost/consumer/payment/consul

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819234402621-271322081.png)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819234412669-526172607.png)


### 三个注册中心异同点

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819234619184-1353502675.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819235059197-383717626.png)


C:Consistency(强一致性)
A:Availability(可周性)
P:Partition tolerance(分区容错性)

CAP理论关注粒度是数据，而不是整体系统设计的策略


最多只能同时较好的满足两个。
CAP理论的核心是：一个分布式系统不可能同时很好的满足一致性，可用性和分区容错性这三个需求，因此，根据 CAP原理将NoSQL 数据库分成了满足CA原则、满足CP原则和满足AP原则三大类：
   CA-单点集群，满足一致性，可用性的系统，通常在可扩展性上不太强大。
   CP-满足一致性，分区容忍必的系统，通常性能不是特别高。
   AP-满足可用性，分区容忍性的系统，通常可能对一致性要求低一些。
   
   
   
AP(Eureka)
CP(Zookeeper/Consul)  


AP
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819235452008-1928721953.png)


CP
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819235711873-269886953.png)
