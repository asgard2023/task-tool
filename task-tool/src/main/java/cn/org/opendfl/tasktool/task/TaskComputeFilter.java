package cn.org.opendfl.tasktool.task;

import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import cn.org.opendfl.tasktool.task.annotation.TaskComputeReq;
import cn.org.opendfl.tasktool.task.annotation.TaskComputeServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * servlet调用统计
 * @author chenjh
 */
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
@Slf4j
@WebFilter(urlPatterns = "/*")
public class TaskComputeFilter implements Filter {
    @Resource
    private TaskToolConfiguration taskToolConfiguration;

    private static AtomicInteger startLogCounter=new AtomicInteger();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        HttpServletMapping mapping = req.getHttpServletMapping();
        String className = mapping.getServletName();
        TaskComputeReq taskComputeReq = getServletCompute(className, uri);
        if (taskComputeReq==null || taskComputeReq.getType()==null) {
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
        computeVo.setPkg(pkg);
        computeVo.readTaskParam(taskToolConfiguration, req, taskComputeReq);
        computeVo.setSource(req.getMethod());
        taskController.setTaskCompute(computeVo);

        int logCount = startLogCounter.get();
        if(logCount < TaskToolUtils.START_LOG_COUNT) {
            logCount = startLogCounter.incrementAndGet();
            log.debug("---doFilter--uri={} classMethod={} startLogCount={}", uri, classMethod, TaskToolUtils.START_LOG_COUNT-logCount);
        }

        TaskToolUtils.startTask(computeVo, classMethod, new Date(curTime));

        chain.doFilter(request, response);
        Date curDate = new Date(curTime);
        TaskToolUtils.finished(computeVo, classMethod, curDate);
    }

    private static Map<String, Class<?>> classMap = new ConcurrentHashMap<>(10);
    private static Map<String, TaskComputeReq> servletComputeMap = new ConcurrentHashMap<>(10);

    private static TaskComputeReq getServletCompute(String className, String uri) {
        //非servlet接口调用不处理
        if ("dispatcherServlet".equals(className)) {
            return null;
        }
        String key = className+":"+uri;
        return servletComputeMap.computeIfAbsent(key, k->{
            TaskComputeReq taskComputeReqVo = new TaskComputeReq();
            taskComputeReqVo.setType("servlet");
            Class<?> clazz = getClass(className);
            if (clazz.getName().equals(className)) {
                TaskComputeServlet servletCompute = clazz.getDeclaredAnnotation(TaskComputeServlet.class);
                if (servletCompute != null) {
                    taskComputeReqVo.load(servletCompute, uri);
                }
            }
            return taskComputeReqVo;
        });
    }

    private static Class<?> getClass(String className) {
        Class<?> clazz = classMap.get(className);
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
