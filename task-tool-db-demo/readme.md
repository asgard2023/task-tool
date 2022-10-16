# task-tool-db-demo 支持将task-tool的统计数据保存到数据库

## 功能

* task-tool-db的演示demo
* 有一个定时任务调用randomNum接口，随机休眠2-3秒，并返回随机数
* 支持接口调用：
    * hello接口，测试正常接口 http://localhost:8080/taskTest/hello
    * helloError接口，模拟异常 http://localhost:8080/taskTest/helloError
    * randomNum，接口调用randomNum http://localhost:8080/taskTest/randomNum
  
## 示例代码
* TaskTestScheduler.java
* TaskTestController.java