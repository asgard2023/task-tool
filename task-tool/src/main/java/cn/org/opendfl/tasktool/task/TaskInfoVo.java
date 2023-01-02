package cn.org.opendfl.tasktool.task;

import lombok.Data;

@Data
public class TaskInfoVo {
    private long ts;
    private String dataId;
    private long uid;
    private long runTime;//单ts
    private String remark;//备注，比如异常信息

}
