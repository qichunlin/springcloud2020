## 10.Hystrix断路器 (第二部分)
https://www.bilibili.com/video/BV18E411x7eT?p=59
https://www.bilibili.com/video/BV18E411x7eT?p=60
https://www.bilibili.com/video/BV18E411x7eT?p=61
https://www.bilibili.com/video/BV18E411x7eT?p=62
https://www.bilibili.com/video/BV18E411x7eT?p=63
https://www.bilibili.com/video/BV18E411x7eT?p=64


##### 服务熔断

- 断路器 (一句话就是家里的保险丝)

- 熔断是什么?  [MartinFowler论文](https://martinfowler.com/bliki/CircuitBreaker.html)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200826220425470-92606063.png)

熔断机制概述
熔断机制是应对雪崩效应的一种微服务链路保护机制。当扇出链路的某个微服务出错不可用或者响应时间太长时,
会进行服务的降级,进而熔断该节点微服务的调用,快速返回错误的响应信息。
当检测到该节点微服务调用响应正常后,恢复调用链路。
在Spring Cloud框架里,熔断机制通过Hystrix实现。Hystrix会监控微服务间调用的状况,
当失败的调用到一定阈值,缺省是5秒内20次调用失败,就会启动熔断机制。熔断机制的注解是@HystrixCommand.


- 实操

修改 springcloud-provider-hystrix-payment8001

**com.qcl.springcloud.service.PaymentService** 为什么要配这些参数

```
//=====服务熔断
@HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
        @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),// 是否开启断路器
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),// 请求次数
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 时间窗口期
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),// 失败率达到多少后跳闸
})
```

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827112720546-1374276257.png)


`HystrixCommandProperties.class`
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827113157695-69582409.png)



**com.qcl.springcloud.controller.PaymentController**

```
/**
 * 服务熔断
 *
 * @param id
 * @return
 */
@GetMapping("/payment/circuit/{id}")
public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
    String result = paymentService.paymentCircuitBreaker(id);
    log.info("****result: " + result);
    return result;
}
```



测试

 正确：http://localhost:8001/payment/circuit/31
 
 错误：http://localhost:8001/payment/circuit/-31

>多次错误,然后慢慢正确,发现刚开始不满足条件,就算是正确的访问地址也不能进



- 小总结

1.官网结论：
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200826220425470-92606063.png)


熔断类型
    熔断打开：请求不再进行调用当前服务,内部设置时钟一般为MTTR(平均故障处理时间),当打开时长达到所设时钟则进入半熔断状态
    熔断关闭：熔断关闭不会对服务进行熔断
    熔断半开：部分请求根据规则调用当前服务,如果请求成功且符合规则则认为当前服务恢复正常,关闭熔断


2.官网断路器流程图

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827114709849-252109454.png)

  2.1 官网步骤
  ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827114805482-1665561830.png)

  2.2 断路器在什么情况下开始起作用
  ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827115039631-1004785787.png)

  涉及到断路器的三个重要参数：快照时间窗、请求总数阈值、错误百分比阈值
     1：快照时间窗：断路器确定是否打开需要统计一些请求和错误数据,而统计的时间范围就是快照时间窗,默认为最近的10秒。
     2：请求总数阀值：在快照时间窗内,必须满足请求总数阀值才有资格熔断。默认为20,意味着在10秒内,如果该hystrix命令的调用次数不足20次,即使所有的请求都超时或其他原因失败,断路器都不会打开。
     3：错误百分比阀值：当请求总数在快照时间窗内超过了阀值,比如发生了30次调用,如果在这30次调用中,有15次发生了超时异常,也就是超过50%的错误百分比,在默认设定50%阀值情况下,这时候就会将断路器打开。
  
  
  2.3 断路器开启或关闭的条件
    当满足一定的阀值的时候(默认10秒内超过20个请求次数)
    当失败率达到一定的时候(默认10秒内超过50%的请求失败)到达以上阀值,断路器将会开启P当开启的时候,所有请求都不会进行转发
    一段时间之后(默认是5秒),这个时候断路器是半开状态,会让其中一个请求进行转发。
    如果成功,断路器会关闭,若失败,继续开启。重复4和5
  
  
  2.4 断路器打开之后
  
    2.4.1：再有请求调用的时候,将不会调用主逻辑,而是直接调用降级fallback。通过断路器,实现了自动地发现错误并将降级逻辑切换为主逻辑,减少响应延迟的效果。
    
    2.4.2：原来的主逻辑要如何恢复呢？
        对于这一问题,hystrix也为我们实现了自动恢复功能。
      当断路器打开,对主逻辑进行熔断之后,hystrix会启动一个休眠时间窗,在这个时间窗内,降级逻辑是临时的成为主逻辑,当休眠时间窗到期,断路器将进入半开状态,释放一次请求到原来的主逻辑上,如果此次请求正常返回,那么断路器将继续闭合,主逻辑恢复,如果这次请求依然有问题,断路器继续进入打开状态,休眠时间窗重新计时。
  
  
  2.5 All配置
  
  ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827141654303-2040131671.png)
  
  ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827141810735-814481115.png)

  ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827141833214-1955558984.png)


##### 服务限流

>后面到了alibaba的Sentinel说明


### hystrix工作流程

#### 官网地址
https://github.com/Netflix/Hystrix/wiki/How-it-Works

or

https://hub.fastgit.org/Netflix/Hystrix/wiki/How-it-Works
 

#### Hystrix工作流程     
- 官网图例

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827142935934-2032459148.png)

 
- 步骤说明

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827142858757-1690375220.png)


### 服务监控hystrixDashboard

#### 概述
除了隔离依赖服务的调用以外,Hystrix还提供了准实时的调用监控（Hystrix Dashboard）,Hystrix会持续地记录所有通过Hystrix发起的请求的执行信息,并以统计报表和图形的形式展示给用户,包括每秒执行多少请求多少成功,多少失败等。Netflix通过hystrix-metrics-event-stream项目实现了对以上指标的监控。Spring Cloud也提供了Hystrix Dashboard的整合,对监控内容转化成可视化界面。

#### 仪表盘9001
- 新建Module springcloud-consumer-hystrix-dashboard9001

- POM

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

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
  port: 9001
```

- HystrixDashboardMain9001+新注解@EnableHystrixDashboard
  
**com.qcl.springcloud.HystrixDashboardMain9001**  

- 所有Provider微服务提供类(8001/8002/8003)都需要监控依赖配置
```xml
<!-- actuator监控信息完善 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

- 启动springcloud-consumer-hystrix-dashboard9001该微服务后续将监控微服务8001

http://localhost:9001/hystrix

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827145334768-770950906.png)


#### 断路器演示(服务监控HystrixDashboard)

##### 修改：springcloud-provider-hystrix-payment8001

>注意：新版本需要在MainAppHystrix8001中指定监控路径 (Unable to connect to Command Metric Stream. OR 404)

```
@Bean
public ServletRegistrationBean getServlet() {
    HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
    ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
    registrationBean.setLoadOnStartup(1);
    registrationBean.addUrlMappings("/hystrix.stream");
    registrationBean.setName("HystrixMetricsStreamServlet");
    return registrationBean;
}
```


##### 监控测试
- 1.启动1个eureka或者3个eureka集群均可

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827150814836-2143398035.png)

- 2.观察监控窗口

2.1 9001监控8001 (填写地址--》 http://localhost:8001/hystrix.stream)
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827150246131-450615588.png)


2.2 测试地址
 http://localhost:8001/payment/circuit/1
 http://localhost:8001/payment/circuit/-1
 上述测试通过
 ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827153117276-757102527.png)
 ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827153142086-1153210889.png)

 先访问正确地址,在访问错误地址,再正确地址,会发现图示断路器都是慢慢放开的
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827153330977-604054091.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827153512461-149739155.png)


2.3 如何看?
 
 7色：右上角
 ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827153727563-553850513.png)

 1圈：实心圆：共有两种含义。它通过颜色的变化代表了实例的健康程度,它的健康度从绿色<黄色<橙色<红色递减。该实心圆除了颜色的变化之外,它的大小也会根据实例的请求流量发生变化,流量越大该实心圆就越大。所以通过该实心圆的展示,就可以在大量的实例中快速的发现故障实例和高压力实例。
 
 1线：曲线：用来记录2分钟内流量的相对变化,可以通过它来观察到流量的上升和下降趋势。
 
 整图说明：
 ![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827154240654-360378530.png)

 整图说明2
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827154313861-1455677325.png)


2.4 搞懂一个才能看懂复杂的

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200827154419421-2141250170.png)
