package cn.org.opendfl.tasktool.task;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TaskCompute的vo对象
 */
@Data
@NoArgsConstructor
public class TaskComputeVo {
    public TaskComputeVo(TaskCompute taskCompute) {
        this.methodCode = taskCompute.methodCode();
        this.category = taskCompute.category();
        this.dataIdArgCount = taskCompute.dataIdArgCount();
        this.showProcessing = taskCompute.showProcessing();
        this.type = "taskCompute";
    }

    /**
     * 方法编码，方法名，类名加方法名唯一，为空默认为当前方法名
     */
    private String methodCode;
    /**
     * 仅分类，没有其他作用
     */
    private String category;
    /**
     * 数据id的参数序号，-1表示无参
     * 0表示第一个参数，如果无参数的话也兼容
     * 建议放第1个，以免在中间增加参数时序号变了
     */
    private int dataIdArgCount;

    /**
     * 用于显示正在执行的数据
     */
    private boolean showProcessing;


    /**
     * 包名
     */
    private String pkg;
    /**
     * 类型
     */
    private String type;
    /**
     * 来源
     */
    private String source;
    /**
     * 数据dataId，优先dataId，没有取userId，再没有取用户IP
     */
    private String dataId;
    private String userId;
}
