package cn.org.opendfl.tasktool.task;

import cn.org.opendfl.tasktool.task.annotation.TaskCompute;
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
