package cn.org.opendfl.tasktool.task.annotation;

import java.lang.annotation.*;

/**
 * 任务计算注解，加了此注解可收集调用量，近期错误等信息
 *
 * @author chenjh
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TaskCompute {
    /**
     * 方法编码，方法名，类名加方法名唯一，为空默认为当前方法名
     *
     * @return
     */
    String methodCode() default "";

    /**
     * 用户id的参数序号，-1表示无参
     * 0表示第一个参数，如果无参数的话也兼容
     * 建议放第1个，以免在中间增加参数时序号变了
     *
     * @return
     */
    int userIdArgCount() default -1;
    /**
     * 数据id的参数序号，-1表示无参
     * 0表示第一个参数，如果无参数的话也兼容
     * 建议放第1个，以免在中间增加参数时序号变了
     *
     * @return
     */
    int dataIdArgCount() default 0;

    /**
     * 用于显示正在执行的数据
     *
     * @return
     */
    boolean showProcessing() default true;

    /**
     * 仅分类，没有其他作用
     *
     * @return
     */
    String category() default "";
}
