package cn.org.opendfl.tasktool.biz.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.tasktool.base.PageVO;
import cn.org.opendfl.tasktool.biz.ITaskHostBiz;
import cn.org.opendfl.tasktool.task.TaskHostVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskHostRedisBiz implements ITaskHostBiz {

    @Resource(name = "redisTemplateJson")
    private RedisTemplate<String, Object> redisTemplateJson;

    private static final String REDIS_TASK_TOOL_HOSTS = "ta:hosts";

    @Override
    public void addHost(TaskHostVo taskHost) {
        String code = taskHost.getCode();
        boolean isExist = redisTemplateJson.opsForHash().hasKey(REDIS_TASK_TOOL_HOSTS, code);
        if (!isExist) {
            taskHost.setJoinDate(new Date());
            taskHost.setUpdateDate(new Date());
            taskHost.setHeartbeat(new Date());
            redisTemplateJson.opsForHash().put(REDIS_TASK_TOOL_HOSTS, code, taskHost);
        } else {
            TaskHostVo exist = (TaskHostVo) redisTemplateJson.opsForHash().get(REDIS_TASK_TOOL_HOSTS, code);
            exist.setHeartbeat(new Date());
            redisTemplateJson.opsForHash().put(REDIS_TASK_TOOL_HOSTS, code, exist);
        }
        log.info("---addHost--code={}", taskHost.getCode());
    }

    public void save(TaskHostVo taskHost) {
        TaskHostVo taskHostVo = (TaskHostVo) redisTemplateJson.opsForHash().get(REDIS_TASK_TOOL_HOSTS, taskHost.getCode());
        taskHostVo.setRemark(taskHost.getRemark());
        taskHostVo.setName(taskHost.getName());
        taskHostVo.setType(taskHost.getType());
        taskHostVo.setIp(taskHost.getIp());
        taskHostVo.setPort(taskHost.getPort());
        if (CharSequenceUtil.isNotBlank(taskHost.getAuthKey())) {
            taskHostVo.setAuthKey(taskHost.getAuthKey());
        }
        taskHostVo.setUpdateDate(new Date());
        redisTemplateJson.opsForHash().put(REDIS_TASK_TOOL_HOSTS, taskHost.getCode(), taskHost);
    }

    public boolean delete(String code) {
        log.info("---delete--code={}", code);
        Long v = redisTemplateJson.opsForHash().delete(REDIS_TASK_TOOL_HOSTS, code);
        return v != null;
    }

    public TaskHostVo getTaskHost(String code) {
        return (TaskHostVo) redisTemplateJson.opsForHash().get(REDIS_TASK_TOOL_HOSTS, code);
    }

    private void sortList(List<TaskHostVo> list, final String sort, final String order) {
        boolean asc = "asc".equals(order);
        Collections.sort(list, new Comparator<TaskHostVo>() {
            Long getTimeBySort(TaskHostVo taskCountVo) {
                if ("updateTime".equals(sort)) {
                    return taskCountVo.getUpdateDate().getTime() + 0L;
                } else if ("joinDate".equals(sort)) {
                    return taskCountVo.getJoinDate().getTime() + 0L;
                } else if ("heartbeat".equals(sort)) {
                    return taskCountVo.getHeartbeat().getTime();
                }
                return 0L;
            }

            @Override
            public int compare(TaskHostVo o1, TaskHostVo o2) {
                if (asc) {
                    return getTimeBySort(o1).compareTo(getTimeBySort(o2));
                } else {
                    return getTimeBySort(o2).compareTo(getTimeBySort(o1));
                }
            }
        });
    }

    /**
     * @param search    查code,type
     * @param startDate 查heartbeat>startDate
     * @param endDate   查heartbeat<endDate
     * @param page
     * @return
     */
    @Override
    public List<TaskHostVo> getHosts(TaskHostVo search, Date startDate, Date endDate, final PageVO page) {
        Comparator<String> orderType = Comparator.naturalOrder();
        if ("desc".equals(page.getOrder())) {
            orderType = Comparator.reverseOrder();
        }
        String sort = page.getSort();
        List<Object> taskHosts = redisTemplateJson.opsForHash().values(REDIS_TASK_TOOL_HOSTS);
        List<TaskHostVo> list = taskHosts.stream().map(t-> {
                    TaskHostVo vo = new TaskHostVo();
                    BeanUtils.copyProperties(t, vo);
                    vo.setRemark(vo.getType()+"-"+vo.getName());
                    return vo;
                })
                .filter(t -> CharSequenceUtil.isBlank(search.getType()) || CharSequenceUtil.equals(search.getType(), t.getType()))
                .filter(t -> CharSequenceUtil.isBlank(search.getCode()) || t.getCode().contains(search.getCode()))
                .filter(t -> startDate == null || t.getHeartbeat().compareTo(startDate) > 0)
                .filter(t -> endDate == null || endDate.compareTo(t.getHeartbeat()) < 0)
                .collect(Collectors.toList());
        if ("code".equals(sort)) {
            list.sort(Comparator.comparing(TaskHostVo::getCode, orderType));
        } else if ("type".equals(sort)) {
            list.sort(Comparator.comparing(TaskHostVo::getType, orderType));
        } else {
            sortList(list, sort, page.getOrder());
        }
        return list;
    }
}
