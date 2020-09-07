## 20.SpringCloud Alibaba Seata 处理分布式事务2
https://www.bilibili.com/video/BV18E411x7eT?p=146
https://www.bilibili.com/video/BV18E411x7eT?p=147
https://www.bilibili.com/video/BV18E411x7eT?p=148


#### 新建账户Account-Module

- 1.springcloud-seata-account-service2003

- 2.POM

- 3.YML

- 4.file.conf

- 5.registry.conf

- 6.domain

- 7.Dao接口及实现

- 8.Service接口及实现

- 9.Controller

- 10.Config配置

- 11.主启动



### Test
#### 下订单>减库存>扣余额>改(订单）状态
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200906184355060-1233682421.png)


#### 数据库初始情况

```sql
SELECT * FROM seata_order.t_order;

SELECT * FROM seata_account.t_account;

SELECT * FROM seata_storage.t_storage;

```


![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200906184737781-1946469059.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200906184756331-397151523.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200906184811211-286218870.png)



### 正常下单

- 启动项目
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907095858718-1197479220.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907101131636-1513818098.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907101519661-1183580400.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907100426153-2120002462.png)



- 浏览器输入：
http://localhost:2001/order/create?userId=1&productId=1&count=10&money=10

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907102321827-1779680417.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907102340084-1914638743.png)


- 数据库情况：
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907102631318-1797086449.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907102648390-1518130921.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907102620687-747238499.png)



#### 超时异常,没加@GlobalTransactional

- AccountServiceImpl添加超时

```
//暂停几秒钟线程
try {
    TimeUnit.SECONDS.sleep(20);
} catch (InterruptedException e) {
    e.printStackTrace();
}
```

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907103151856-889839197.png)


- 数据库情况

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907103218221-1447292471.png)
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907103231666-1978506987.png)
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907103248725-1264654295.png)



- 故障情况

当库存和账户金额扣减后,订单状态并没有设置为已经完成,没有从零改为1而且由于feign的重试机制,账户余额还有可能被多次扣减



#### 超时异常,添加@GlobalTransactional

- AccountServiceImpl添加超时

```
//暂停几秒钟线程
try {
    TimeUnit.SECONDS.sleep(20);
} catch (InterruptedException e) {
    e.printStackTrace();
}
```

- OrderServiceImpl创建订单方法上面添加 @GlobalTransactional 注解

`@GlobalTransactional(name = "legend-create-order", rollbackFor = Exception.class)`

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907103645806-1308183766.png)


- 下单后数据库数据并没有任何改变：记录没有被添加进来
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907103912070-388827935.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907103931684-419880722.png)
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907103941693-1974050323.png)
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907103951141-797565227.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907104012536-1478167090.png)


>我们只需要使用一个@GlobalTransactional注解在业务方法上即可完成全局事务控制



### 补充
#### Seata

2019年1月份蚂蚁金服和阿里巴巴共同开源的分布式事务解决方案

Simple Extensible Autonomous Transaction Architecture,简单可扩展自治事务框架

2020起始,参加工作后用1.0以后的版本(GTS)
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200906232256604-1513484042.png)

>0.9版本不支持集群


#### 再看TC/TM/RM三大组件

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200906232424560-830435376.png)


>TC：seata的服务器
>那个方法头上标记@GlobalTransactional注解谁就是TM(事务的发起方)

>一个数据库就是RM(事务的参与方)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200906232723773-189079219.png)



##### 分布式事务的执行流程

TM开启分布式事务（TM向TC注册全局事务记录）；
按业务场景,编排数据库、服务等事务内资源（RM向TC汇报资源准备状态）；
TM结束分布式事务,事务一阶段结束（TM通知TC提交/回滚分布式事务）；
TC汇总事务信息,决定分布式事务是提交还是回滚；
TC通知所有RM提交/回滚资源,事务二阶段结束。



#### AT模式如何做到对业务的无侵入

##### 是什么
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907105055062-1628697019.png)


##### 一阶段加载

在一阶段,Seata 会拦截"业务SQL",
1.解析SQL语义,找到"业务SQL"要更新的业务数据,在业务数据被更新前,将其保存成 "before image",

2.执行 "业务SQL"更新业务数据,在业务数据更新之后,

3.其保存成 "after image",最后生成行锁。

>以上操作全部在一个数据库事务内完成,这样保证了一阶段操作的原子性。

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907105432773-930814220.png)


##### 二阶段提交

二阶段如果是顺利提交的话,因为“业务SQL"在一阶段已经提交至数据库,所以Seata框架只需将一阶段保存的快照数据和行锁删掉,完成数据清理即可。

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907105523037-812231749.png)


##### 二阶段回滚

二阶段回滚：
二阶段如果是回滚的话,Seata就需要回滚一阶段已经执行的“业务SQL”,还原业务数据。
回滚方式便是用“before image”还原业务数据；但在还原前要首先要校验脏写,对比“数据库当前业务数据”和“after image",如果两份数据完全一致就说明没有脏写,可以还原业务数据,如果不一致就说明有脏写,出现脏写就需要转人工处理。|

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907105720985-275661676.png)


#### debug

debug启动项目


#### 补充

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907110840802-121521004.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200907110930918-1200222403.png)
