package cn.org.opendfl.tasktool.task;

import cn.hutool.core.date.DateUtil;
import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import cn.org.opendfl.tasktool.config.vo.TaskCountTypeVo;
import cn.org.opendfl.tasktool.constant.DateTimeConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务工具分析
 */
@Slf4j
@Component
public class TaskToolUtils {

    private TaskToolUtils() {

    }


    private static TaskToolConfiguration taskToolConfiguration;

    @Resource
    public void setTaskToolConfiguration(TaskToolConfiguration taskToolConfiguration) {
        TaskToolUtils.taskToolConfiguration = taskToolConfiguration;
    }


    private static Map<String, TaskCountVo> taskCounterMap = new ConcurrentHashMap<>(20);

    public static void startTask(TaskCompute taskCompute, String type, String source, String dataId, long startTime) {
        List<TaskCountTypeVo> countTypes = taskToolConfiguration.getCounterTimeTypes();
        for (TaskCountTypeVo countTypeVo : countTypes) {
            startTask(taskCompute, countTypeVo, type, source, dataId, startTime);
        }
    }

    public static TaskCountVo startTask(TaskCompute taskCompute, TaskCountTypeVo countTypeVo, String type, String source, String dataId, long startTime) {
        TaskCountVo taskCountVo = taskCounterMap.computeIfAbsent(countTypeVo.getCode() + ":" + type, k -> {
            TaskCountVo vo = new TaskCountVo();
            vo.setCountType(countTypeVo.getCode());
            vo.setTaskCompute(new TaskComputeVo(taskCompute));
            vo.setFirstTime(startTime);
            vo.setProcessingData(new ConcurrentHashMap<>(20));
            vo.setRunCounter(new AtomicInteger());
            vo.setErrorCounter(new AtomicInteger());
            vo.setSourceCounterMap(new ConcurrentHashMap<>(10));
            return vo;
        });

        int periodTime = countTypeVo.getTimeSeconds() * DateTimeConstant.SECOND_MILLIS;
        boolean isExpired = countTypeVo.getTimeSeconds() != -1 && startTime - taskCountVo.getFirstTime() > periodTime;
        if (isExpired) {
            //重置RunCounter次数为1
            taskCountVo.setFirstTime(startTime);
            int count = taskCountVo.getRunCounter().get();
            taskCountVo.getRunCounter().getAndAdd(-count);

            //重置ErrorCounter次数为0
            count = taskCountVo.getErrorCounter().get();
            if (count > 0) {
                taskCountVo.getErrorCounter().getAndAdd(-count);
            }

            taskCountVo.setRunTimeMax(0);
        }

        AtomicInteger sourceCounter = taskCountVo.getSourceCounterMap().computeIfAbsent(source, k -> new AtomicInteger());
        sourceCounter.incrementAndGet();

        taskCountVo.setDataId(dataId);
        taskCountVo.setLatestTime(startTime);
        taskCountVo.getRunCounter().incrementAndGet();
        if (dataId != null) {
            taskCountVo.getProcessingData().put(dataId, startTime);
        }
        return taskCountVo;
    }

    public static void finished(TaskCompute taskCompute, String type, String source, String dataId, long startTime) {
        List<TaskCountTypeVo> countTypes = taskToolConfiguration.getCounterTimeTypes();
        long runTime = System.currentTimeMillis() - startTime;
        for (TaskCountTypeVo countTypeVo : countTypes) {
            finished(taskCompute, countTypeVo, type, source, dataId, startTime, runTime);
        }
    }

    public static void finished(TaskCompute taskCompute, TaskCountTypeVo countTypeVo, String type, String source, String dataId, final long startTime, final long runTime) {
        TaskCountVo taskCountVo = taskCounterMap.get(countTypeVo.getCode() + ":" + type);
        if (taskCountVo == null) {
            taskCountVo = startTask(taskCompute, countTypeVo, type, source, dataId, startTime);
        }
        taskCountVo.setRunTime(runTime);
        if (runTime > taskToolConfiguration.getRunTimeBase()) {
            if (runTime > taskCountVo.getRunTimeMax()) {
                taskCountVo.setRunTimeMax(runTime);
                taskCountVo.setRunTimeMaxDataId(dataId);
                taskCountVo.setRunTimeMaxTime(startTime);
            }
        }
        if (dataId != null) {
            taskCountVo.getProcessingData().remove(dataId);
        }
    }

    public static void error(final String type, final String dataId, final String errorInfo, long startTime) {
        List<TaskCountTypeVo> countTypes = taskToolConfiguration.getCounterTimeTypes();
        for (TaskCountTypeVo countTypeVo : countTypes) {
            error(countTypeVo, type, dataId, errorInfo, startTime);
        }
    }

    public static void error(final TaskCountTypeVo countTypeVo, final String type, final String dataId, String errorInfo, long startTime) {
        TaskCountVo taskCountVo = taskCounterMap.get(countTypeVo.getCode() + ":" + type);
        taskCountVo.setErrorNewlyTime(startTime);
        taskCountVo.setErrorNewlyInfo(errorInfo);
        if (dataId != null) {
            taskCountVo.setErrorNewlyDataId(dataId);
        }
        taskCountVo.getErrorCounter().incrementAndGet();
        if (dataId != null) {
            taskCountVo.getProcessingData().remove(dataId);
        }
    }


    public static List<TaskCountVo> getTaskCountInfo() {
        long curTime = System.currentTimeMillis();
        final List<TaskCountTypeVo> countTypes = taskToolConfiguration.getCounterTimeTypes();
        List<TaskCountVo> list = new ArrayList<>(taskCounterMap.size());
        Set<Map.Entry<String, TaskCountVo>> entrySetSet = taskCounterMap.entrySet();
        for (Map.Entry<String, TaskCountVo> entry : entrySetSet) {
            TaskCountVo taskCountVo = entry.getValue().copy();
            taskCountVo.setKey(entry.getKey());
            taskCountVo.setLatestTimes(DateUtil.format(new Date(taskCountVo.getLatestTime()), "yyyy-MM-dd HH:mm:ss"));
            Optional<TaskCountTypeVo> countTypeOp = countTypes.stream().filter(t -> t.getCode().equals(taskCountVo.getCountType())).findFirst();
            if (!countTypeOp.isPresent()) {
                continue;
            }
            TaskCountTypeVo countTypeVo = countTypeOp.get();
            int periodTime = countTypeVo.getTimeSeconds() * DateTimeConstant.SECOND_MILLIS;
            boolean isExpired = countTypeVo.getTimeSeconds() != -1 && curTime - taskCountVo.getFirstTime() > periodTime;
            if (isExpired) {
                continue;
            }

            Map<String, Long> processingDataMap = entry.getValue().getProcessingData();
            Set<Map.Entry<String, Long>> msgKeySet = processingDataMap.entrySet();
            Map<String, Long> pendingTimeMap = new HashMap<>(processingDataMap.size());
            for (Map.Entry<String, Long> msgKey : msgKeySet) {
                pendingTimeMap.put(msgKey.getKey(), curTime - msgKey.getValue());
            }
            taskCountVo.setProcessingData(pendingTimeMap);
            list.add(taskCountVo);
        }
        list.sort(Comparator.comparing(taskCountVo -> taskCountVo.getCountType() + ":" + taskCountVo.getKey()));
        return list;
    }
}
