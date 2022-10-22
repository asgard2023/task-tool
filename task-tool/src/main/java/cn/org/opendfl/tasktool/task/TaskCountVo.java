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
        vo.setFirstTime(this.firstTime);
        vo.setTaskCompute(this.taskCompute);
        vo.setCountType(this.countType);
        vo.setLatestTime(this.latestTime);
        vo.setRunTime(this.runTime);
        vo.setDataId(this.dataId);
        vo.setRunCounter(new AtomicInteger(runCounter.get()));

        vo.setRunTimeMax(this.runTimeMax);
        vo.setRunTimeMaxTime(this.runTimeMaxTime);
        vo.setRunTimeMaxDataId(this.runTimeMaxDataId);

        vo.setErrorNewlyDataId(this.errorNewlyDataId);
        vo.setErrorNewlyTime(this.errorNewlyTime);
        vo.setErrorNewlyInfo(this.errorNewlyInfo);
        vo.setErrorCounter(new AtomicInteger(errorCounter.get()));


        vo.setSourceCounterMap(this.sourceCounterMap);
        return vo;
    }

    private String key;
    private String countType;
    /**
     * 调用次数
     */
    private AtomicInteger runCounter;
    /**
     * 最近调用id
     */
    private String dataId;

    /**
     * 最近执行时间
     */
    private long runTime;
    /**
     * 最大执行时间
     */
    private long runTimeMax;
    /**
     * 最大执行时间对应的dataId
     */
    private String runTimeMaxDataId;

    /**
     * 最大执行时间发生的时间
     */
    private long runTimeMaxTime;
    /**
     * 异常错误次数
     */
    private AtomicInteger errorCounter;
    /**
     * 最近异常错误发生的时间
     */
    private long errorNewlyTime;
    /**
     * 最近异常错误的信息
     */
    private String errorNewlyInfo;
    /**
     * 最近异常错误对应的id
     */
    private String errorNewlyDataId;
    /**
     * 最近执行时间
     */
    private long firstTime;
    private long latestTime;
    /**
     * 最近执行时间格式化
     */
    private String latestTimes;


    private TaskComputeVo taskCompute;

    private Map<String, Long> processingData;

    private Map<String, AtomicInteger> sourceCounterMap;
}
