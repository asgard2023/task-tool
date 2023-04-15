package cn.org.opendfl.tasktool.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.org.opendfl.tasktool.base.PageVO;
import cn.org.opendfl.tasktool.biz.ITaskHostBiz;
import cn.org.opendfl.tasktool.client.RoutingDelegate;
import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import cn.org.opendfl.tasktool.config.vo.AppInfoVo;
import cn.org.opendfl.tasktool.constant.DateTimeConstant;
import cn.org.opendfl.tasktool.task.RouteApiVo;
import cn.org.opendfl.tasktool.task.TaskCountVo;
import cn.org.opendfl.tasktool.task.TaskToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 信息查询
 *
 * @author chenjh
 */
@RestController
@RequestMapping("taskInfo")
@Slf4j
public class TaskInfoController {

    @Resource
    private TaskToolConfiguration taskToolConfiguration;
    @Resource
    private RoutingDelegate routingDelegate;
    @Resource
    private ITaskHostBiz taskHostBiz;
    private static final long START_TIME = System.currentTimeMillis();


    @RequestMapping(value = "runInfoJson", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getTaskInfoJson(@RequestParam(value = "authKey", required = false) String key,
                                  @RequestParam(value = "taskHostCode", required = false) String taskHostCode, @RequestBody TaskCountVo taskCountVo, PageVO page
            , HttpServletRequest request, HttpServletResponse response) {
        return getTaskInfo(key, taskHostCode, taskCountVo, page, request, response);
    }

    /**
     * 运信信息
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "runInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getTaskInfo(@RequestParam(value = "authKey", required = false) String key,
                              @RequestParam(value = "taskHostCode", required = false) String taskHostCode, TaskCountVo taskCountVo, PageVO page
            , HttpServletRequest request, HttpServletResponse response) {
        if (!taskToolConfiguration.getSecurityKey().equals(key)) {
            log.warn("----runInfo--taskHostCode={} authKey={}", taskHostCode, key);
            return "{\"errorMsg\":\"auth fail\"}";
        }
        String ip = ServletUtil.getClientIP(request);
        log.info("----runInfo--taskHostCode={} ip={}", taskHostCode, ip);
        if (CharSequenceUtil.isNotBlank(taskHostCode)) {
            RouteApiVo routeApiVo = taskHostBiz.getRouteApi(taskHostCode);
            routeApiVo.setIp(ip);
            return routingDelegate.redirect(request, response, "", routeApiVo);
        }
        List<TaskCountVo> list = TaskToolUtils.getTaskCountInfo();
        list = list.stream()
                .filter(t -> CharSequenceUtil.isBlank(taskCountVo.getCountType()) || CharSequenceUtil.equals(taskCountVo.getCountType(), t.getCountType()))
                .filter(t -> CharSequenceUtil.isBlank(taskCountVo.getKey()) || t.getKey().contains(taskCountVo.getKey()))
                .collect(Collectors.toList());


        Comparator orderType = Comparator.naturalOrder();
        if ("desc".equals(page.getOrder())) {
            orderType = Comparator.reverseOrder();
        }
        String sort = page.getSort();
        try {
            if ("key".equals(sort)) {
                list.sort(Comparator.comparing(TaskCountVo::getKey, orderType));
            } else if ("countType".equals(sort)) {
                list.sort(Comparator.comparing(TaskCountVo::getCountType, orderType));
            } else {
                sortList(list, sort, page.getOrder());
            }
        } catch (Exception e) {
            log.warn("----sort={}", sort, e);
        }

        page.setTotalSize(list.size());
        page.setDatas(list);
        return page;
    }

    private void sortList(List<TaskCountVo> list, final String sort, final String order) {
        boolean asc = "asc".equals(order);
        Collections.sort(list, new Comparator<TaskCountVo>() {
            Long getTimeBySort(TaskCountVo taskCountVo) {
                if ("runCounter".equals(sort)) {
                    return taskCountVo.getRunCounter().get() + 0L;
                } else if ("errorCounter".equals(sort)) {
                    return taskCountVo.getErrorCounter().get() + 0L;
                } else if ("first.ts".equals(sort)) {
                    return taskCountVo.getFirst().getTs();
                } else if ("first.runTime".equals(sort)) {
                    return taskCountVo.getFirst().getRunTime();
                } else if ("newly.ts".equals(sort)) {
                    return taskCountVo.getNewly().getTs();
                } else if ("newly.runTime".equals(sort)) {
                    return taskCountVo.getNewly().getRunTime();
                } else if ("max.ts".equals(sort)) {
                    return taskCountVo.getMax().getTs();
                } else if ("max.runTime".equals(sort)) {
                    return taskCountVo.getMax().getRunTime();
                } else if ("error.ts".equals(sort)) {
                    return taskCountVo.getError().getTs();
                } else if ("timeValue".equals(sort)) {
                    return taskCountVo.getTimeValue() + 0L;
                }
                return 0L;
            }

            @Override
            public int compare(TaskCountVo o1, TaskCountVo o2) {
                if (asc) {
                    return getTimeBySort(o1).compareTo(getTimeBySort(o2));
                } else {
                    return getTimeBySort(o2).compareTo(getTimeBySort(o1));
                }
            }
        });
    }

    /**
     * 配置查询
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "config", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getConfig(@RequestParam(value = "authKey", required = false) String key
            , @RequestParam(value = "type", required = false) String type
            , @RequestParam(value = "taskHostCode", required = false) String taskHostCode
            , HttpServletRequest request, HttpServletResponse response) {
        if (!taskToolConfiguration.getSecurityKey().equals(key)) {
            log.warn("----config--type={} authKey={}", type, key);
            return "{\"errorMsg\":\"auth fail\"}";
        }

        if (CharSequenceUtil.isNotBlank(taskHostCode)) {
            String ip = ServletUtil.getClientIP(request);
            RouteApiVo routeApiVo = taskHostBiz.getRouteApi(taskHostCode);
            routeApiVo.setIp(ip);
            return routingDelegate.redirect(request, response, "", routeApiVo);
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

    /**
     * 时间格式对应的timeValue测试
     *
     * @param timeType H,D等
     * @param format   yyyMMddHHmmss的组合
     * @return
     */
    @RequestMapping(value = "timeValue", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getTimeValue(@RequestParam(value = "timeType", required = false) String timeType
            , @RequestParam(value = "format", required = false) String format
            , @RequestParam(value = "taskHostCode", required = false) String taskHostCode
            , HttpServletRequest request, HttpServletResponse response) {
        if (CharSequenceUtil.isNotBlank(taskHostCode)) {
            String ip = ServletUtil.getClientIP(request);
            RouteApiVo routeApiVo = taskHostBiz.getRouteApi(taskHostCode);
            routeApiVo.setIp(ip);
            return routingDelegate.redirect(request, response, "", routeApiVo);
        }
        return DateTimeConstant.getDateInt(new Date(), timeType, format);
    }


}
