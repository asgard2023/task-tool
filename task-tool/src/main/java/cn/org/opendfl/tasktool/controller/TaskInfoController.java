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
import cn.org.opendfl.tasktool.utils.RequestParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public void setTaskHostBiz(ITaskHostBiz taskHostBiz) {
        this.taskHostBiz = taskHostBiz;
    }

    private static final long START_TIME = System.currentTimeMillis();


    @RequestMapping(value = "runInfoJson", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getTaskInfoJson(@RequestParam(value = RequestParams.AUTH_KEY, required = false) String key,
                                  @RequestParam(value = RequestParams.TASK_HOST_CODE, required = false) String taskHostCode, @RequestBody TaskCountVo taskCountVo
            , HttpServletRequest request, HttpServletResponse response) {
        return getTaskInfo(key, taskHostCode, taskCountVo, request, response);
    }

    /**
     * 运信信息
     *
     * @param authKey
     * @return
     */
    @RequestMapping(value = "runInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getTaskInfo(@RequestParam(value = RequestParams.AUTH_KEY, required = false) String authKey,
                              @RequestParam(value = RequestParams.TASK_HOST_CODE, required = false) String taskHostCode, TaskCountVo taskCountVo
            , HttpServletRequest request, HttpServletResponse response) {
        if (!taskToolConfiguration.isAuth(authKey, request)) {
            log.warn("----runInfo--taskHostCode={} authKey={}", taskHostCode, authKey);
            return "{\"errorMsg\":\"auth fail\"}";
        }
        PageVO<TaskCountVo> page = new PageVO<>(request);
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


        sortList(page, list);

        page.loadCurrentPage(list);
        return page;
    }

    private void sortList(PageVO<TaskCountVo> page, List<TaskCountVo> list) {
        String sort = page.getSort();
        try {
            if(CharSequenceUtil.isNotBlank(sort)){
                Comparator<TaskCountVo> comparator = null;
                if(sort.startsWith("taskCompute.")){
                    comparator = getSortCompute(sort);
                }
                else{
                    comparator = getSort(sort);
                }
                if(comparator!=null){
                    boolean asc = "asc".equals(page.getOrder());
                    if(asc){
                        list.sort(comparator);
                    }
                    else{
                        list.sort(comparator.reversed());
                    }
                }
                else{
                    log.warn("----sortList--sort={} invalid", sort);
                }
            }
        } catch (Exception e) {
            log.warn("----sortList--sort={}", sort, e);
        }
    }


    private Comparator<TaskCountVo> getSortCompute(final String sort) {
        Comparator<TaskCountVo> computeCompare = null;
        switch (sort){
            case "taskCompute.pkg":
                computeCompare = Comparator.comparing(temp -> temp.getTaskCompute().getPkg());
                break;
            case "taskCompute.category":
                computeCompare = Comparator.comparing(temp -> temp.getTaskCompute().getCategory());
                break;
            case "taskCompute.methodCode":
                computeCompare = Comparator.comparing(temp -> temp.getTaskCompute().getMethodCode());
                break;
            case "taskCompute.type":
                computeCompare = Comparator.comparing(temp -> temp.getTaskCompute().getType());
                break;
            case "taskCompute.dataIdArg":
                computeCompare = Comparator.comparing(temp -> temp.getTaskCompute().getDataIdArg());
                break;
            case "taskCompute.userIdArg":
                computeCompare = Comparator.comparing(temp -> temp.getTaskCompute().getUserIdArg());
                break;
            case "taskCompute.source":
                computeCompare = Comparator.comparing(temp -> temp.getTaskCompute().getSource());
                break;
            default:
                log.warn("----getSortCompute--sort={} invalid", sort);
        }
        return computeCompare;
    }
    private Comparator<TaskCountVo> getSort(final String sort) {
        Comparator<TaskCountVo> taskCountCompare = null;
        switch (sort){
            case "runCounter":
                taskCountCompare = Comparator.comparing(temp-> temp.getRunCounter().get());
                break;
            case "errCounter":
                taskCountCompare = Comparator.comparing(temp-> temp.getErrorCounter().get());
                break;
            case "timeValue":
                taskCountCompare = Comparator.comparing(TaskCountVo::getTimeValue);
                break;
            case "key":
                taskCountCompare = Comparator.comparing(TaskCountVo::getKey);
                break;
            case "countType":
                taskCountCompare = Comparator.comparing(TaskCountVo::getCountType);
                break;
            case "first.ts":
                taskCountCompare = Comparator.comparing(temp-> temp.getFirst().getTs());
                break;
            case "first.runTime":
                taskCountCompare = Comparator.comparing(temp-> temp.getFirst().getRunTime());
                break;
            case "newly.ts":
                taskCountCompare = Comparator.comparing(temp-> temp.getNewly().getTs());
                break;
            case "newly.runTime":
                taskCountCompare = Comparator.comparing(temp-> temp.getNewly().getRunTime());
                break;
            case "max.ts":
                taskCountCompare = Comparator.comparing(temp-> temp.getMax().getTs());
                break;
            case "max.runTime":
                taskCountCompare = Comparator.comparing(temp-> temp.getMax().getRunTime());
                break;
            default:
                log.warn("----sort={} invalid", sort);
        }
        return taskCountCompare;
    }

    /**
     * 配置查询
     *
     * @param authKey
     * @return
     */
    @RequestMapping(value = "config", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getConfig(@RequestParam(value = RequestParams.AUTH_KEY, required = false) String authKey
            , @RequestParam(value = "type", required = false) String type
            , @RequestParam(value = "taskHostCode", required = false) String taskHostCode
            , HttpServletRequest request, HttpServletResponse response) {
        if (!taskToolConfiguration.isAuth(authKey, request)) {
            log.warn("----config--type={} authKey={}", type, authKey);
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
