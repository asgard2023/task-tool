package cn.org.opendfl.tasktool.task;

import cn.org.opendfl.tasktool.task.annotation.TaskComputeReq;
import cn.org.opendfl.tasktool.task.annotation.TaskComputeServlet;
import cn.org.opendfl.tasktool.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

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
@Slf4j
@WebFilter(urlPatterns = "/*")
public class TaskComputeFilter implements Filter {

    private static AtomicInteger startLogCounter=new AtomicInteger();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        HttpServletMapping mapping = req.getHttpServletMapping();
        String className = mapping.getServletName();

        //非servlet接口调用不处理
        if ("dispatcherServlet".equals(className)) {
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

        TaskComputeVo computeVo = getServletCompute(className, uri, pkg);
        String sourceUri = RequestUtils.getUriBySource(req, uri, computeVo.getSourceType());
        taskController.setSource(sourceUri);
        taskController.setTaskCompute(computeVo);
        taskController.readParam(req);

        int logCount = startLogCounter.get();
        if(logCount < TaskToolUtils.getTaskToolConfig().getStartLogCount()) {
            logCount = startLogCounter.incrementAndGet();
            log.debug("---doFilter--uri={} classMethod={} startLogCount={}", uri, classMethod, TaskToolUtils.getTaskToolConfig().getStartLogCount()-logCount);
        }

        TaskToolUtils.startTask(taskController, classMethod, new Date(curTime));

        chain.doFilter(request, response);
        Date curDate = new Date(curTime);
        TaskToolUtils.finished(taskController, classMethod, curDate);
    }

    private static Map<String, Class<?>> classMap = new ConcurrentHashMap<>(10);
    private static Map<String, TaskComputeVo> servletComputeMap = new ConcurrentHashMap<>(10);

    private  TaskComputeVo getServletCompute(final String className, final String uri, final String pkg) {
        String key = className+":"+uri;
        return servletComputeMap.computeIfAbsent(key, k->{
            TaskComputeReq taskComputeReqVo = new TaskComputeReq();
            taskComputeReqVo.setType("servlet");
            Class<?> clazz = getClass(pkg+"."+className);
            TaskComputeVo computeVo = new TaskComputeVo();
            TaskComputeServlet servletCompute = clazz.getDeclaredAnnotation(TaskComputeServlet.class);
            if (servletCompute != null) {
                taskComputeReqVo.load(servletCompute, uri);
                computeVo.setSourceType(servletCompute.sourceType());
            }
            computeVo.setMethodCode(key);
            computeVo.setShowProcessing(true);
            computeVo.readTaskParam(TaskToolUtils.getTaskToolConfig(), taskComputeReqVo);
            computeVo.setPkg(pkg);
            return computeVo;
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
