package cn.org.opendfl.tasktool.task;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskComputeVo {
    public TaskComputeVo(TaskCompute taskCompute) {
        this.category = taskCompute.category();
        this.dataIdArgCount = taskCompute.dataIdArgCount();
        this.showProcessing = taskCompute.showProcessing();
    }

    private String category;
    private int dataIdArgCount;
    private boolean showProcessing;
}
