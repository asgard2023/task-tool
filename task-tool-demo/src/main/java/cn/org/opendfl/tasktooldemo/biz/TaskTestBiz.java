package cn.org.opendfl.tasktooldemo.biz;

import cn.org.opendfl.tasktool.constant.DateTimeConstant;
import cn.org.opendfl.tasktool.task.TaskCompute;
import org.springframework.stereotype.Service;

@Service
public class TaskTestBiz implements ITaskTestBiz {
    @TaskCompute
    @Override
    public String hello(String name, int sleepSecond) {
        try {
            Thread.sleep(sleepSecond * DateTimeConstant.SECOND_MILLIS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "hello " + name;
    }

    @TaskCompute
    @Override
    public String helloError(String name, int sleepSecond) {
        int v = 1 / 0;
        return "hello " + name;
    }
}
