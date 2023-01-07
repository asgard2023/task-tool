package cn.org.opendfl.tasktool.task;


import cn.hutool.core.collection.CollUtil;
import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import cn.org.opendfl.tasktool.config.vo.ControllerConfigVo;
import cn.org.opendfl.tasktool.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * controller接口或web请求接口统计处理
 *
 * @author chenjh
 */
@Service
public class TaskControllerHandler implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TaskControllerHandler.class);

    private final ThreadLocal<TaskControllerVo> taskComputeVo = new ThreadLocal<>();

    @Resource
    private TaskToolConfiguration taskToolConfiguration;


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




    private void preTaskCompute(HttpServletRequest request, final String uri, HandlerMethod handlerMethod) {
        long curTime = System.currentTimeMillis();
        TaskControllerVo taskController = new TaskControllerVo();
        taskController.setStartTime(curTime);
        String packageName = handlerMethod.getBean().getClass().getPackage().getName();
        if (isContainPackage(packageName)) {
            String classMethod = getMethodKey(handlerMethod);
            TaskComputeVo computeVo = new TaskComputeVo();
            computeVo.setMethodCode(classMethod);
            computeVo.setShowProcessing(true);
            computeVo.setCategory(null);

            computeVo.setType("controller");
            computeVo.setPkg(packageName);
            computeVo.setDataId(RequestUtils.getDataId(taskToolConfiguration, request));
            computeVo.setUserId(RequestUtils.getUserId(taskToolConfiguration, request));
            computeVo.setSource(uri);

            taskController.setTaskCompute(computeVo);
            taskComputeVo.set(taskController);

            logger.debug("---preHandle--packageName={} uri={} classMethod={}", packageName, uri, classMethod);

            TaskToolUtils.startTask(computeVo, classMethod, new Date(curTime));
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
        String dataId = taskControllerVo.getTaskCompute().getDataId();
        String errMsg=ex!=null?ex.getMessage():null;
        if(errMsg == null){
            //用于异常被@ExceptionHandler吃掉的处理
            errMsg=(String)request.getAttribute(RequestUtils.EXCEPTION_MSG_KEY);
        }
        if (errMsg == null && response.getStatus()== HttpStatus.OK.value()) {
            TaskToolUtils.finished(taskControllerVo.getTaskCompute(), classMethod, curDate);
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
     * @param uri
     * @return
     */
    private boolean skip(Object handler, String uri) {
        boolean isSkip = false;
        if (!(handler instanceof HandlerMethod)) {
            isSkip = true;
        } else {
            ControllerConfigVo controllerConfigVo = taskToolConfiguration.getControllerConfig();
            final String uriWhiteList = controllerConfigVo.getUriWhitelist();
            //uri接口白名单检查
            if (!"none".equals(uriWhiteList) && uriWhiteList.contains(uri + ",")) {
                isSkip = true;
            }
        }
        return isSkip;
    }


    private boolean isContainPackage(String packageName) {
        boolean isContainPackage = false;
        List<String> basePackages = taskToolConfiguration.getControllerConfig().getPackages();

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
