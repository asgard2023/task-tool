package cn.org.opendfl.tasktool.task;


import cn.hutool.core.collection.CollUtil;
import cn.org.opendfl.tasktool.config.vo.ControllerConfigVo;
import cn.org.opendfl.tasktool.config.vo.TaskToolConfig;
import cn.org.opendfl.tasktool.task.annotation.TaskComputeController;
import cn.org.opendfl.tasktool.task.annotation.TaskComputeReq;
import cn.org.opendfl.tasktool.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * controller接口或web请求接口统计处理
 *
 * @author chenjh
 */

public class TaskControllerHandler implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TaskControllerHandler.class);

    private final ThreadLocal<TaskControllerVo> taskComputeVo = new ThreadLocal<>();




    private String getMethodKey(HandlerMethod handler) {
        return handler.getBean().getClass().getSimpleName() + "." + handler.getMethod().getName();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        final String uri = request.getRequestURI();
        if (!skip(handler, uri)) {
            preTaskCompute(request, uri, (HandlerMethod) handler);
        }
        return true;
    }

    private static final AtomicInteger startLogCounter=new AtomicInteger();


    private static final Map<String, TaskComputeVo> methodUriMap=new ConcurrentHashMap<>(100);
    private void preTaskCompute(HttpServletRequest request, final String uri, HandlerMethod handlerMethod) {
        long curTime = System.currentTimeMillis();

        TaskControllerVo taskController = new TaskControllerVo();
        taskController.setStartTime(curTime);
        String packageName = handlerMethod.getBean().getClass().getPackage().getName();
        TaskToolConfig taskToolConfig = TaskToolUtils.getTaskToolConfig();
        if (isContainPackage(packageName)) {
            final String classMethod = getMethodKey(handlerMethod);
            String key= request.getMethod()+"/"+classMethod;
            TaskComputeVo compute = methodUriMap.computeIfAbsent(key, k->{
                TaskComputeVo computeVo = new TaskComputeVo();
                TaskComputeController taskComputeController =  handlerMethod.getMethodAnnotation(TaskComputeController.class);
                TaskComputeReq taskComputeReqVo = new TaskComputeReq();
                computeVo.setMethodCode(classMethod);
                if (taskComputeController != null) {
                    taskComputeReqVo.load(taskComputeController);
                    computeVo.setSourceType(taskComputeController.sourceType());
                }
                else{
                    taskComputeReqVo.setType("controller");
                }
                computeVo.setShowProcessing(true);
                computeVo.setPkg(packageName);
                computeVo.readTaskParam(taskToolConfig, taskComputeReqVo);
                return computeVo;
            });

            String sourceUri = RequestUtils.getUriBySource(request, uri, compute.getSourceType());
            taskController.setSource(sourceUri);
            taskController.setTaskCompute(compute);

            if(request.getDispatcherType() == DispatcherType.ERROR){
                taskController.setDataId(request.getQueryString());
            }
            else{
                taskController.readParam(request);
            }

            taskComputeVo.set(taskController);

            int logCount = startLogCounter.get();
            if(logCount < TaskToolUtils.getTaskToolConfig().getStartLogCount()) {
                logCount = startLogCounter.incrementAndGet();
                logger.debug("---preTaskCompute--packageName={} uri={} classMethod={} startLogCount={}", packageName, uri, classMethod, taskToolConfig.getStartLogCount()-logCount);
            }


            TaskToolUtils.startTask(taskController, classMethod, new Date(curTime));
        }
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TaskControllerVo taskControllerVo = taskComputeVo.get();
        if (taskControllerVo == null) {
            return;
        }
        String classMethod = taskControllerVo.getTaskCompute().getMethodCode();
        Date curDate = new Date(taskControllerVo.getStartTime());
        String dataId = taskControllerVo.getDataId();
        String errMsg=ex!=null?ex.getMessage():null;
        if(errMsg == null){
            //用于异常被@ExceptionHandler吃掉的处理
            errMsg=(String)request.getAttribute(RequestUtils.EXCEPTION_MSG_KEY);
        }
        if (errMsg == null && response.getStatus()== HttpStatus.OK.value()) {
            TaskToolUtils.finished(taskControllerVo, classMethod, curDate);
        } else {
            errMsg=errMsg!=null?errMsg:"";
            errMsg+=",httpStatus:"+response.getStatus();
            TaskToolUtils.error(classMethod, dataId, errMsg, curDate);
        }

        taskComputeVo.remove();
    }


    /**
     * 是否忽略
     *
     * @param handler
     * @param uri uri
     * @return
     */
    private boolean skip(Object handler, String uri) {
        boolean isSkip = false;
        if (!(handler instanceof HandlerMethod)) {
            isSkip = true;
        } else {
            ControllerConfigVo controllerConfigVo = TaskToolUtils.getTaskToolConfig().getControllerConfig();
            final String uriWhiteList = controllerConfigVo.getUriWhitelist();
            //uri接口白名单检查
            if (!"none".equals(uriWhiteList) && uriWhiteList.contains(uri + ",")) {
                isSkip = true;
            }
        }
        return isSkip;
    }


    private boolean isContainPackage(String packageName) {
        boolean isContainPackage;
        List<String> basePackages = TaskToolUtils.getTaskToolConfig().getControllerConfig().getPackages();

        if (CollUtil.isEmpty(basePackages) || PKG_MATCH_ALL.equals(basePackages.get(0))) {
            isContainPackage = true;
        } else {
            isContainPackage = basePackages.stream().anyMatch(packageName::startsWith);
        }

        return isContainPackage;
    }

    /**
     * *号表示全部
     */
    private static final String PKG_MATCH_ALL = "*";

}
