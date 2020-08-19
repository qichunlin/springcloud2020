## Zookeeper服务注册与发现
https://www.bilibili.com/video/BV18E411x7eT?p=28
https://www.bilibili.com/video/BV18E411x7eT?p=29
https://www.bilibili.com/video/BV18E411x7eT?p=30


### SpringCloud整合zookeeper代替Eureka

#### 注册中心zookeeper

zookeeper是一个分布式协调工具，可以实现注册中心功能

关闭Linux服务器防火墙后启动zookeeper服务器 (systemctl stop firewalld   systemctl status firewalld)

zookeeper服务器取代Eureka服务器，zk作为服务注册中心


![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819221603176-1529834636.png)


#### 服务提供者

- 新建Module (springcloud-provider-payment8004)

- POM
```xml
<dependencies>
    <!--引入自己的API通用包,可以使用Payment支付Entity-->
    <dependency>
        <groupId>com.qcl.springcloud</groupId>
        <artifactId>springcloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <!--zookeeper客户端-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
        <!--先排除自带的zookeeper3.5.3-->
        <exclusions>
            <exclusion>
                <groupId>org.apache.zookeeper</groupId>
                <artifactId>zookeeper</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.apache.zookeeper</groupId>
        <artifactId>zookeeper</artifactId>
        <version>3.4.9</version>
    </dependency>

    <!--web启动器-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <exclusions>
            <exclusion>
                <groupId>ch.qos.logback</groupId>
                <artifactId>logback-classic</artifactId>
            </exclusion>
        </exclusions>
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


- 写YML
```yml
server:
  port: 8004

spring:
  application:
    name: springcloud-provider-payment
  cloud:
    zookeeper:
      connect-string: 192.168.0.120:2181

```


- 主启动类

**com.qcl.springcloud.PaymentMain8004**


- Controller

**com.qcl.springcloud.controller.PaymentController**


- 启动8004注册进zookeeper

启动zookeeper 进入到bin目录下面  执行  ./zkServer.sh start   --> 连接上之后  ./zkCli.sh

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819223350635-1003179106.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819223435837-154248717.png)


ls /
get /zookeeper

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819223517342-1800530332.png)


启动后问题

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819224147698-1725432103.png)


why
解决zookeeper版本jar包冲突问题
排除zk冲突后的新pom (先排除3.5.3 再引入3.4.9的包即可)


- 验证测试

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819224519139-335134844.png)

http:localhost:8004/payment/zk

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819224633354-1445091109.png)


- 验证测试2

zookeeper中获取json串信息

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819225135722-68996802.png)


```json
{"name":"springcloud-provider-payment","id":"62bf5bb7-73b5-4c7d-acda-0fe2f0031d77","address":"legend-PC","port":8004,"sslPort":null,"payload":{"@class":"org.springframework.cloud.zookeeper.discovery.ZookeeperInstance","id":"application-1","name":"springcloud-provider-payment","metadata":{}},"registrationTimeUTC":1597848085645,"serviceType":"DYNAMIC","uriSpec":{"parts":[{"value":"scheme","variable":true},{"value":"://","variable":false},{"value":"address","variable":true},{"value":":","variable":false},{"value":"port","variable":true}]}}
```

```json
{
    "name": "springcloud-provider-payment",
    "id": "62bf5bb7-73b5-4c7d-acda-0fe2f0031d77",
    "address": "legend-PC",
    "port": 8004,
    "sslPort": null,
    "payload": {
        "@class": "org.springframework.cloud.zookeeper.discovery.ZookeeperInstance",
        "id": "application-1",
        "name": "springcloud-provider-payment",
        "metadata": {}
    },
    "registrationTimeUTC": 1597848085645,
    "serviceType": "DYNAMIC",
    "uriSpec": {
        "parts": [
            {
                "value": "scheme",
                "variable": true
            },
            {
                "value": "://",
                "variable": false
            },
            {
                "value": "address",
                "variable": true
            },
            {
                "value": ":",
                "variable": false
            },
            {
                "value": "port",
                "variable": true
            }
        ]
    }
}
```


- 思考

服务节点是临时节点还是持久节点

>测试发现是临时性的,在服务停止一段时间后节点会自动剔除 CP特性

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819230043041-747910090.png)


#### 服务消费者
- 新建 springcloud-consumerzk-order80

- POM
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
    <!--引入自己的API通用包,可以使用Payment支付Entity-->
    <dependency>
        <groupId>com.qcl.springcloud</groupId>
        <artifactId>springcloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <!--zookeeper客户端-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
        <!--先排除自带的zookeeper3.5.3-->
        <exclusions>
            <exclusion>
                <groupId>org.apache.zookeeper</groupId>
                <artifactId>zookeeper</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.apache.zookeeper</groupId>
        <artifactId>zookeeper</artifactId>
        <version>3.4.9</version>
    </dependency>
</dependencies>
```


- YML
```yml
server:
  port: 80

spring:
  application:
    name: springcloud-consumer-order
  cloud:
    zookeeper:
      connect-string: 192.168.0.120:2181

```

- 主启动

**com.qcl.springcloud.OrderZKMain80**


- 业务类

**com.qcl.springcloud.controller.OrderZKController**

**com.qcl.springcloud.config.ApplicationContextConfig**


- 验证测试

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200819224519139-335134844.png)


访问测试地址  http://localhost/consumer/payment/zk
