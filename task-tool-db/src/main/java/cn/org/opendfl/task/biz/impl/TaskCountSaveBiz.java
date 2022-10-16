package cn.org.opendfl.task.biz.impl;

import cn.org.opendfl.task.biz.ITaDataMethodBiz;
import cn.org.opendfl.task.biz.ITaMethodCountBiz;
import cn.org.opendfl.task.biz.ITaMethodCountSourceBiz;
import cn.org.opendfl.task.po.TaMethodCountPo;
import cn.org.opendfl.tasktool.config.vo.TaskCountTypeVo;
import cn.org.opendfl.tasktool.constant.DateTimeConstant;
import cn.org.opendfl.tasktool.task.TaskCountVo;
import cn.org.opendfl.tasktool.task.TaskToolUtils;
import cn.org.opendfl.tasktool.thread.ITaskCountSaveBiz;
import cn.org.opendfl.tasktool.thread.TaskCountSaveThreadTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 方法调用次数等信息保存到数据库
 *
 * @author chenjh
 */
@Service(value = "taskCountSaveBiz")
@Slf4j
public class TaskCountSaveBiz implements ITaskCountSaveBiz {

    @Resource
    private ITaDataMethodBiz taDataMethodBiz;
    @Resource
    private ITaMethodCountBiz taMethodCountBiz;
    @Resource
    private ITaMethodCountSourceBiz taMethodCountSourceBiz;

    /**
     * 用于通知异步接口当前为该实现类
     */
    @PostConstruct
    public void initTaskCountSave() {
        TaskCountSaveThreadTask.setTaskCountSaveBiz(this);
    }

    private static final Map<String, Integer> codeIdMap = new ConcurrentHashMap<>();

    private static final Map<String, Integer> countIdMap = new ConcurrentHashMap<>();

    private static final Map<Integer, Long> errorTimeMap = new ConcurrentHashMap<>();

    private static final Map<Integer, Long> maxRunTimeMap = new ConcurrentHashMap<>();

    public int cleanExpirekey(List<String> expiredKeys) {
        int removeCount = 0;
        for (String key : expiredKeys) {
            Integer methodCountId = countIdMap.get(key);
            if (methodCountId != null) {
                countIdMap.remove(key);
                errorTimeMap.remove(methodCountId);
                maxRunTimeMap.remove(methodCountId);
                Map<String, Integer> sourceIdMap = methodCountSourceIdMap.remove(methodCountId);
                if (sourceIdMap != null) {
                    sourceIdMap.clear();
                }
                removeCount++;
            }
        }
        log.debug("----cleanExpirekey--expiredKeys={} removeCount={}", expiredKeys.size(), removeCount);
        return removeCount;
    }

    @Override
    public int saveTaskCount(TaskCountTypeVo countType, TaskCountVo taskCountVo) {
        final String methodCode = taskCountVo.getKey();
        Integer dataMethodId = codeIdMap.get(methodCode);
        if (dataMethodId == null) {
            synchronized (methodCode.intern()) {
                dataMethodId = codeIdMap.computeIfAbsent(methodCode, k -> taDataMethodBiz.autoSave(methodCode, taskCountVo));
            }
        }

        Date firstTime = new Date(taskCountVo.getFirstTime());
        Integer timeValue = DateTimeConstant.getDateInt(firstTime, countType.getCode(), null);
        final String countCode = TaskToolUtils.getMethodCountKey(countType, methodCode, timeValue);
        Integer methodCountId = countIdMap.get(countCode);
        final Integer dataMethodIdFinal = dataMethodId;
        if (methodCountId == null) {
            synchronized (countCode.intern()) {
                methodCountId = countIdMap.computeIfAbsent(countCode, k -> taMethodCountBiz.autoSave(countType.getCode(), timeValue, dataMethodIdFinal, countType.getTimeSeconds(), firstTime));
            }
        }
        //更新运行次数
        int v = this.taMethodCountBiz.updateTaskRunCount(methodCountId, taskCountVo, firstTime);

        saveTaskCountNewlyError(taskCountVo, firstTime, methodCountId);

        saveTaskCountMaxRunTime(taskCountVo, firstTime, methodCountId);

        this.saveTaskCountSource(taskCountVo, methodCountId);

        return v;
    }

    private static final Map<Integer, Map<String, Integer>> methodCountSourceIdMap = new ConcurrentHashMap<>();

    private int saveTaskCountSource(final TaskCountVo taskCountVo, final Integer methodCountId) {
        Set<Map.Entry<String, AtomicInteger>> sourceCountSet = taskCountVo.getSourceCounterMap().entrySet();
        Map<String, Integer> sourceIdMap = methodCountSourceIdMap.computeIfAbsent(methodCountId, k -> new ConcurrentHashMap<>());
        int v = 0;
        for (Map.Entry<String, AtomicInteger> entity : sourceCountSet) {
            String source = entity.getKey();
            Integer sourceId = sourceIdMap.get(source);
            if (sourceId == null) {
                synchronized (source.intern()) {
                    sourceId = sourceIdMap.computeIfAbsent(source, k -> this.taMethodCountSourceBiz.autoSave(methodCountId, source));
                }
            }
            int runCount = entity.getValue().get();
            entity.getValue().getAndAdd(-runCount);
            v += this.taMethodCountSourceBiz.updateTaskRunCountSource(sourceId, runCount);
        }
        return v;
    }

    /**
     * 更新最新错误信息，如果不是最新，则不更新
     *
     * @param taskCountVo
     * @param firstTime
     * @param methodCountId
     * @return
     */
    private int saveTaskCountNewlyError(TaskCountVo taskCountVo, Date firstTime, Integer methodCountId) {
        int v = 0;
        //如果有新错误，则更新错误次数，错误信息
        Long errorTimeExist = errorTimeMap.get(methodCountId);
        if (errorTimeExist == null || errorTimeExist < taskCountVo.getErrorNewlyTime()) {
            errorTimeMap.put(methodCountId, taskCountVo.getErrorNewlyTime());
            if (taskCountVo.getErrorNewlyTime() > 0) {
                v = this.taMethodCountBiz.updateTaskErrorInfo(methodCountId, taskCountVo, firstTime);
                //更新失败，取数据库的最新错误时间
                if (v < 1) {
                    TaMethodCountPo exist = this.taMethodCountBiz.getDataByIdByProperties(methodCountId, "id,errorNewlyTime");
                    errorTimeMap.put(methodCountId, exist.getErrorNewlyTime().getTime());
                }
            }
        }
        return v;
    }

    /**
     * 保存最大运行时间，如果不是最大运行时间，则不更新，并同步本地的最大运行时间
     *
     * @param taskCountVo
     * @param firstTime
     * @param methodCountId
     * @return
     */
    private int saveTaskCountMaxRunTime(TaskCountVo taskCountVo, Date firstTime, Integer methodCountId) {
        int v = 0;
        //如果运行时间超过最大值，则更新最大运行时间
        Long maxRunTimeExist = maxRunTimeMap.get(methodCountId);
        if (maxRunTimeExist == null || maxRunTimeExist < taskCountVo.getRunTimeMax()) {
            maxRunTimeMap.put(methodCountId, taskCountVo.getRunTimeMax());
            v = this.taMethodCountBiz.updateTaskMaxRunTime(methodCountId, taskCountVo, firstTime);
            //更新失败，取数据库的最大执行时间
            if (v < 1) {
                TaMethodCountPo exist = this.taMethodCountBiz.getDataByIdByProperties(methodCountId, "id,maxRunTime");
                maxRunTimeMap.put(methodCountId, exist.getMaxRunTime() + 0L);
            }
        }
        return v;
    }


}
