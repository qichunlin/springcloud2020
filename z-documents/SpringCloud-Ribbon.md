## 8.Ribbon负载均衡服务调用
https://www.bilibili.com/video/BV18E411x7eT?p=36
https://www.bilibili.com/video/BV18E411x7eT?p=37
https://www.bilibili.com/video/BV18E411x7eT?p=38
https://www.bilibili.com/video/BV18E411x7eT?p=39
https://www.bilibili.com/video/BV18E411x7eT?p=40
https://www.bilibili.com/video/BV18E411x7eT?p=41
https://www.bilibili.com/video/BV18E411x7eT?p=42


### 概述
#### 是什么
  Spring Cloud Ribbon是基于Netflix Ribbon实现的一套客户端负载均衡的工具。
简单的说,Ribbon是Netflix发布的开源项目,主要功能是提供客户端的软件负载均衡算法和服务调用。
Ribbon客户端组件提供一系列完善的配置项如连接超时,重试等。简单的说,就是在配置文件中列出Load Balancer（简称LB）
后面所有的机器,Ribbon会自动的帮助你基于某种规则（如简单轮询,随机连接等）去连接这些机器。我们很容易使用Ribbon实现自定义的负载均衡算法。


#### 官网资料
https://github.com/Netflix/ribbon/wiki/Getting-Started

Ribbon目前也进入维护模式
    未来替代方案
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200820085517391-784250847.png)
    


#### 能干什么
##### LB（负载均衡）
- LB负载均衡（Load Balance）是什么
简单的说就是将用户的请求平摊的分配到多个服务上,从而达到系统的HA（高可用）。
常见的负载均衡有软件Nginx,LVS,硬件F5等。


- Ribbon本地负载均衡客户端 VS Nginx服务端负载均衡区别

Nginx是服务器负载均衡,客户端所有请求都会交给nginx,然后由nginx实现转发请求。即负载均衡是由服务端实现的。


Ribbon本地负载均衡,在调用微服务接口时候,会在注册中心上获取注册信息服务列表之后缓存到JVM本地,从而在本地实现RPC远程服务调用技术。


- 集中式LB (Nginx服务端负载均衡)

即在服务的消费方和提供方之间使用独立的LB设施（可以是硬件,如F5,也可以是软件,如nginx）,由该设施负责把访问请求通过某种策略转发至服务的提供方；


- 进程内LB (Ribbon本地负载均衡)
  将LB逻辑集成到消费方,消费方从服务注册中心获知有哪些地址可用,然后自己再从这些地址中选择出一个合适的服务器。
Ribbon就属于进程内LB,它只是一个类库,集成于消费方进程,消费方通过它来获取到服务提供方的地址。



##### 前面我们讲解过了80通过轮询负载访问8001/8002


##### 一句话

Ribbon就是负载均衡 + Rest Template


### Ribbon负载均衡演示

#### 架构说明

总结：Ribbon其实就是一个软负载均衡的客户端组件,他可以和其他所需请求的客户端结合使用,和eureka结合只是其中的一个实例

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200820090805742-990464626.png)


Ribbon在工作时分成两步
第一步先选择 EurekaServer,它优先选择在同一个区域内负载较少的server.
第二步再根据用户指定的策略,在从server取到的服务注册列表中选择一个地址。
其中Ribbon提供了多种策略：比如轮询、随机和根据响应时间加权。


#### POM
之前写样例时候没有引入spring-cloud-starter-ribbon也可以使用ribbon,

```xml
<dependency>
    <groupld>org.springframework.cloud</groupld>
    <artifactld>spring-cloud-starter-netflix-ribbon</artifactld>
</dependency>

```
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200820190344919-844715486.png)



#### 二说RestTemplate的使用
##### 官网

[RestTemplate资料](https://docs.spring.io/spring-framework/docs/5.2.2.RELEASE/javadoc-api/org/springframework/web/client/RestTemplate.html)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200820215426466-1403731322.png)


##### getForObject方法/getForEntity方法

getForObject：
返回对象为响应体中数据转化成的对象,基本上可以理解为Json


getForEntity：
返回对象为ResponseEntity对象,包含了响应中的一些重要信息,比如响应头、响应状态码、响应体等


##### postForObject方法/postForEntity方法
同上


### Ribbon核心组件IRule

#### IRule：根据特定算法中从服务列表中选取一个要访问的服务

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200820225108744-1175567456.png)

com.netflix.loadbalancer.RoundRobinRule轮询
com.netflix.loadbalancer.RandomRule 随机
com.netflix.loadbalancer.RetryRule先按照RoundRobinRule的策略获取服务,如果获取服务失败则在指定时间内会进行重试,获取可用的服务
WeightedResponseTimeRule 
BestAvailableRule 会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务,然后选择一个并发量最小的服务
AvailabilityFilteringRule先过滤掉故障实例,再选择并发较小的实例
ZoneAvoidanceRule默认规则,复合判断server所在区域的性能和server的可用性选择服务器


#### 如何替换

- 修改springcloud-consumer-order80


- 注意配置细节

官方文档明确给出了警告：
这个自定义配置类不能放在@ComponentScan所扫描的当前包下以及子包下,否则我们自定义的这个配置类就会被所有的Ribbon客户端所共享,达不到特殊化定制的目的了。

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200820230639264-1426461461.png)



- 新建package  com.qcl.myrule

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200820230800162-152633901.png)


- 上面包下新建MySelfRule规则类

**com.qcl.myrule.MySelfRule**


- 主启动类添加@RibbonClient了

**com.qcl.springcloud.OrderMain80**

>@RibbonClient(name = "SPRINGCLOUD-PAYMENT-SERVICE", configuration = MySelfRule.class)


- 测试

**http://localhost/consumer/api/payment/get/1**


### Ribbon负载均衡算法
#### 原理

负载均衡算法：rest接口第几次请求数%服务器集群总数量=实际调用服务器位置下标,每次服务重启动后rest接口计数从1开始。

List<Servicelnstance> instances = discoveryClient.getInstances("SPRINGCLOUD-PAYMENT-SERVICE");

如：
List[0] instances = 127.0.0.1:8002
List[1] instances = 127.0.0.1:8001
8001+8002组合成为集群,它们共计2台机器,集群总数为2,按照轮询算法原理：
当总请求数为1时：1%2=1对应下标位置为1,则获得服务地址为127.0.0.1:8001
当总请求数位2时：2%2=0对应下标位置为0,则获得服务地址为127.0.0.1:8002
当总请求数位3时：3%2=1对应下标位置为1,则获得服务地址为127.0.0.1:8001
当总请求数位4时：4%2=0对应下标位置为0,则获得服务地址为127.0.0.1:8002
如此类推.…


#### RoundRobinRule源码

**com.netflix.loadbalancerRoundRobinRule**


关键点
```
private AtomicInteger nextServerCyclicCounter;


public Server choose(ILoadBalancer lb, Object key) {
    if (lb == null) {
        log.warn("no load balancer");
        return null;
    } else {
        Server server = null;
        int count = 0;

        while(true) {
            if (server == null && count++ < 10) {
                List<Server> reachableServers = lb.getReachableServers();
                List<Server> allServers = lb.getAllServers();
                int upCount = reachableServers.size();
                int serverCount = allServers.size();
                if (upCount != 0 && serverCount != 0) {
                    int nextServerIndex = this.incrementAndGetModulo(serverCount);
                    server = (Server)allServers.get(nextServerIndex);
                    if (server == null) {
                        Thread.yield();
                    } else {
                        if (server.isAlive() && server.isReadyToServe()) {
                            return server;
                        }

                        server = null;
                    }
                    continue;
                }

                log.warn("No up servers available from load balancer: " + lb);
                return null;
            }

            if (count >= 10) {
                log.warn("No available alive servers after 10 tries from load balancer: " + lb);
            }

            return server;
        }
    }
}


//自旋锁
private int incrementAndGetModulo(int modulo) {
    int current;
    int next;
    do {
        current = this.nextServerCyclicCounter.get();
        next = (current + 1) % modulo;
    } while(!this.nextServerCyclicCounter.compareAndSet(current, next));

    return next;
}
```

[自旋锁CAS的相关知识点](https://www.bilibili.com/video/av48988279?from=search&seid=5479711710596585145)


#### 手写
原理 + JUC(CAS+自旋锁)

##### 7001/7002集群启动

##### 8001/8002微服务改造

controller

```
@GetMapping(value = "/lb")
public String getPaymentLB(){
    return serverPort;
}
```

##### 80订单微服务改造
- 1.ApplicationContextBean去掉注解 @LoadBalance


- 2.LoadBalance接口

**com.qcl.springcloud.loadbalance.LoadBalancer**


- 3.MyLoadBalancer实现类

**com.qcl.springcloud.loadbalance.MyLoadBalancer**


- 4.OrderController

```
@GetMapping(value = "/consumer/payment/lb")
public String getPaymentLoadBalance() {
    List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("SPRINGCLOUD-PAYMENT-SERVICE");

    if (CollectionUtils.isEmpty(serviceInstanceList)) {
        return null;
    }
    //获取提供服务的实例对象
    ServiceInstance serviceInstance = loadBalancer.instances(serviceInstanceList);
    URI uri = serviceInstance.getUri();
    return restTemplate.getForObject(uri + "/payment/lb", String.class);
}
```

- 5.测试

http://localhost/consumer/payment/Ib