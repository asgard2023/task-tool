package cn.org.opendfl.tasktool.task;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * controller或者web接口统计参数
 *
 * @author chenjh
 */
@Data
public class TaskControllerVo {
    private long startTime;
    private TaskComputeVo taskCompute;
    private Map<String, String> typeCountCodeMap = new HashMap<>();
}
