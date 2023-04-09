package cn.org.opendfl.tasktool.biz;

import cn.org.opendfl.tasktool.task.annotation.TaskCompute;
import org.springframework.stereotype.Service;

/**
 * 方法调用测试
 */
@Service
public class TaskTestBiz implements ITaskTestBiz {
    @TaskCompute
    @Override
    public String hello(String name) {
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "hello " + name;
    }
}
