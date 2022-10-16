package cn.org.opendfl.task.test.biz;

/**
 * 接口测试
 *
 * @author chenjh
 */
public interface ITaskTestBiz {
    public String hello(String name, int sleepSecond);

    public String helloError(String name, int sleepSecond);

    /**
     * 最后一个参数用于表示来源
     * @param source
     * @return
     */
    public int randomNum(String source);
}
