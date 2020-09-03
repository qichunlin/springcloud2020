## 19.SpringCloud Alibaba Sentinel 实现熔断和限流2
https://www.bilibili.com/video/BV18E411x7eT?p=120
https://www.bilibili.com/video/BV18E411x7eT?p=121
https://www.bilibili.com/video/BV18E411x7eT?p=122
https://www.bilibili.com/video/BV18E411x7eT?p=123
https://www.bilibili.com/video/BV18E411x7eT?p=124
https://www.bilibili.com/video/BV18E411x7eT?p=125
https://www.bilibili.com/video/BV18E411x7eT?p=126
https://www.bilibili.com/video/BV18E411x7eT?p=127
https://www.bilibili.com/video/BV18E411x7eT?p=128
https://www.bilibili.com/video/BV18E411x7eT?p=129


### 降级规则
#### 官网

[Github降级规则文档](https://hub.fastgit.org/alibaba/Sentinel/wiki/%E7%86%94%E6%96%AD%E9%99%8D%E7%BA%A7)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901163922646-486514939.png)

- RT(平均响应时间,秒级)平均响应时间超出阀值且在时间窗口内通过的请求>=5,两个条件同时满足后触发降级

- 窗口期过后关闭断路器

- RT最大4900(更大的需要通过-Dcsp.sentinel.statistic.max.rt=XXXX才能生效)


- 异常比列(秒级)
  QPS>=5且异常比例(秒级统计)超过阀值时,触发降级；时间窗口结束后,关闭降级


#### 基本介绍
- 进一步说明

Sentinel 熔断降级会在调用链路中某个资源出现不稳定状态时(例如调用超时或异常比例升高),对这个资源的调用进行限制,让请求快速失败,避免影响到其它的资源而导致级联错误。
当资源被降级后,在接下来的降级时间窗口之内,对该资源的调用都自动熔断(默认行为是抛出DegradeException)。


- Sentinel的断路器是没有半开状态的

半开的状态系统自动去检测是否请求有异常,没有异常就关闭断路器恢复使用,有异常则继续打开断路器不可用。

具体可以参考Hystrix
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901164653254-192194028.png)


#### 降级策略实战
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901165115204-1121581303.png)

##### RT
- 是什么

    平均响应时间(DEGRADE_GRADE_RT)：当1s内持续进入5个请求,对应时刻的平均响应时间(秒级)均超过阈值(count,以ms为单位),
那么在接下的时间窗口(DegradeRule中的timeWindow,以s为单位)之内,对这个方法的调用都会自动地熔断(抛出DegradeException)。
注意 Sentinel 默认统计的RT上限是900ms,超出此阀值的都会算作4900ms,若需要变更此上限可以通过启动配置项 -Dcsp.sentinel.statistic.max.rt=xxx来配置。

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901165317109-878721940.png)



- 测试

代码
```
@GetMapping("/testD")
public String testDRT() {
    try {
        TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return "------testD";
}
```


sentinel降级配置
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901165819172-2145174041.png)

jmeter压测
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901171030277-1142444173.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901171531465-974580094.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901171439612-520950580.png)


结论
  按照上述配置,永远一秒钟打进来10个线程(大于5个了)调用testD,我们希望200毫秒处理完本次任务,如果超过200毫秒还没处理完,在未来1秒钟的时间窗口内,断路器打开(保险丝跳闸)微服务不可用,保险丝跳闸断电了
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901172222231-19377869.png)


>后续停止jmeter,没有这么大的访问量了,断路器关闭(保险丝恢复),微服务恢复OK



##### 异常比例 (DEGRADE_GRADE_EXCEPTION_RATIO)

- 是什么
当资源的每秒请求量>=5,并且每秒异常总数占通过量的比值超过阀值(DegradeRule中的count)之后,资源进入降级状态,即在接下的时间窗口(DegradeRule中的timewindow,以s为单位)之内,对这个方法的调用都会自动地返回。异常比率的阈值范围是[0.0,1.0],代表0%-100%。
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901172917203-2145193799.png)


- 测试

代码

```
/**
 * Sentinel降级-异常比例
 *
 * @return
 */
@GetMapping("/testDException")
public String testDException() {
    log.info(Thread.currentThread().getName() + "\t" + "...testDException DEGRADE_GRADE_EXCEPTION_RATIO");
    int age = 10 / 0;
    return "------testDException DEGRADE_GRADE_EXCEPTION_RATIO";
}
```


sentinel降级配置
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901173556916-1544894828.png)


测试
http://localhost:8401/testDException

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901173941232-725077020.png)



按照上述配置,单独访问一次,必然来一次报错一次(int age=10/0),调一次错一次；

异常比例(DEGRADE_GRADE_EXCEPTION_RATIO)：当资源的每秒请求量>=5,并且每秒异常总数占通过量的比值超过阈值(DegradeRule中的count)之后,资源进入降级状态,即在接下的时间窗口(DegradeRule中的timeWindow,以s为单位)之内,对这个方法的调用都会自动地返回。异常比率的闽值范围是[e.e,1.e],代表0%-100%。
开启jmeter后,直接高并发发送请求,多次调用达到我们的配置条件了。
断路器开启(保险丝跳闸),微服务不可用了,不再报错error而是服务降级了。

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901204702307-1218864240.png)


##### 异常数
- 是什么

异常数（DEGRADE_GRADE_EXCEPTION_COUNT）：当资源近1分钟的异常数目超过闽值之后会进行熔断。注意由于统计时间窗口是分钟级别的,若timelindow小于60s,则结束熔断状态后仍可能再进入熔断状态。

>时间窗口期一定要大于60秒

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901205033681-954665295.png)


- 异常数是按照分钟统计的

- 测试

代码
```
/**
 * Sentinel降级-异常数
 *
 * @return
 */
@GetMapping("/testE")
public String testE() {
    log.info("testE 测试异常数");
    int age = 10 / 0;
    return "------testE 测试异常数";
}
```

sentinel降级配置
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901205307077-63184014.png)


http://localhost:8401/testE,第一次访问绝对报错,因为除数不能为零,我们看到error窗口,但是达到5次报错后,进入熔断后降级。

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901205426148-154790917.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901205522441-1607881654.png)



### 热点key限流
#### 基本介绍
- 是什么
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901210328620-176928518.png)


#### 官网

[热点参数限流文档Githu官网](https://github.com/alibaba/Sentinel/wiki/热点参数限流)


#### 复习 (@SentinelResource)

- 兜底方法
分为系统默认和客户自定义,两种

之前的case,限流出问题后,都是用sentinel系统默认的提示：Blocked by Sentinel（flow limiting）

我们能不能自定？类似hystrix,某个方法出问题了,就找对应的兜底降级方法？

- 结论
从HystrixCommand 到@SentinelResource


#### 代码

- sentinel默认异常的源码
**com.alibaba.csp.sentinel.slots.block.BlockException**

- 自定义接口
```
/**
 * 测试热点规则(何为热点？热点即经常访问的数据。很多时候我们希望统计某个热点数据中访问频次最高的 Top K 数据,并对其访问进行限制。)
 *
 * blockHandler 如果跟我们热点配置的sentinel规则的就会条调到该方法类似于兜底的方法
 * @param p1
 * @param p2
 * @return
 */
@GetMapping("/testHotKey")
@SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey")
public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
                         @RequestParam(value = "p2", required = false) String p2) {
    //int age = 10/0;
    return "------testHotKey";
}
```

#### 配置

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901211637033-1435870430.png)


- 第一种情况

>@SentinelResource(value = "testHotKey")
>异常打到了前台用户界面看到,不友好

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901212350732-1668503831.png)



- 第二种情况(建议使用)
>@SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey")
>接口方法testHotKey里面第一个参数只要QPS超过每秒1次,马上降级处理,用了我们自己定义的方法

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901211807223-1824846836.png)



#### 测试
- http://localhost:8401/testHotKey
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901211320493-965461186.png)

- http://localhost:8401/testHotKey?p1=1&p2=2
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901211407211-1563760163.png)

- 连续点击两次》http://localhost:8401/testHotKey?p1=1
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901211807223-1824846836.png)

- 不带p1参数》http://localhost:8401/testHotKey?p2=1
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901212737552-719682861.png)


多次点击没有降级效果


#### 参数例外项
上述演示了第一个参数p1,当QPS超过1秒1次点击后马上被限流

##### 特例情况
普通：超过1秒钟一个后,达到阈值1后马上被限流

我们期望p1参数当他是某个特殊值时,它的限流值和平时不一样

特例：假如当p1的值等于5时,它的阈值可以达到200


##### 配置
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901213617670-1849784670.png)


##### 测试

- 连续点击五次》http://localhost:8401/testHotKey?p1=1
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901213648190-1918180505.png)

- 连续点击五次》http://localhost:8401/testHotKey?p1=5
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901213703557-2002505380.png)

>当p1等于5的时候,阈值变为200;当p1不等于5的时候,阈值就是平常的1


##### 前提条件
热点参数的注意点,参数必须是基本类型或者String


#### 其他

- 添加异常看看
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200901214642895-1077007146.png)

- @SentinelResource处理的是sentinel控制台配置的违规情况,有blockHandler方法配置的兜底处理；

RuntimeException int age=10/0 这个是java运行时报出的运行时异常RunTimeException,@SentinelResource不管

总结
>@SentinelResource主管配置出错,运行出错该走异常走异常


### 系统规则
#### 是什么

https://github.com/alibaba/Sentinel/wiki/%E7%B3%BB%E7%BB%9F%E8%87%AA%E9%80%82%E5%BA%94%E9%99%90%E6%B5%81

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200902150436808-1652434326.png)


#### 各项配置参数说明
- Load 自适应（仅对 Linux/Unix-like 机器生效）：系统的 load1 作为启发指标，进行自适应系统保护。当系统 load1 超过设定的启发值，且系统当前的并发线程数超过估算的系统容量时才会触发系统保护（BBR 阶段）。系统容量由系统的 maxQps * minRt 估算得出。设定参考值一般是 CPU cores * 2.5。

- CPU usage（1.5.0+ 版本）：当系统 CPU 使用率超过阈值即触发系统保护（取值范围 0.0-1.0），比较灵敏。

- 平均 RT：当单台机器上所有入口流量的平均 RT 达到阈值即触发系统保护，单位是毫秒。

- 并发线程数：当单台机器上所有入口流量的并发线程数达到阈值即触发系统保护。

- 入口 QPS：当单台机器上所有入口流量的 QPS 达到阈值即触发系统保护。


#### 配置全局QPS

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200902151301290-491597607.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200902151356432-1486202955.png)
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200902151408459-1244882970.png)


### @SentinelResource
#### 按资源名称限流+后续处理

- 启动Nacos成功

- 启动Sentinel成功

- Module

springcloud-alibaba-sentinel-service8401

POM
```xml
<!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
<dependency>
    <groupId>com.qcl.springcloud</groupId>
    <artifactId>springcloud-api-commons</artifactId>
    <version>${project.version}</version>
</dependency>
```

YML
```yml
还是原来的配置
```


业务类RateLimitController

**com.qcl.springcloud.alibaba.controller.RateLimitController**

主启动

**com.qcl.springcloud.alibaba.SentinelMain8401**


- 配置流控规则

`配置步骤`

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200902153126481-447243204.png)


`图形配置和代码关系`
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200902224223232-1792994558.png)

`表示一秒钟查询次数大于1,就跑到我们自定义的限流处`



- 测试

一秒钟点击一下

超过上述，疯狂点击，返回了自己定义的限流处理信息，限流发生


- 额外问题
此时关闭服务8401

Sentinel控制台,流控规则消失(临时/持久)



#### 按照Url地址限流+后续处理

通过访问的URL来限流，会返回Sentinel自带默认的限流处理信息

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200902225131249-294299193.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200902225844353-277505047.png)


#### 上面兜底方案面临的问题

- 1.系统默认的，没有体现我们自己的业务要求。

- 2.依照现有条件，我们自定义的处理方法又和业务代码耦合在一块，不直观。

- 3.每个业务方法都添加一个兜底的，那代码膨胀加剧。

- 4.全局统一的处理方法没有体现。


#### 客户自定义限流处理逻辑

##### 创建CustomerBlockHandler类用于自定义限流处理逻辑

##### 自定义限流处理类

**com.qcl.springcloud.alibaba.myhandler.CustomerBlockHandler**


##### RateLimitController

**com.qcl.springcloud.alibaba.controller.RateLimitController**


##### 启动微服务后先调用一次

http://localhost:8401/rateLimit/customerBlockHandler


##### Sentinel控制台配置
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200903100424249-1974519168.png)

##### 测试后我们自定义的出来了
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200903100445112-1301956621.png)

##### 进一步说明
业务和代码进行了解耦,避免代码膨胀

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200903100636537-1043071874.png)

```
@GetMapping("/rateLimit/customerBlockHandler")
@SentinelResource(value = "customerBlockHandler",
        blockHandlerClass = CustomerBlockHandler.class,
        blockHandler = "handlerException2")
```

>blockHandlerClass 指定处理限流逻辑的类;blockHandler指定处理逻辑类里面的哪个方法名来处理



#### 更多注解属性说明

[查看官网-介绍](https://hub.fastgit.org/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D)

[查看官网-注解支持](https://hub.fastgit.org/alibaba/Sentinel/wiki/%E6%B3%A8%E8%A7%A3%E6%94%AF%E6%8C%81)


![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200903101348558-1045090863.png)

[查看官网-如何使用](https://hub.fastgit.org/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200903101906783-1103561005.png)

>所有的代码都要用try-catch-finally方式进行处理


##### Sentinel三个核心的API
- SphU定义资源

- Tracer定义统过

- ContextUtil定义了上下文

