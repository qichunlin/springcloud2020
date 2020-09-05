## 20.SpringCloud Alibaba Seata 处理分布式事务
https://www.bilibili.com/video/BV18E411x7eT?p=138
https://www.bilibili.com/video/BV18E411x7eT?p=139
https://www.bilibili.com/video/BV18E411x7eT?p=140
https://www.bilibili.com/video/BV18E411x7eT?p=141
https://www.bilibili.com/video/BV18E411x7eT?p=142
https://www.bilibili.com/video/BV18E411x7eT?p=143
https://www.bilibili.com/video/BV18E411x7eT?p=144
https://www.bilibili.com/video/BV18E411x7eT?p=145
https://www.bilibili.com/video/BV18E411x7eT?p=146
https://www.bilibili.com/video/BV18E411x7eT?p=147
https://www.bilibili.com/video/BV18E411x7eT?p=148


### 分布式事务问题

#### 分布式前
单机单库没这个问题

从1：1->1：N->N:N

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904113930271-471721516.png)

#### 分布式后

单体应用被拆分成微服务应用,原来的三个模块被拆分成三个独立的应用,分别使用三个独立的数据源,业务操作需要调用三个服务来完成。此时每个服务内部的数据一致性由本地事务来保证,但是全局的数据一致性问题没法保证。


#### 一句话

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904114110802-780222758.png)


### Seata简介
#### 是什么

Seata是一款开源的分布式事务解决方案,致力于在微服务架构下提供高性能和简单易用的分布式事务服务。

官网地址：http://seata.io/zh-cn/

http://seata.io/zh-cn/docs/overview/what-is-seata.html

https://hub.fastgit.org/seata/seata



#### 能干嘛
##### 一个典型的分布式事务过程

- 分布式事务处理过程的一 ID + 三组件模型 (Seata是由 1 [全局唯一的事务ID] + 3 [TC+TM+RM]的套件组成)

`Transaction ID XID：全局唯一的事务ID`


3个组件概念：
  
  Transaction Coordinator(TC)事务协调器,维护全局事务的运行状态,负责协调并驱动全局事务的提交或回滚;
  Transaction Manager(TM)控制全局事务的边界,负责于开启一个全局事务,并最终发起全局提交或全局回滚的协议;
  Resource Manager(RM)控制分支事务,负责分支注册、状态汇报,并接收事务协调器的指令,驱动分支(本地)事务的提交和回滚


![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904140305483-333092100.png)


- 处理过程

1.TM向TC申请开启一个全局事务,全局事务创建成功并生成一个全局唯一的XID;

2.XID在微服务调用链路的上下文中传播;

3.RM向TC注册分支事务,将其纳入XID对应全局事务的管辖;

4.TM向TC发起针对XID的全局提交或回滚决议;

5.TC调度XID下管辖的全部分支事务完成提交或回滚请求。

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904140829736-1395122533.png)


>RM就是数据库


#### 去哪下

http://seata.io/zh-cn/blog/download.html

[Seata-Github-1.1.0.zip下载](https://github.com/seata/seata/releases/download/v1.1.0/seata-server-1.1.0.zip)


#### 怎么玩

##### 本地@Transactional

##### 全局@GlobalTransactional

- SEATA的分布式交易解决方案
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904141732793-362115104.png)


### Seata-Server安装

#### 1.官网地址

[Seata-Github-1.1.0.zip下载](https://github.com/seata/seata/releases/download/v1.1.0/seata-server-1.1.0.zip)

Seata-Github-0.9.0.zip下载：链接：https://pan.baidu.com/s/1SiiINocb248OedX31IPj2g  提取码：arxs


#### 2.下载版本

1.1.0 或者是 0.9.0

#### 3.seata-server-1.1.0.zip解压到指定目录并修改conf目录下的file.conf配置文件

- 主要修改：自定义事务组名称+事务日志存储模式为db+数据库连接信息

- file.conf

service模块：vgroupMapping.my_test_tx_group = "legend_tx_group"
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904143237146-361934917.png)

store模块：从原来默认的  mode = "file" 修改为db形式 并修改数据库的连接信息
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904143323043-1936810561.png)


#### 4.mysql5.7数据库新建库seata

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904143448746-10786437.png)


#### 5.在seata库里建表

建表db_store.sql在\Seata\seata-server-0.9.0\seata\conf目录里面  db_store.sql,如果没有 可以下载 [db_store.sql下载地址](https://hub.fastgit.org/seata/seata/blob/v0.9.0/server/src/main/resources/db_store.sql) 

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904144506381-1323659336.png)


#### 6.修改seata-server-1.1.0\seata\conf目录下的registry.conf配置文件

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904144854066-532326348.png)

>目的是：指明注册中心为nacos,及修改nacos连接信息


#### 7.先启动Nacos端口号8848
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904145203611-167659666.png)


#### 8.再启动seata-server

D:\software\Seata\seata-server-1.1.0\seata\bin\seata-server.bat

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904145319462-1149713229.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904145449831-668632648.png)


### 订单/库存/账户业务数据库准备

#### 以下演示都需要先启动Nacos后启动Seata,保证两个都OK
Seata没启动报错no available server to connect


#### 分布式事务业务说明

- 业务说明
这里我们会创建三个服务,一个订单服务,一个库存服务,一个账户服务。

当用户下单时,会在订单服务中创建一个订单,然后通过远程调用库存服务来扣减下单商品的库存,再通过远程调用账户服务来扣减用户账户里面的余额,最后在订单服务中修改订单状态为已完成。

该操作跨越三个数据库,有两次远程调用,很明显会有分布式事务问题。


- 下订单--->扣库存-->减账户 (余额)



#### 创建业务数据库

seata_order：存储订单的数据库;

seata_storage：存储库存的数据库;

seata_account：存储账户信息的数据库。


建库SQL
```
CREATE DATABASE seata_order; 

CREATE DATABASE seata_storage;

CREATE DATABASE seata_account;
```

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904150510855-378807330.png)



#### 按照上述3库分别建对应业务表

seata_order库下建t_order表

```sql
CREATE TABLE t_order (
`id` BIGINT ( 11 ) NOT NULL AUTO_INCREMENT PRIMARY KEY,
`user_id` BIGINT ( 11 ) DEFAULT NULL COMMENT '用户id',
`product_id` BIGINT ( 11 ) DEFAULT NULL COMMENT '产品id',
`count` INT ( 11 ) DEFAULT NULL COMMENT '数量',
`money` DECIMAL ( 11, 0 ) DEFAULT NULL COMMENT '金额',
`status` INT ( 1 ) DEFAULT NULL COMMENT '订单状态：0：创建中;1：已完结' 
) ENGINE = INNODB AUTO_INCREMENT = 7 DEFAULT CHARSET = utf8;


SELECT * FROM t_order;
```
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904151447617-1412602998.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904150842130-1353049680.png)


seata_storage库下建t_storage表

```sql
CREATE TABLE t_storage (
`id` BIGINT ( 11 ) NOT NULL AUTO_INCREMENT PRIMARY KEY,
`product_id` BIGINT ( 11 ) DEFAULT NULL COMMENT '产品id',
`total` INT ( 11 ) DEFAULT NULL COMMENT '总库存',
`used` INT ( 11 ) DEFAULT NULL COMMENT '已用库存',
`residue` INT ( 11 ) DEFAULT NULL COMMENT '剩余库存'
) ENGINE = INNODB AUTO_INCREMENT = 2 DEFAULT CHARSET = utf8;

INSERT INTO t_storage(`id`,`product_id`,`total`,`used`,`residue`) VALUES('1','1','100','0','100');


SELECT * FROM t_storage;
```
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904152310916-886654054.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904153135007-570231131.png)


seata_account库下建t_account表

```sql
CREATE TABLE t_account (
`id` BIGINT ( 11 ) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
`user_id` BIGINT ( 11 ) DEFAULT NULL COMMENT '用户id',
`total` DECIMAL ( 10, 0 ) DEFAULT NULL COMMENT '总额度',
`used` DECIMAL ( 10, 0 ) DEFAULT NULL COMMENT '已用余额',
`residue` DECIMAL ( 10, 0 ) DEFAULT 0 COMMENT '剩余可用额度' 
) ENGINE = INNODB AUTO_INCREMENT = 2 DEFAULT CHARSET = utf8;

INSERT INTO seata_account.t_account(`id`,`user_id`,`total`,`used`,`residue`)VALUES(1,1,'1000','0',1000);

SELECT * FROM t_account;
```
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904153637673-61025372.png)

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904153114621-290720218.png)


#### 按照上述3库分别建对应的回滚日志表

订单-库存-账户3个库下都需要建各自的回滚日志表
\seata-server-1.1.0\seata\conf目录下的db_undo_log.sql  [db_undo_log.sql下载地址](https://hub.fastgit.org/seata/seata/blob/v0.9.0/server/src/main/resources/db_undo_log.sql)

建表SQL
```sql
-- the table to store seata xid data
-- 0.7.0+ add context
-- you must to init this sql for you business databese. the seata server not need it.
-- 此脚本必须初始化在你当前的业务数据库中，用于AT 模式XID记录。与server端无关（注：业务数据库）
-- 注意此处0.3.0+ 增加唯一索引 ux_undo_log
-- drop table `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```


#### 最终效果
![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904155114818-2127883935.png)


### 订单/库存/账户业务微服务准备

#### 业务需求

下订单->减库存>扣余额>改（订单）状态


#### 新建订单Order-Module

- 1.springcloud-seata-order-service2001

- 2.POM
```xml
<dependencies>
    <!--nacos-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    <!--seata-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
        <!--seata排除原来的-->
        <exclusions>
            <exclusion>
                <artifactId>seata-all</artifactId>
                <groupId>io.seata</groupId>
            </exclusion>
        </exclusions>
    </dependency>
    <!--seata引入自己的版本-->
    <dependency>
        <groupId>io.seata</groupId>
        <artifactId>seata-all</artifactId>
        <version>0.9.0</version>
    </dependency>
    <!--feign-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <!--web-actuator-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <!--mysql-druid-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.37</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.1.10</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

- 3.YML
```yaml
server:
  port: 2001

spring:
  application:
    name: springcloud-seata-order-service
  cloud:
    alibaba:
      seata:
        #自定义事务组名称需要与seata-server中的对应
        tx-service-group: legend_tx_group
    nacos:
      discovery:
        server-addr: localhost:8848
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/seata_order
    username: root
    password: 123456


#hystrix启用feign支持
feign:
  hystrix:
    enabled: false


#seata日志
logging:
  level:
    io:
      seata: info


#Mybatis配置
mybatis:
  mapperLocations: classpath:mapper/*.xml
```

- 4.file.conf

```
transport {
  # tcp udt unix-domain-socket
  type = "TCP"
  #NIO NATIVE
  server = "NIO"
  #enable heartbeat
  heartbeat = true
  #thread factory for netty
  thread-factory {
    boss-thread-prefix = "NettyBoss"
    worker-thread-prefix = "NettyServerNIOWorker"
    server-executor-thread-prefix = "NettyServerBizHandler"
    share-boss-worker = false
    client-selector-thread-prefix = "NettyClientSelector"
    client-selector-thread-size = 1
    client-worker-thread-prefix = "NettyClientWorkerThread"
    # netty boss thread size,will not be used for UDT
    boss-thread-size = 1
    #auto default pin or 8
    worker-thread-size = 8
  }
  shutdown {
    # when destroy server, wait seconds
    wait = 3
  }
  serialization = "seata"
  compressor = "none"
}

service {

  vgroup_mapping.fsp_tx_group = "default" #修改自定义事务组名称

  default.grouplist = "127.0.0.1:8091"
  enableDegrade = false
  disable = false
  max.commit.retry.timeout = "-1"
  max.rollback.retry.timeout = "-1"
  disableGlobalTransaction = false
}


client {
  async.commit.buffer.limit = 10000
  lock {
    retry.internal = 10
    retry.times = 30
  }
  report.retry.count = 5
  tm.commit.retry.count = 1
  tm.rollback.retry.count = 1
}

## transaction log store
store {
  ## store mode: file、db
  mode = "db"

  ## file store
  file {
    dir = "sessionStore"

    # branch session size , if exceeded first try compress lockkey, still exceeded throws exceptions
    max-branch-session-size = 16384
    # globe session size , if exceeded throws exceptions
    max-global-session-size = 512
    # file buffer size , if exceeded allocate new buffer
    file-write-buffer-cache-size = 16384
    # when recover batch read size
    session.reload.read_size = 100
    # async, sync
    flush-disk-mode = async
  }

  ## database store
  db {
    ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp) etc.
    datasource = "dbcp"
    ## mysql/oracle/h2/oceanbase etc.
    db-type = "mysql"
    driver-class-name = "com.mysql.jdbc.Driver"
    url = "jdbc:mysql://127.0.0.1:3306/seata"
    user = "root"
    password = "123456"
    min-conn = 1
    max-conn = 3
    global.table = "global_table"
    branch.table = "branch_table"
    lock-table = "lock_table"
    query-limit = 100
  }
}
lock {
  ## the lock store mode: local、remote
  mode = "remote"

  local {
    ## store locks in user's database
  }

  remote {
    ## store locks in the seata's server
  }
}
recovery {
  #schedule committing retry period in milliseconds
  committing-retry-period = 1000
  #schedule asyn committing retry period in milliseconds
  asyn-committing-retry-period = 1000
  #schedule rollbacking retry period in milliseconds
  rollbacking-retry-period = 1000
  #schedule timeout retry period in milliseconds
  timeout-retry-period = 1000
}

transaction {
  undo.data.validation = true
  undo.log.serialization = "jackson"
  undo.log.save.days = 7
  #schedule delete expired undo_log in milliseconds
  undo.log.delete.period = 86400000
  undo.log.table = "undo_log"
}

## metrics settings
metrics {
  enabled = false
  registry-type = "compact"
  # multi exporters use comma divided
  exporter-list = "prometheus"
  exporter-prometheus-port = 9898
}

support {
  ## spring
  spring {
    # auto proxy the DataSource bean
    datasource.autoproxy = false
  }
}
```

- 5.registry.conf

```
registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  type = "nacos"

  nacos {
    serverAddr = "localhost:8848"
    namespace = ""
    cluster = "default"
  }
  eureka {
    serviceUrl = "http://localhost:8761/eureka"
    application = "default"
    weight = "1"
  }
  redis {
    serverAddr = "localhost:6379"
    db = "0"
  }
  zk {
    cluster = "default"
    serverAddr = "127.0.0.1:2181"
    session.timeout = 6000
    connect.timeout = 2000
  }
  consul {
    cluster = "default"
    serverAddr = "127.0.0.1:8500"
  }
  etcd3 {
    cluster = "default"
    serverAddr = "http://localhost:2379"
  }
  sofa {
    serverAddr = "127.0.0.1:9603"
    application = "default"
    region = "DEFAULT_ZONE"
    datacenter = "DefaultDataCenter"
    cluster = "default"
    group = "SEATA_GROUP"
    addressWaitTime = "3000"
  }
  file {
    name = "file.conf"
  }
}

config {
  # file、nacos 、apollo、zk、consul、etcd3
  type = "file"

  nacos {
    serverAddr = "localhost"
    namespace = ""
  }
  consul {
    serverAddr = "127.0.0.1:8500"
  }
  apollo {
    app.id = "seata-server"
    apollo.meta = "http://192.168.1.204:8801"
  }
  zk {
    serverAddr = "127.0.0.1:2181"
    session.timeout = 6000
    connect.timeout = 2000
  }
  etcd3 {
    serverAddr = "http://localhost:2379"
  }
  file {
    name = "file.conf"
  }
}
```

- 6.domain

**com.qcl.springcloud.alibaba.seata.domain.CommonResult**

**com.qcl.springcloud.alibaba.seata.domain.Order**


- 7.Dao接口及实现

**com.qcl.springcloud.alibaba.seata.dao.OrderDao**

**D:\WorkCode\ideaWork\springcloud2020\springcloud-seata-order-service2001\src\main\resources\mapper\OrderMapper.xml**


- 8.Service接口及实现

**com.qcl.springcloud.alibaba.seata.service.OrderService**
**com.qcl.springcloud.alibaba.seata.service.impl.OrderServiceImpl**


- 9.Controller

**com.qcl.springcloud.alibaba.seata.controller.OrderController**


- 10.Config配置

**com.qcl.springcloud.alibaba.seata.config.DataSourceProxyConfig**


- 11.主启动

**com.qcl.springcloud.alibaba.seata.SeataOrderMain2001**

145开始

#### 新建库存Storage-Module

- 1.springcloud-seata-storage-service2002

- 2.POM

- 3.YML

- 4.file.conf

- 5.registry.conf

- 6.domain

![](https://img2020.cnblogs.com/blog/1231979/202009/1231979-20200904165249564-982714568.png)

`CommonResult`

`Order`



- 7.Dao接口及实现

- 8.Service接口及实现

- 9.Controller

- 10.Config配置

- 11.主启动


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
