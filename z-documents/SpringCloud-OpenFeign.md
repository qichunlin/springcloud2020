## 9.OpenFeign服务接口调用
https://www.bilibili.com/video/BV18E411x7eT?p=43
https://www.bilibili.com/video/BV18E411x7eT?p=44
https://www.bilibili.com/video/BV18E411x7eT?p=45
https://www.bilibili.com/video/BV18E411x7eT?p=46


### 概述
#### OpenFeign是什么?

服务器+绑定器(接口+注解去调用)

[官网概述](https://cloud.spring.io/spring-cloud-static/Hoxton.SR1/reference/htmlsingle/#spring-cloud-openfeign)

Feign是一个声明式WebService客户端。使用Feign能让编写Web Service客户端更加简单。
它的使用方法是定义一个服务接口然后在上面添加注解。Feign也支持可拔插式的编码器和解码器。Spring Cloud对Feign进行了封装,使其支持了Spring MVC标准注解和HttpMessageConverters。Feign可以与Eureka和Ribbon组合使用以支持负载均衡
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200821075842711-941205725.png)


Feign是一个声明式的Web服务客户端,让编写Web服务客户端变得非常容易,只需创建一个接口并在接口上添加只需创建一个接口并在接口上添加注解即可


Github：https://github.com/spring-cloud/spring-cloud-openfeign


#### 能干什么

- Feign能干什么

Feign旨在使编写Java Http客户端变得更容易。
前面在使用Ribbon+RestTemplate时,利用RestTemplate对http请求的封装处理,形成了一套模版化的调用方法。但是在实际开发中,由于对服务依赖的调用可能不止一处,往往一个接口会被多处调用,所以通常都会针对每个微服务自行封装一些客户端类来包装这些依赖服务的调用。所以,Feign在此基础上做了进一步封装,由他来帮助我们定义和实现依赖服务接口的定义。在Feign的实现下,我们只需创建一个接口并使用注解的方式来配置它（以前是Dao接口上面标注Mapper注解,现在是一个微服务接口上面标注一个Feign注解即可）,即可完成对服务提供方的接口绑定,简化了使用Spring cloud Ribbon时,自动封装服务调用客户端的开发量。

- Feign集成了Ribbon

利用Ribbon维护了Payment的服务列表信息,并且通过轮询实现了客户端的负载均衡。而与Ribbon不同的是,通过feign只需要定义服务绑定接口且以声明式的方法,优雅而简单的实现了服务调用

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200821081059883-205885515.png)


#### Feign和OpenFeign两者区别

Feign是SpringCloud组件中的一个轻量级Restful的HTTP服务客户端,Feign内置了Ribbon,用来做客户端负载均衡,去调用服务注册中心的服务。Feign的使用方式是：使用Feign的注解定义接口,调用这个接口,就可以调用服务注册中心的服务

OpenFeign是SpringCloud在Feign的基础上支持了SpringMVC的注解,如@RequestMapping等等。OpenFeign的@FeignClient可以解析SpringMVC的@RequestMapping注解下的接口,并通过动态代理的方式产生实现类,实现类中做负载均衡并调用其他服务。


### OpenFeign使用步骤

- 接口+注解 (微服务调用接口+@FeignClient)

- 新建Module springcloud-consumer-feign-order80 (Feign在消费端使用)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200821080950347-2044524663.png)


- POM

```xml
<dependencies>
    <!--openfeign-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <!--eureka client-->
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
    <!--web-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <!--一般基础通用配置-->
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
  port: 80

eureka:
  client:
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
```

- 主启动

**com.qcl.springcloud.OrderFeignMain80**


- 业务类

业务逻辑接口+ @FeignClient配置调用provider服务

**com.qcl.springcloud.service.PaymentFeignService**

>可用从8001的服务的controller上面拷贝


新建PaymentFeignService接口并新增注解@FeignClient

**com.qcl.springcloud.service.PaymentFeignService**


控制层Controller

**com.qcl.springcloud.controller.OrderFeignController**


>主启动类上使用 (com.qcl.springcloud.OrderFeignMain80) @EnableFeignClients ; 服务调用类(com.qcl.springcloud.service.PaymentFeignService) @FeignClient(value = "SPRINGCLOUD-PAYMENT-SERVICE")

- 测试

先启动2个eureka集群7001/7002

再启动2个微服务8001/8002

启动OpenFeign启动

http://localhost/consumer/payment/get/31

>Feign自带负载均衡配置项


- 小总结

@FeignClient(value = "SPRINGCLOUD-PAYMENT-SERVICE")
value指定的是eureka上面的application name

接口的方法指定的是调用的url地址(可以从需要调用的微服务controller上面拷贝出来)


### OpenFeign超时控制

#### 超时设置,故意设置超时演示出错情况

- 服务提供方8001故意写暂停程序

```
@GetMapping(value = "/feign/timeout")
public String paymentFeignTimeout(){
    //暂停几秒钟线程
    try {
        TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return serverPort;
}
```


- 服务消费方80添加超时方法PaymentFeignService

```
/**
 * Feign超时控制
 *
 * @return
 */
@GetMapping(value = "/api/payment/feign/timeout")
String paymentFeignTimeout();
```

- 服务消费方80添加超时方法OrderFeignController

```
@GetMapping(value = "/consumer/payment/feign/timeout")
public String paymentFeignTimeout() {
    // OpenFeign客户端一般默认等待1秒钟
    return paymentFeignService.paymentFeignTimeout();
}
```


- 测试

http://localhost/api/payment/feign/timeout

http://localhost/consumer/payment/feign/timeout


错误页面
Read TimeOut 微服务名称



#### OpenFeign默认等待1秒钟,超过后报错是什么


#### 是什么

默认Feign客户端只等待一秒钟,但是服务端处理需要超过1秒钟,导致Feign客户端不想等待了,直接返回报错。
为了避免这样的情况,有时候我们需要设置Feign客户端的超时控制。

yml文件中开启配置


>OpenFeign默认支持Ribbon

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200821085530642-1175551690.png)


#### YML文件里需要开启OpenFeign客户端超时控制
```yml
#设置feign客户端超时时间(OpenFeign默认支持ribbon)
ribbon:
#指的是建立连接所用的时间,适用于网络状况正常的情况下,两端连接所用的时间 (5000ms = 5s)
  ReadTimeout: 5000
#指的是建立连接后从服务器读取到可用资源所用的时间
  ConnectTimeout: 5000
```

>转圈后完成请求获取到数据


### OpenFeign日志打印功能
#### 日志打印功能
#### 是什么

Feign 提供了日志打印功能,我们可以通过配置调整日志级别,从而了解Feign中Http 请求的细节。
说白了就是对Feign接口的调用情况进行监控和输出

#### 日志级别

NONE：默认的,不显示任何日志；
BASIC：仅记录请求方法、URL、响应状态码及执行时间；
HEADERS：除了BASIC中定义的信息之外,还有请求和响应的头信息；
FULL：除了HEADERS中定义的信息之外,还有请求和响应的正文及元数据。


#### 配置日志bean

**com.qcl.springcloud.config.FeignConfig**

#### YML文件里需要开启日志的Feign客户端

```yml
logging:
  level:
    # feign日志以什么级别监控哪个接口
    com.qcl.springcloud.service.PaymentFeignService: debug
```

#### 后台日志查看

成功输出
例子
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200821090235607-2014132852.png)
