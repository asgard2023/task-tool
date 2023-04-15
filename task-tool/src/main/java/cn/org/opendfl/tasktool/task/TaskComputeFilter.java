package cn.org.opendfl.tasktool.task;

import cn.org.opendfl.tasktool.task.annotation.TaskComputeServlet;
import cn.org.opendfl.tasktool.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * servlet调用统计
 * @author chenjh
 */
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
@Slf4j
@WebFilter(urlPatterns = "/*")
public class TaskComputeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        HttpServletMapping mapping = req.getHttpServletMapping();
        String className = mapping.getServletName();
        TaskComputeServlet servletCompute = getServletCompute(className);
        if (servletCompute == null) {
            chain.doFilter(request, response);
            return;
        }

        long curTime = System.currentTimeMillis();

        TaskControllerVo taskController = new TaskControllerVo();
        taskController.setStartTime(curTime);

        String pkg = "";
        int idx = className.lastIndexOf('.');
        if (idx > 0) {
            pkg = className.substring(0, idx);
            className = className.substring(idx + 1);
        }
        String classMethod = className + ":" + uri;

        TaskComputeVo computeVo = new TaskComputeVo();
        computeVo.setMethodCode(classMethod);
        computeVo.setShowProcessing(true);
        computeVo.setCategory(null);
        computeVo.setPkg(pkg);
        computeVo.setType("servlet");
        String dataId = RequestUtils.getRequestValue(req, servletCompute.dataIdParamName());
        String userId = RequestUtils.getRequestValue(req, servletCompute.userIdParamName());
        computeVo.setDataId(dataId);
        computeVo.setUserId(userId);
        computeVo.setSource(req.getMethod());
        taskController.setTaskCompute(computeVo);

//        log.debug("---doFilter--uri={} classMethod={}", uri, classMethod);

        TaskToolUtils.startTask(computeVo, classMethod, new Date(curTime));

        chain.doFilter(request, response);
        Date curDate = new Date(curTime);
        TaskToolUtils.finished(computeVo, classMethod, curDate);
    }

    private static Map<String, Class> classMap = new ConcurrentHashMap<>(10);
    private static Map<String, TaskComputeServlet> servletComputeMap = new ConcurrentHashMap<>(10);

    private static TaskComputeServlet getServletCompute(String className) {
        //非servlet接口调用不处理
        if ("dispatcherServlet".equals(className)) {
            return null;
        }
        TaskComputeServlet servletCompute = servletComputeMap.get(className);
        if (servletCompute == null) {
            Class clazz = getClass(className);
            if (clazz.getName().equals(className)) {
                servletCompute = (TaskComputeServlet) clazz.getDeclaredAnnotation(TaskComputeServlet.class);
                if (servletCompute != null) {
                    servletComputeMap.put(className, servletCompute);
                }
            }
        }
        return servletCompute;
    }

    private static Class getClass(String className) {
        Class clazz = classMap.get(className);
        try {
            if (clazz == null) {
                clazz = Class.forName(className);
            }
            //为空用Object对我，有以避免为空
            if (clazz == null) {
                clazz = Object.class;
            }
            classMap.put(className, clazz);
        } catch (ClassNotFoundException e) {
            log.warn("---getClass--className={} error={}", className, e.getMessage());
        }
        return clazz;
    }
}
