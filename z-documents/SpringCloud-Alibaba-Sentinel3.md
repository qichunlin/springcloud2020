## 19.SpringCloud Alibaba Sentinel 实现熔断和限流3
https://www.bilibili.com/video/BV18E411x7eT?p=130
https://www.bilibili.com/video/BV18E411x7eT?p=131
https://www.bilibili.com/video/BV18E411x7eT?p=132
https://www.bilibili.com/video/BV18E411x7eT?p=133
https://www.bilibili.com/video/BV18E411x7eT?p=134
https://www.bilibili.com/video/BV18E411x7eT?p=135
https://www.bilibili.com/video/BV18E411x7eT?p=136
https://www.bilibili.com/video/BV18E411x7eT?p=137



### 服务熔断功能
#### sentinel整合ribbon+openFeign+fallback

#### Ribbon系列

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904081050302-196572971.png)

##### 启动nacos和sentinel
man函数启动

##### 提供者9003/9004

- 新建springcloud-alibaba-provider-payment9003/9004两个一样的做法

- POM
```xml
<dependencies>
    <!--SpringCloud ailibaba nacos -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    <dependency>
        <!-- 引入自己定义的api通用包,可以使用Payment支付Entity -->
        <groupId>com.qcl.springcloud</groupId>
        <artifactId>springcloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <!-- SpringBoot整合Web组件 -->
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

- YML 记得修改不同的端口号

```yaml
server:
  port: 9003

spring:
  application:
    name: nacos-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #配置Nacos地址

management:
  endpoints:
    web:
      exposure:
        include: '*'
```

- 主启动

**com.qcl.springcloud.alibaba.PaymentMain9003**


- 业务类

**com.qcl.springcloud.alibaba.controller.PaymentController**


- 测试地址

http:/localhost:9003/paymentSQL/1


>9004直接拷贝9003的即可


##### 消费者84

- 新建springcloud-alibaba-consumer-nacos-order84

- POM
```xml
<dependencies>
    <!--SpringCloud openfeign -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <!--SpringCloud ailibaba nacos -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    <!--SpringCloud ailibaba sentinel -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
    </dependency>
    <!-- 引入自己定义的api通用包,可以使用Payment支付Entity -->
    <dependency>
        <groupId>com.qcl.springcloud</groupId>
        <artifactId>springcloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <!-- SpringBoot整合Web组件 -->
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

```yaml
server:
  port: 84


spring:
  application:
    name: nacos-order-consumer
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
    sentinel:
      transport:
        #配置Sentinel dashboard地址
        dashboard: localhost:8080
        #默认8719端口,假如被占用会自动从8719开始依次+1扫描,直至找到未被占用的端口
        port: 8719

#消费者将要去访问的微服务名称(注册成功进nacos的微服务提供者)
service-url:
  nacos-user-service: http://nacos-payment-provider

# 激活Sentinel对Feign的支持
feign:
  sentinel:
    enabled: true
```

- 主启动

**com.qcl.springcloud.alibaba.OrderNacosMain84**

- 业务类

**com.qcl.springcloud.alibaba.controller.CircleBreakerController**

**com.qcl.springcloud.alibaba.config.ApplicationContextConfig**


- CircleBreakerController

`修改后重启服务 (热部署对java代码级生效及时; 对@SentinelResource注解内属性,有时效果不好)`

`目的 (fallback管运行异常,进行服务降级;blockHandler管sentinel配置违规)`

`测试地址：http://localhost:84/comsumer/fallback/1`

`没有任何配置：给客户是springboot默认的error page界面,不友好`

`只配置fallback：@SentinelResource(value = "fallback",fallback = "handlerFallback") //fallback只负责业务异常`

`只配置blockHandler：@SentinelResource(value = "fallback",blockHandler = "blockHandler") //blockHandler只负责sentinel控制台配置违规`
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904082830307-1245447180.png)

`fallback和blockHandler都配置：@SentinelResource(value = "fallback", fallback = "handlerFallback", blockHandler = "blockHandler") //fallback和blockHandler都配置的情况`
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904083342964-447817998.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904083405528-1200609222.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904083429117-1909896741.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904083534071-130128353.png)

>注意：若blockHandler和fallback 都进行了配置,则被限流降级而抛出BlockException时只会进入blockHandler 处理逻辑。


`异常忽略属性(exceptionsToIgnore)参数配置：@SentinelResource(value = "fallback", fallback = "handlerFallback", blockHandler = "blockHandler",exceptionsToIgnore = {IllegalArgumentException.class}) //假如报该异常,不再有fallback方法兜底,没有降级效果了`


#### Feign系列

##### 修改84模块
84消费者调用提供者9003

Feign组件一般是消费侧


##### POM
```xml
<!--SpringCloud openfeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

##### YML
```yaml
#消费者将要去访问的微服务名称(注册成功进nacos的微服务提供者)
service-url:
  nacos-user-service: http://nacos-payment-provider

# 激活Sentinel对Feign的支持
feign:
  sentinel:
    enabled: true
```

##### 业务类
`带@FeignClient注解的业务接口`
**com.qcl.springcloud.alibaba.service.PaymentService**

`fallback = PaymentFallbackService.class`
**com.qcl.springcloud.alibaba.service.PaymentFallbackService**

`Controller`

**com.qcl.springcloud.alibaba.controller.CircleBreakerController#paymentSQL**


##### 主启动
添加@EnableFeignClients启动Feign的功能

**com.qcl.springcloud.alibaba.OrderNacosMain84**


##### 测试
http://localhost:84/consumer/paymentSQL/1

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904085255178-1001472103.png)

`测试84调用9003,此时故意关闭9003微服务提供者,看84消费侧会不会自动降级,会不会被耗死`

84消费侧不会自动降级,不会被耗死
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904085413406-899538487.png)


#### 熔断框架比较
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904085600279-1292055634.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904085743102-1100591960.png)



### 规则持久化
#### 是什么

一旦我们重启应用,sentinel规则将消失,生产环境需要将配置规则进行持久化

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904094549110-527192085.png)


#### 怎么玩
将限流配置规则持久化进Nacos保存,只要刷新8401某个rest地址,sentinel控制台的流控规则就能看到,只要Nacos里面的配置不删除,针对8401上sentinel上的流控规则持续有效


##### 步骤

- 修改springcloud-alibaba-sentinel-service8401

- YML
`添加Nacos数据源配置`
```yaml
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
      datasource:
        ds1:
          nacos:
            server-addr: localhost:8848
            dataId: springcloud-alibaba-sentinel-service
            groupId: DEFAULT_GROUP
            data-type: json
            rule-type: flow
```

- POM
```xml
<!--SpringCloud ailibaba sentinel-datasource-nacos 后续做持久化用到-->
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```


- 添加Nacos业务规则配置
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904100159327-1709229345.png)

```json
[
    {
        "resource": "/rateLimit/byUrl",
        "limitApp": "default",
        "grade": 1,
        "count": 1,
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    }
]
```

`节点参数说明`

resource：资源名称;

limitApp：来源应用;

grade：阈值类型,0表示线程数,1表示QPS;

count：单机阈值;

strategy：流控模式,0表示直接,1表示关联,2表示链路;

controlBehavior：流控效果,0表示快速失败,1表示Warm Up,2表示排队等待;

clusterMode：是否集群。


`内容解析`



- 启动8401后刷新sentinel发现业务规则有了

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904100651372-708734622.png)


- 快速访问测试接口
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904100748581-1233461430.png)


- 停止8401再看sentinel
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904102033157-790230221.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904105119804-1923716469.png)

>停机后发现流控规则没有了


- 重新启动8401再看sentinel
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904101954544-403800252.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904101943491-1755099606.png)


多次调用：http://localhost:8401/rateLimit/byUrl

重新配置出现了,持久规则验证通过