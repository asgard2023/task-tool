# task-tool spring接口调用统计工具

服务内统计接口方法的执行次数，时间，最近错误，最大执行时长等看每个需要监控的方法的执行情况，以便于快速发现性能问题。

## 目的

在系统开发完后，接口的使用情况，是不知道的，没办法直观的知道情况。
通过这个工具来统计，调用次数，异常次数，异常发生时间，异常信息，最大执行时长等，最大执行时长发生成的时间等，来方便查看接口的运行情况。

## 功能特性

* 支持springboot,springmvc
* 统计信息含（调用次数，最近调用时间，最长执行时间（执行时长、发生时间、对应ID）异常错误（次数、最近发生时间，对应ID））
* 支持按分钟，小时、天等维度统计相关信息。
* 支持uri接口的计用次数统计(可以分别计算每个来源的调用次数)
* 支持sping内部调用次数统计，来源默认为空
* 支持scheduler任务的次数统计
* 支持mq的消费次数统计
* 有控制台显示执行信息
* 用hashmap当缓存，性能超好，并且异步进行保存避免影响功能本身的性能。

## Quick Start

http://localhost:8080/index.html  
http://task-tool-demo.opendfl.org.cn/index.html

## 使用

* 1 maven引入依赖包

```xml

<dependency>
    <groupId>cn.org.opendfl</groupId>
    <artifactId>task-tool</artifactId>
    <version>1.1</version>
</dependency>
```

如果需要定时自动保存到数据库可引入下面这个

```xml

<dependency>
    <groupId>cn.org.opendfl</groupId>
    <artifactId>task-tool-db</artifactId>
    <version>1.1</version>
</dependency>
```

* 2,在要统计方法上加上@TaskCompute注解

```java

@Service
public class TaskTestBiz implements ITaskTestBiz {
    @TaskCompute
    @Override
    public String hello(String name) {
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "hello " + name;
    }
}
```

* 3,配置
  * springboot支持任务统计，加scanBasePackages={"cn.org.opendfl.tasktool"}
    ```java
    @SpringBootApplication(scanBasePackages = {"cn.org.opendfl.tasktool"})
    ```
  * springboot支持保存到数据库，加scanBasePackages = {"cn.org.opendfl.task", "cn.org.opendfl.tasktool"}
    ```java
    @SpringBootApplication(scanBasePackages = {"cn.org.opendfl.task", "cn.org.opendfl.tasktool")

* 4,接口有调用后查看结果加scanBasePackages=cn.org.opendfl.tasktool,

http://localhost:8080/taskInfo/runInfo?key=tasktooltest

* 5,查看配置信息

http://localhost:8080/taskInfo/config?key=tasktooltest