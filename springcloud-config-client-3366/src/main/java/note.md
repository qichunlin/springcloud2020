## SpringCloudBus环境搭建 (由于RabbitMQ运行在Erlang环境中，所以首先安装Erlang)
注意:版本搭配:https://www.rabbitmq.com/news.html

### 搭建过程
#### (1)安装Erlang
http://erlang.org/download/otp_win64_21.3.exe

#### (2)RabbitMQ 3.7.14
https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Frabbitmq%2Frabbitmq-server%2Freleases%2Fdownload%2Fv3.7.14%2Frabbitmq-server-3.7.14.exe

![](https://img2020.cnblogs.com/blog/1231979/202005/1231979-20200501104742754-758492921.png)


#### (3)进入RabbitMQ安装目录下的sbin目录 例如:(D:\software\Rabbitmq\rabbitmq\rabbitmq_server-3.7.14\sbin)
输入如下命令启动管理:rabbitmq-plugins enable rabbitmq_management
![](https://img2020.cnblogs.com/blog/1231979/202005/1231979-20200501105800263-2128232486.png)

可视化插件很多自带的有


可视化插件中找到rabbitmq-start
http://localhost:15672/

输入账户密码:guest  guest (默认的)



### 设计思想
![](https://img2020.cnblogs.com/blog/1231979/202005/1231979-20200501111047751-2016894280.png)



### 发送请求 实现一次发送,处处生效(通过服务端去通知所有的客户端)
curl -X POST "http://localhost:3344/actuator/bus-refresh"

实现了自动版全局广播的动态刷新配置



rabbitMQ默认有一个topic叫springCloudBus



### Bus动态刷新定点通知(不想全部通知  指定某一个实例生效)
公式: curl -X POST "http://localhost:配置中心的端口号/actuator/bus-refresh/{destination}"

/bus-refresh 请求不再发送到具体的服务实例上,而是发给config server并通过 destination参数类指定需要更新配置的服务或实例

只通知3355配置刷新
curl -X POST "http://localhost:配置中心的端口号/actuator/bus-refresh/springcloudconfig-client:3355"