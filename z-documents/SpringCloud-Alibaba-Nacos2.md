## 18.SpringCloud Alibaba Nacos 服务注册与配置中心2
https://www.bilibili.com/video/BV18E411x7eT?p=106
https://www.bilibili.com/video/BV18E411x7eT?p=107
https://www.bilibili.com/video/BV18E411x7eT?p=108
https://www.bilibili.com/video/BV18E411x7eT?p=109
https://www.bilibili.com/video/BV18E411x7eT?p=110



### Nacos集群和持久化配置
#### 官网说明

- [集群部署说明](https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html)

- 官网架构图
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831160330723-1943118275.png)

- 官网架构图2
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831160646510-51386881.png)

- 说明 (https://nacos.io/zh-cn/docs/deployment.html)

默认Nacos使用嵌入式数据库实现数据的存储。所以,如果启动多个默认配置下的Nacos节点,数据存储是存在一致性问题的。
为了解决这个问题,Nacos采用了集中式存储的方式来支持集群化部署,目前只支持MySQL的存储。


![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831161415875-2057560519.png)

>再以单机模式启动nacos,nacos所有写嵌入式数据库的数据都写到了mysql


#### Nacos持久化配置解释
- Nacos默认自带的是嵌入式数据库derby

https://github.com/alibaba/nacos/blob/develog/config/pom.xml

- derby到mysql切换配置步骤


1.nacos-server-1.1.4\nacos\conf目录下找到sql脚本
   1.1 nacos-mysql.sql
   1.2 执行脚本
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831162429902-443023979.png)

2.nacos-server-1.1.4\nacos\conf目录下找到application.properties

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831162700848-316718671.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831162908827-846497276.png)

```
spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://localhost:3307/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=root
db.password=123456
```

- 启动Nacos,可以看到是个全新的空记录界面,以前是记录进derby,现在是存储在MySQL数据库

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831163453116-1921093743.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831163536271-1725839944.png)


![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831163719031-838965432.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831163751904-1530853094.png)



#### Linux版Nacos+MySQL生产环境配置

[集群部署官网说明](https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831164245784-741309792.png)


##### 预计需要,1个Nginx+3个Nacos注册中心+1个MySQL

##### Nacos下载Linux版
https://github.com/alibaba/nacos/releases/tag/1.1.4

nacos-server-1.1.4.tar.gz


解压后安装
tar -zxvf nacos-server-1.1.4.tar.gz



##### 集群配置步骤

1.Linux服务器上mysql数据库配置

SQL脚本在哪里
  /conf/nacos-mysql.sql

sql语句源文件(nacos-mysql.sql)

Linux机器上的Mysql数据库粘贴



2.application.properties 配置
cp application.properties.example application.properties

vi application.properties

```
##########################################################################################
spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://localhost:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=root
db.password=123456
```
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831212807161-1287652750.png)


3.Linux服务器上nacos的集群配置cluster.conf
梳理出三台nacos集器的不同服务端口号
复制出 cluster.conf
cp cluster.conf.example cluster.conf

vi cluster.conf

```
192.168.0.120:3333
192.168.0.120:4444
192.168.0.120:5555
```
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831212933618-709691085.png)


>这个IP不能写127.0.0.1,必须是Linux命令hostname -i能够识别的IP


4.编辑Nacos的启动脚本startup.sh,使它能够接受不同的启动端口
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831213319935-1957304307.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831213258325-937961687.png)

/mynacos/nacos/bin 目录下面有startup.sh

/mynacos/nacos/bin 目录下有startup.sh平时单机版的启动,都是./startup.sh即可。
但是
集群启动,我们希望可以类似其它软件的shell命令,传递不同的端口号启动不同的nacos实例。
命令：./startup.sh -p 3333 表示启动端口号为3333的nacos服务器实例,和上一步的cluster.conf配置的一致。

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831184914279-1598513812.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831185110852-454723556.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831185350412-362225691.png)

>修改内容：nohup $JAVA -Dserver.port=${PORT} ${JAVA_OPT} nacos.nacos >> ${BASE_DIR}/logs/start.out 2>&1 &


./startup.sh -p 3333


5.Nginx的配置,由它作为负载均衡器
 修改nginx的配置文件
 nginx.conf (cd /usr/src/nginx/sbin/    ./nginx  -c /root/nginx-1.8.1/conf/nginx.conf)
```

``` 
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831214605403-712185452.png)



 按照指定启动
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831214900236-917132917.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831215533769-1584024188.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831215624707-1260304634.png)



6.截止到此处,1个Nginx+3个nacos注册中心+1个mysql

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831215125748-1438116724.png)

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831215125666-1564914785.png)


查看nacos有几台集群
ps -ef|grep nacos |grep -v grep |wc -l

![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831223902547-2113993368.png)


参考博客 https://www.cnblogs.com/brightfang/p/12543860.html


##### 测试

##### 高可用小总结
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200831224757644-1661843690.png)
