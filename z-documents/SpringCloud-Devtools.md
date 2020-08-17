接上 (z-documents/SpringCloud-Start2.md)

https://www.bilibili.com/video/BV18E411x7eT?p=11
https://www.bilibili.com/video/BV18E411x7eT?p=12
https://www.bilibili.com/video/BV18E411x7eT?p=13
https://www.bilibili.com/video/BV18E411x7eT?p=14


##### 热部署插件devtools

1.Adding devtools ta your project
```xml
<!--热部署-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403011657708-37156709.png)


2.Adding plugin to your pom.xml

下段配置我们粘贴进聚合父类总工程的pom.xml里
```xml
<!--添加devtools热部署插件到父工程-->
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <fork>true</fork>
                <addResources>true</addResources>
            </configuration>
        </plugin>
    </plugins>
</build>
```
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403011943987-629316253.png)


3.Enabling automatic build

idea设置
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403011821733-1251408000.png)


4.Update the value of
press `ctrl+shift+Alt+/`  and search for the registry. In the Registry, enable:

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200817214359823-693531335.png)

![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403012045522-992639407.png)

![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403012159250-283351305.png)


5.重启IDEA

>这个功能只在开发的时候使用,上生产必须关闭


##### 微服务消费者订单Module模块(springcloud-consumer-order80)

步骤同payment8001

- 1.建Module
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403104441160-24542329.png)
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403104513111-682292006.png)
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403104712728-368333949.png)
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403104738436-1597733682.png)



- 2.改POM
```xml
<dependencies>
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
    <!--mybatis启动器-->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
    </dependency>
    <!--德鲁伊连接池-->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
    </dependency>
    <!--mysql驱动-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <!--jdbc依赖-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
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

    <!--Eureka客户端-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>

```


- 3.写YML
```yml
server:
  port: 80

spring:
  application:
    name: springcloud-order-service
```


- 4.主启动
com.qcl.springcloud.OrderMain80


- 5.业务类
com.qcl.springcloud.controller.OrderController

1.entities
主实体Payment
Json封装体 CommonResult


2.Rest Template (需注入Spring容器)

是什么?
```
RestTemplate提供了多种便捷访问远程Http服务的方法,是一种简单便捷的访问restfu服务模板类,是Spring提供的用于访问Rest服务的客户端模板工具集
```


官网及使用

[官网地址](https://docs.spring.io/spring-framework/docs/5.2.2.RELEASE/javadoc-api/org/springframework/web/client/RestTemplate.html)

使用
  使用restTemplate访问restful接口非常的简单。(url,requestMap,ResponseBean.class）这三个参数分别代表
REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。


3.config配置类
com.qcl.springcloud.config.ApplicationContextConfig


- 6.测试

查询测试
http://localhost/consumer/api/payment/get/1

创建测试
http://localhost/consumer/api/payment/create?serial=666


**发现一个问题：保存到数据库的数据只有主键id却没有serial的字段值?**

>解决办法：在payment8001服务的创建接口上面 不要忘记@RequestBody注解



##### 工程重构
- 观察问题

系统中有重复代码部分


- 新建springcloud-api-commons

![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403141819393-219092627.png)
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403141839521-1730066195.png)
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403141903189-493137019.png)
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200403141928553-1232743238.png)


- POM
```xml
<dependencies>
    <!--lombok-->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <!--热部署-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <!--java工具包-->
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
        <version>5.1.0</version>
    </dependency>
</dependencies>
```

- entities

主实体Payment
**com.qcl.springcloud.entities.Payment**

Json封装体CommonResult
**com.qcl.springcloud.commons.CommonResult**

- maven命令打包
mvn clean install

- 改造调整服务

订单80和支付8001分别改造 (删除两个工程原来的entities文件夹 引入下面的maven依赖坐标)
```xml
<!--引入自定义的通用包(工程重构)-->
<dependency>
    <groupId>com.qcl.springcloud</groupId>
    <artifactId>springcloud-api-commons</artifactId>
    <version>${project.version}</version>
</dependency>
```