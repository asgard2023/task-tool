package cn.org.opendfl.tasktool.task;

import cn.hutool.core.date.DateUtil;
import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import cn.org.opendfl.tasktool.config.vo.TaskCountTypeVo;
import cn.org.opendfl.tasktool.constant.DateTimeConstant;
import cn.org.opendfl.tasktool.thread.ITaskCountSaveBiz;
import cn.org.opendfl.tasktool.thread.TaskCountSaveThreadTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务工具分析
 *
 * @author chenjh
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


    private static final Map<String, TaskCountVo> taskCounterMap = new ConcurrentHashMap<>(20);

    public static void startTask(TaskComputeVo taskCompute, String classMethod, Date curDate) {
        List<TaskCountTypeVo> countTypes = taskToolConfiguration.getCounterTimeTypes();
        for (TaskCountTypeVo countTypeVo : countTypes) {
            startTask(taskCompute, countTypeVo, classMethod, curDate);
        }
    }

    public static TaskCountVo startTask(TaskComputeVo taskCompute, TaskCountTypeVo countTypeVo, final String classMethod, Date curDate) {
        final Integer timeValue = DateTimeConstant.getDateInt(curDate, countTypeVo.getCode(), countTypeVo.getDateFormat());
        final String countCode = getMethodCountKey(countTypeVo, classMethod, timeValue);

        final TaskInfoVo taskInfoVo=new TaskInfoVo();
        taskInfoVo.setTs(curDate.getTime());
        taskInfoVo.setDataId(taskCompute.getDataId());
        taskInfoVo.setUid(taskCompute.getUserId());
        long startTime = curDate.getTime();
        TaskCountVo taskCountVo = taskCounterMap.computeIfAbsent(countCode, k -> {
            TaskCountVo vo = new TaskCountVo();
            vo.setCountType(countTypeVo.getCode());
            vo.setTimeValue(timeValue);
            vo.setTaskCompute(taskCompute);
            vo.setKey(classMethod);
            vo.setFirst(taskInfoVo);
            vo.setMax(taskInfoVo);
            vo.setProcessingData(new ConcurrentHashMap<>(20));
            vo.setRunCounter(new AtomicInteger());
            vo.setErrorCounter(new AtomicInteger());
            vo.setSourceCounterMap(new ConcurrentHashMap<>(10));
            return vo;
        });
        taskCountVo.setNewly(taskInfoVo);
        String source = taskCompute.getSource();
        if(taskToolConfiguration.getControllerConfig().isSource()) {
            AtomicInteger sourceCounter = taskCountVo.getSourceCounterMap().computeIfAbsent(source, k -> new AtomicInteger());
            sourceCounter.incrementAndGet();
        }
        taskCountVo.getRunCounter().incrementAndGet();
        String dataId = taskCompute.getDataId();
        if (dataId != null) {
            taskCountVo.getProcessingData().put(dataId, startTime);
        }
        return taskCountVo;
    }


    public static void finished(TaskComputeVo taskCompute, String classMethod, Date curDate) {
        long startTime = curDate.getTime();
        List<TaskCountTypeVo> countTypes = taskToolConfiguration.getCounterTimeTypes();
        long runTime = System.currentTimeMillis() - startTime;
        for (TaskCountTypeVo countTypeVo : countTypes) {
            finished(taskCompute, countTypeVo, classMethod, curDate, runTime);
        }
        //通知异步线程进行保存
        TaskCountSaveThreadTask.saveTaskCount();
    }

    public static void finished(TaskComputeVo taskCompute, TaskCountTypeVo countTypeVo, String classMethod, final Date curDate, final long runTime) {
        final Integer timeValue = DateTimeConstant.getDateInt(curDate, countTypeVo.getCode(), countTypeVo.getDateFormat());
        final String countCode = getMethodCountKey(countTypeVo, classMethod, timeValue);
        TaskCountVo taskCountVo = taskCounterMap.get(countCode);
        if (taskCountVo == null) {
            taskCountVo = startTask(taskCompute, countTypeVo, classMethod, curDate);
        }
        taskCountVo.getNewly().setRunTime(runTime);
        String dataId = taskCompute.getDataId();
        if (runTime > taskToolConfiguration.getRunTimeBase()) {
            if (runTime > taskCountVo.getMax().getRunTime()) {
                taskCountVo.setMax(taskCountVo.getNewly());
            }
        }
        if (dataId != null) {
            taskCountVo.getProcessingData().remove(dataId);
        }
    }

    public static void error(final String classMethod, final String dataId, final String errorInfo, Date curDate) {
        List<TaskCountTypeVo> countTypes = taskToolConfiguration.getCounterTimeTypes();
        for (TaskCountTypeVo countTypeVo : countTypes) {
            error(countTypeVo, classMethod, dataId, errorInfo, curDate);
        }
    }

    public static void error(final TaskCountTypeVo countTypeVo, final String classMethod, final String dataId, String errorInfo, Date curDate) {
        final Integer timeValue = DateTimeConstant.getDateInt(curDate, countTypeVo.getCode(), countTypeVo.getDateFormat());
        final String countCode = getMethodCountKey(countTypeVo, classMethod, timeValue);
        TaskCountVo taskCountVo = taskCounterMap.get(countCode);
        taskCountVo.setError(taskCountVo.getNewly());
        taskCountVo.getError().setTs(curDate.getTime());
        taskCountVo.getError().setDataId(dataId);
        taskCountVo.getError().setRemark(errorInfo);
        taskCountVo.getErrorCounter().incrementAndGet();
        if (dataId != null) {
            taskCountVo.getProcessingData().remove(dataId);
        }
    }

    /**
     * 用于生成当前时间缓存对应的唯一key
     *
     * @param countTypeVo 统计类型
     * @param classMethod 类名+方法名
     * @param timeValue   类型对应的时间值
     * @return
     */
    public static String getMethodCountKey(TaskCountTypeVo countTypeVo, String classMethod, Integer timeValue) {
        return classMethod + ":" + countTypeVo.getCode() + ":" + timeValue + ":" + countTypeVo.getTimeSeconds();
    }


    public static List<TaskCountVo> getTaskCountInfo() {
        long curTime = System.currentTimeMillis();
        final List<TaskCountTypeVo> countTypes = taskToolConfiguration.getCounterTimeTypes();
        List<TaskCountVo> list = new ArrayList<>(taskCounterMap.size());
        Set<Map.Entry<String, TaskCountVo>> entrySetSet = taskCounterMap.entrySet();
        for (Map.Entry<String, TaskCountVo> entry : entrySetSet) {
            TaskCountVo taskCountVo = entry.getValue().copy();
            Optional<TaskCountTypeVo> countTypeOp = countTypes.stream().filter(t -> t.getCode().equals(taskCountVo.getCountType())).findFirst();
            if (!countTypeOp.isPresent()) {
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

    public static TaskSaveInfoVo saveTaskCounts(ITaskCountSaveBiz taskCountSaveBiz) {
        long curTime = System.currentTimeMillis();
        final List<TaskCountTypeVo> countTypes = taskToolConfiguration.getCounterTimeTypes();
        Set<Map.Entry<String, TaskCountVo>> entrySetSet = taskCounterMap.entrySet();
        List<String> expireKeys = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, TaskCountVo> entry : entrySetSet) {
            TaskCountVo taskCountVo = entry.getValue();
            Optional<TaskCountTypeVo> countTypeOp = countTypes.stream().filter(t -> t.getCode().equals(taskCountVo.getCountType())).findFirst();
            if (!countTypeOp.isPresent()) {
                continue;
            }
            TaskCountTypeVo countTypeVo = countTypeOp.get();
            int periodTime = countTypeVo.getTimeSeconds() * DateTimeConstant.SECOND_MILLIS * (countTypeVo.getHisCount()+1);
            boolean isExpired = countTypeVo.getTimeSeconds() > 0 && curTime - taskCountVo.getFirst().getTs() > periodTime;
            if (isExpired) {
                log.warn("----saveTaskCounts--expire--key={} expired", entry.getKey());
                expireKeys.add(entry.getKey());
                continue;
            }

            int runCount = taskCountVo.getRunCounter().get();
            if (countTypeVo.isSaveDb() && runCount > 0) {
                count++;
                if (taskCountSaveBiz == null) {
                    log.info("----saveTaskCounts--timeType={} code={} runCount={}", countTypeVo.getCode(), entry.getKey(), runCount);
                } else {
                    taskCountSaveBiz.saveTaskCount(countTypeVo, taskCountVo);
                }
            }
        }

        //清理过期的key
        int expireCount = expireKeys.size();
        if (expireCount > 0) {
            for (String key : expireKeys) {
                taskCounterMap.remove(key);
            }
            if (taskCountSaveBiz != null) {
                taskCountSaveBiz.cleanExpirekey(expireKeys);
            }
        }
        long runTime = System.currentTimeMillis() - curTime;
        int logTime = 2000;
        if (runTime > logTime) {
            log.info("----saveTaskCounts--runTime={} expireKeys={}", runTime, expireKeys.size());
        }

        TaskSaveInfoVo saveInfoVo = new TaskSaveInfoVo();
        saveInfoVo.setSaveCount(count);
        saveInfoVo.setExpireCount(expireCount);
        return saveInfoVo;
    }
}
