package cn.org.opendfl.tasktool.config.vo;

import cn.org.opendfl.tasktool.task.TaskHostVo;
import lombok.Data;

@Data
public class TaskToolVo {
    private boolean open;
    private String apiUrl;
    private String authKey;
    private TaskLocalVo taskLocal;
}
