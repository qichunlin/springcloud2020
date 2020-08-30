## 16.SpringCloudSleuth 分布式请求链路跟踪
https://www.bilibili.com/video/BV18E411x7eT?p=92
https://www.bilibili.com/video/BV18E411x7eT?p=93
https://www.bilibili.com/video/BV18E411x7eT?p=94


理论---实操---小总结


### 概述
#### 为什会出现这个技术?需要解决哪些问题?
  
  在微服务框架中,一个由客户端发起的请求在后端系统中会经过多个不同的的服务节点调用来协同产生最后的请求结果,
每一个前段请求都会形成一条复杂的分布式服务调用链路,链路中的任何一环出现高延时或错误都会引起整个请求最后的失败。

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200814132353318-1923750681.png)


#### 是什么
- [GitHub官网](https://github.com/spring-cloud/spring-cloud-sleuth)

- Spring Cloud Sleuth提供了一套完整的服务跟踪的解决方案

- 在分布式系统中提供追踪解决方案并且兼容支持了zipkin

一个管监控(sleuth) 一个管展示(zipkin)


#### 解决
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200814132739708-781953830.png)



### 搭建SpringCloudSleuth 链路监控步骤
#### zipkin

- 下载
SpringCloud从F版起已不需要自己构Zipkin Server了,只需调用jar包即可

[zipkin-server下载地址](https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/)

zipkin-server-2.12.9-exec.jar


- 运行jar
java -jar zipkin-server-2.12.9-exec.jar

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200830221740721-1188189457.png)


- 运行控制台

http:/localhost:9411/zipkin/

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200814134407240-1237643644.png)


##### 术语
- 完整的调用链路
表示一请求链路,一条链路通过Trace Id唯一标识,Span标识发起的请求信息,各span通过parent id 关联起来

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200814133629733-2080379585.png)

    
- 上图what简化之后

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200814133843983-390492175.png)


- 名词解释
Trace：类似于树结构的Span集合,表示一条调用链路,存在唯一标识
Span：表示调用链路来源,通俗的理解span就是一次请求信息    



#### 2.服务提供者 (springcloud-provider-payment8001)

- POM
```xml
<!--包含了sleuth和zipkin-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

- YML
```yml
# sleuth 和zipkin配置相关
zipkin:
  base-url: http://localhost:9411
sleuth:
  sampler:
    #采样率值介于 0 到 1 之间, 1 则表示全部采集
    probability: 1

```
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200814185622596-1329988956.png)


- 业务类PaymentController
```
@GetMapping(value = "/sleuth/zipkin")
public String paymentZipkin(){
    return "I am ok payment fall back welcome to hhhhh";
}
```



#### 3.服务消费者(调用方) -->springcloud-payment-order80

- POM
```xml
<!--包含了sleuth和zipkin-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```


- YML
```yml
spring:
  application:
    name: springcloud-order-service
    # sleuth 和zipkin配置相关
    zipkin:
      base-url: http://localhost:9411
    sleuth:
      sampler:
        #采样率值介于 0 到 1 之间, 1 则表示全部采集
        probability: 1
```


![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200814190917688-88202251.png)


- 业务类OrderController
```
/**
 * zipkin+sleuth
 *
 * @return
 */
@GetMapping(value = "/consumer/payment/zipkin")
public String getPaymentZipkin() {
    return restTemplate.getForObject("http://localhost:8001" + "/api/payment/sleuth/zipkin/", String.class);
}
```


#### 4.依次启动Eureka 7001、7002/8001、8002/80

http://localhost/consumer/payment/zipkin

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815124606973-30668949.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815124355177-670878469.png)


#### 5.打开浏览器 http://localhost:9411 (http://localhost:9411/zipkin/)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815133054024-1548855675.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815133155519-1103981612.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815133227904-1378527709.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815143303549-408130383.png)
