package cn.org.opendfl.tasktool.task;


import lombok.Data;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 接口任务统计vo
 *
 * @author chenjh
 */
@Data
public class TaskCountVo {
    public TaskCountVo copy() {
        TaskCountVo vo = new TaskCountVo();
        vo.setKey(this.key);
        vo.setFirst(this.first);
        vo.setNewly(this.newly);
        vo.setMax(this.max);
        vo.setError(this.error);
        vo.setTaskCompute(this.taskCompute);
        vo.setCountType(this.countType);
        vo.setTimeValue(this.timeValue);
        vo.setRunCounter(new AtomicInteger(runCounter.get()));
        vo.setErrorCounter(new AtomicInteger(errorCounter.get()));
        vo.setSourceCounterMap(this.sourceCounterMap);
        return vo;
    }

    private String key;
    private String countType;
    private Integer timeValue;

    private TaskComputeVo taskCompute;
    /**
     * 调用次数
     */
    private AtomicInteger runCounter;
    /**
     * 异常错误次数
     */
    private AtomicInteger errorCounter;
    /**
     * 首次调用
     */
    private TaskInfoVo first;
    /**
     * 最新
     */
    private TaskInfoVo newly;
    /**
     * 最大时长
     */
    private TaskInfoVo max;
    /**
     * 最近错误
     */
    private TaskInfoVo error;


    private Map<String, Long> processingData;
    private Map<String, AtomicInteger> sourceCounterMap;
}
