package cn.org.opendfl.tasktooldemo.biz;

/**
 * 接口测试
 *
 * @author chenjh
 */
public interface ITaskTestBiz {
    public String hello(String name, int sleepSecond);

    public String helloError(String name, int sleepSecond);

    public int randomNum();
}
