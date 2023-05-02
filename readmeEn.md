# task-tool spring interface calls statistics tool

Count the execution times, time, recent errors, and maximum execution time of interface methods in the service to see the execution of each method that needs to be monitored, so as to quickly find performance problems.

## Features

* Support springboot, springmvc
* The statistical information includes (the number of calls, the most recent call time, the longest execution time (execution time, occurrence time, corresponding ID) abnormal error (the number of times, the most recent occurrence time, the corresponding ID))
* Support statistics related information by minutes, hours, days and other dimensions.
* Support the statistics of the number of times of usage of the uri interface (the number of calls of each source can be calculated separately)
* Support the statistics of the number of internal calls of sping, the source is empty by default
* Support the number of scheduler tasks statistics
* Support consumption statistics of mq
* Support to check the execution information of each interface

## Quick Start

http://localhost:8080/index.html  
http://task-tool-demo.opendfl.org.cn/index.html

## Use

* 1,import maven dependency packages

```xml

<dependency>
    <groupId>cn.org.opendfl</groupId>
    <artifactId>task-tool</artifactId>
    <version>2.0</version>
</dependency>
```

* 2,Add the @TaskCompute annotation to the method to be counted

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

* 3,View the result after the interface is called

http://localhost:8080/taskInfo/runInfo?key=tasktooltest  
http://localhost:8080/pages/runInfo.html

* 4,View configuration information

http://localhost:8080/taskInfo/config?key=tasktooltest