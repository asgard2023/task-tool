package cn.org.opendfl.tasktool.controller;

import cn.org.opendfl.tasktool.base.PageVO;
import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import cn.org.opendfl.tasktool.config.vo.AppInfoVo;
import cn.org.opendfl.tasktool.task.TaskCountVo;
import cn.org.opendfl.tasktool.task.TaskToolUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 信息查询
 *
 * @author chenjh
 */
@RestController
@RequestMapping("taskInfo")
public class TaskInfoController {

    @Resource
    private TaskToolConfiguration taskToolConfiguration;
    private static final long START_TIME = System.currentTimeMillis();

    /**
     * 运信信息
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "runInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getTaskInfo(@RequestParam(value = "authKey", required = false) String key, TaskCountVo taskCountVo, PageVO page) {
        if (!taskToolConfiguration.getSecurityKey().equals(key)) {
            return "{\"auth\":\"fail\"}";
        }
        List<TaskCountVo> list = TaskToolUtils.getTaskCountInfo();
        list = list.stream().filter(t ->
                StringUtils.isBlank(taskCountVo.getCountType()) || StringUtils.equals(taskCountVo.getCountType(), t.getCountType())
        ).filter(t ->
                StringUtils.isBlank(taskCountVo.getKey()) || t.getKey().contains(taskCountVo.getKey())
        ).collect(Collectors.toList());


        Comparator orderType = Comparator.naturalOrder();
        if ("desc".equals(page.getOrder())) {
            orderType = Comparator.reverseOrder();
        }
        String sort = page.getSort();
        if ("key".equals(sort)) {
            list.sort(Comparator.comparing(TaskCountVo::getKey, orderType));
        } else if ("countType".equals(sort)) {
            list.sort(Comparator.comparing(TaskCountVo::getCountType, orderType));
        } else if ("firstTime".equals(sort)) {
            list.sort(Comparator.comparing(TaskCountVo::getFirstTime, orderType));
        } else if ("latestTime".equals(sort)) {
            list.sort(Comparator.comparing(TaskCountVo::getLatestTime, orderType));
        } else if ("runTime".equals(sort)) {
            list.sort(Comparator.comparing(TaskCountVo::getRunTime, orderType));
        } else if ("runTimeMax".equals(sort)) {
            list.sort(Comparator.comparing(TaskCountVo::getRunTimeMax, orderType));
        } else if ("runTimeMaxTime".equals(sort)) {
            list.sort(Comparator.comparing(TaskCountVo::getRunTimeMaxTime, orderType));
        } else if ("errorNewlyTime".equals(sort)) {
            list.sort(Comparator.comparing(TaskCountVo::getErrorNewlyTime, orderType));
        }

        page.setTotalSize(list.size());
        page.setDatas(list);
        return page;
    }

    /**
     * 配置查询
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "config", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getConfig(@RequestParam(value = "authKey", required = false) String key
            , @RequestParam(value = "type", required = false) String type) {
        if (!taskToolConfiguration.getSecurityKey().equals(key)) {
            return "{\"auth\":\"fail\"}";
        }

        if ("timeTypes".equals(type)) {
            return taskToolConfiguration.getCounterTimeTypes();
        }

        long currentTime = System.currentTimeMillis();
        AppInfoVo appInfoVo = new AppInfoVo();
        appInfoVo.setSystemTime(currentTime);
        appInfoVo.setStartTime(START_TIME);
        appInfoVo.setVersion(taskToolConfiguration.getVersion());
        appInfoVo.setCounterTimeTypes(taskToolConfiguration.getCounterTimeTypes());
        appInfoVo.setRunTimeBase(taskToolConfiguration.getRunTimeBase());
        return appInfoVo;

    }


}
