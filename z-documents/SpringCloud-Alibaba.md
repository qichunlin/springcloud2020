## 17.SpringCloud Alibaba 入门简介
https://www.bilibili.com/video/BV18E411x7eT?p=95



### 为什么会出现 SpringCloud Alibaba
#### SpringCloud Netflix项目进入维护模式
https://spring.io/blog/2018/12/12/spring-cloud-greenwich-rc1-available-now
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815145323068-1455430704.png)



#### Spring Cloud Netflix Projects Entering Maintenance Mode
##### 什么是维护模式?
将模块置于维护模式,意味着Spring Cloud团队将不会再向模块添加新功能。
我们将修复block级别的bug 以及安全问题,我们也会考虑并审查社区的小型 pull request。


##### 进入维护模式意味着什么?
Spring Cloud Netflix 将不再开发新的组件

  我们都知道Spring Cloud 版本迭代算是比较快的,因而出现了很多重大ISSUE都还来不及Fix就又推另一个Release了。
进入维护模式意思就是目前一直以后一段时间Spring Cloud Netflix提供的服务和功能就这么多了,不在开发新的组件
和功能了。以后将以维护和Merge分支Full Request为主新组件功能将以其他替代平代替的方式实现

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815145713208-1318531318.png)



### SpringCloud Alibaba带来了什么
#### 是什么?

[Alibaba GitHub官网](https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md)

诞生：
2018.10.31,Spring Cloud Alibaba 正式入驻了 Spring Cloud 官方孵化器,并在Maven中央库发布了第一个版本。
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815150510493-290637071.png)


#### 能干嘛

- 服务限流降级：默认支持Servlet、Feign、RestTemplate、Dubbo和RocketMQ限流降级功能的接入,可以在运行时通过控制台实时修改限流降级规则,还支持查看限流降级 Metrics监控。

- 服务注册与发现：适配Spring Cloud服务注册与发现标准,默认集成了Ribbon的支持。

- 分布式配置管理：支持分布式系统中的外部化配置,配置更改时自动刷新。

- 消息驱动能力：基于Spring Cloud Stream为微服务应用构建消息驱动能力。

- 阿里云对象存储：阿里云提供的海量、安全、低成本、高可靠的云存储服务。支持在任何应用、任何时间、任何地点存储和访问任意类型的数据。

- 分布式任务调度：提供秒级、精准、高可靠、高可用的定时（基于Cron表达式）任务调度服务。同时提供分布式的任务执行模型,如网格任务。网格任务支持海量子任务均匀分配到所有 Worker（schedulerx-client）上执行。

#### 去哪下
https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md

```xml
<!--spring cloud alibaba 2.1.0.RELEASE-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2.2.0.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```


### SpringCloud Alibaba学习资料获取

[SpringCloud Alibaba官网](https://spring.io/projects/spring-cloud-alibaba#overview)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200815153801571-664322731.png)

Spring Cloud Alibaba 致力于提供微服务开发的一站式解决方案。此项目包含开发分布式应用微服务的必需组件，方便开发者通过Spring Cloud 编程模型轻松使用这些组件来开发分布式应用服务。
依托Spring Cloud Alibaba，您只需要添加一些注解和少量配置，就可以将 Spring Cloud应用接入阿里微服务解决方案，通过阿里中间件来迅速搭建分布式应用系统。


[SpringCloud Alibaba GitHub官网-英文版](https://github.com/alibaba/spring-cloud-alibaba)

[SpringCloud Alibaba GitHub官网-中文版](https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md)
