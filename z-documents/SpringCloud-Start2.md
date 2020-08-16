## 4.微服务架构编码构建
https://www.bilibili.com/video/BV18E411x7eT?p=5
https://www.bilibili.com/video/BV18E411x7eT?p=6
https://www.bilibili.com/video/BV18E411x7eT?p=7

### 约定>配置>构建
约定大于配置

git提交流程
maven


### IDEA新建Project工作空间

总父工程
POM
    project
        Module


#### 微服务cloud整体聚合父工程project
##### 父工程步骤
- New Project
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200401134753882-85789748.png)
         
- 聚合总父工程名字
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200401134903028-1063713040.png)

- Maven选版本
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200401135309835-1480237609.png)

- 工程名字
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200401135604619-603234077.png)

- 字符编码
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200401140141328-1989201129.png)

- 注解生效激活
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200401140257105-1871780655.png)

- Java编译版本选8
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200401140516692-1595590463.png)

- File Type过滤
![](https://img2020.cnblogs.com/blog/1231979/202008/1231979-20200816223651513-97887296.png)


#### 父工程POM
```
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.qcl.springcloud</groupId>
    <artifactId>springcloud2020</artifactId>
    <version>1.0-SNAPSHOT</version>
    <!-- 表示是个pom是父工程-->
    <packaging>pom</packaging>

    <!--统一管理jar包和版本-->
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <junit.version>4.12</junit.version>
        <log4j.version>1.2.17</log4j.version>
        <lombok.version>1.18.10</lombok.version>
        <mysql.version>5.1.47</mysql.version>
        <druid.verison>1.1.10</druid.verison>
        <mybatis.spring.boot.verison>1.3.0</mybatis.spring.boot.verison>
    </properties>

    <!--子模块继承之后，提供作用：锁定版本+子module不用写groupId和version-->
    <dependencyManagement>
        <dependencies>
            <!--spring boot 2.2.2-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>2.2.2.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--spring cloud Hoxton.SR1-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Hoxton.SR1</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--spring cloud alibaba 2.1.0.RELEASE-->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>2.2.0.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- MySql -->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
            <!-- Druid -->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid-spring-boot-starter</artifactId>
                <version>${druid.verison}</version>
            </dependency>
            <!-- mybatis-springboot整合 -->
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${mybatis.spring.boot.verison}</version>
            </dependency>
            <!--lombok-->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </dependency>
            <!--junit-->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
            </dependency>
            <!-- log4j -->
            <dependency>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>
                <version>${log4j.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <!--添加devtools热部署插件到父工程-->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <fork>true</fork>
                    <addResources>true</addResources>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

#### Maven工程知识点
##### Maven中的DependencyManagement和 Dependencies

- DependencyManagement 
    Maven 使用dependencyManagement 元素来提供了一种管理依赖版本号的方式。
    通常会在一个组织或者项目的最顶层的父POM 中看到dependencyManagement 元素。

使用pom.xml 中的dependencyManagement 元素能让所有在子项目中引用一个依赖而不用显式的列出版本号。
Maven会沿着父子层次向上走,直到找到一个拥有dependencyManagement元素的项目,然后它就会使用这个
dependencyManagement 元素中指定的版本号。然后在子项目里就可以添加mysql-connector时可以不指定版本号

>这样做的好处就是：如果有多个子项目都引用同一样依赖,则可以避免在每个使用的子项目里都声明一个版本号,
这样当想升级或切换到另一个版本时,只需要在顶层父容器里更新,而不需要一个一个子项目的修改;另外如果某个
子项目需要另外的一个版本,只需要声明version即可.

**dependencyManagement里只是声明依赖,并不实观引入,因此子项目需要显示的声明需要用的依赖。**

**如果不在子项目中声明依赖,是不会从项目中继承下来的;只有在子项目中写了该依赖项,并且没有指定具体版本,才会从父项目中继承该项,并且version和scope都读取自父pom;**

**如果子项目中指定了版本号,那么会使用子项目中指定的jar版本。**


##### Maven中跳过单元测试
![](https://img2020.cnblogs.com/blog/1231979/202004/1231979-20200401143444872-1784555783.png)


#### 父工程创建完成
执行mvn:install将父工程发布到仓库方便子工程继承