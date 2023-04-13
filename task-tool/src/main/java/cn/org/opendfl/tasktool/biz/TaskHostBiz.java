package cn.org.opendfl.tasktool.biz;

import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.tasktool.base.PageVO;
import cn.org.opendfl.tasktool.task.TaskHostVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskHostBiz implements ITaskHostBiz {
    private static Map<String, TaskHostVo> taskHostMap = new ConcurrentHashMap<>(20);

    @Override
    public void addHost(TaskHostVo taskHost) {
        String code = taskHost.getCode();
        TaskHostVo exist = taskHostMap.computeIfAbsent(code, k -> {
            taskHost.setJoinDate(new Date());
            taskHost.setUpdateDate(new Date());
            return taskHost;
        });
        log.info("---addHost--code={}", taskHost.getCode());
        exist.setHeartbeat(new Date());
        exist.setBuildTime(taskHost.getBuildTime());
    }

    public void save(TaskHostVo taskHost) {
        TaskHostVo taskHostVo = taskHostMap.get(taskHost.getCode());
        taskHostVo.setRemark(taskHost.getRemark());
        taskHostVo.setName(taskHost.getName());
        taskHostVo.setType(taskHost.getType());
        taskHostVo.setIp(taskHost.getIp());
        taskHostVo.setPort(taskHost.getPort());
        if (CharSequenceUtil.isNotBlank(taskHost.getAuthKey())) {
            taskHostVo.setAuthKey(taskHost.getAuthKey());
        }
        taskHostVo.setUpdateDate(new Date());
    }

    public boolean delete(String code) {
        log.info("---delete--code={}", code);
        Object obj = taskHostMap.remove(code);
        return obj != null;
    }

    public TaskHostVo getTaskHost(String code) {
        return taskHostMap.get(code);
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
        List<TaskHostVo> list = taskHostMap.values().stream()
                .map(t-> {
                    TaskHostVo vo = new TaskHostVo();
                    BeanUtils.copyProperties(t, vo);
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
