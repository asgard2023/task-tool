package cn.org.opendfl.tasktooldemo.biz;

import cn.org.opendfl.tasktool.constant.DateTimeConstant;
import cn.org.opendfl.tasktool.task.TaskCompute;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 接口测试
 *
 * @author chenjh
 */
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

    private static Random random = new Random();

    /**
     * 返回100以内的数，并休眠1-3s
     * @return
     */
    @TaskCompute
    public int randomNum() {
        int rnd = random.nextInt(100);
        try {
            Thread.sleep((rnd % 3 + 1) * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return rnd;
    }
}
