package cn.org.opendfl.tasktool.task.annotation;


import cn.org.opendfl.tasktool.task.TaskComputeVo;
import cn.org.opendfl.tasktool.task.TaskToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenjh
 * @TaskCompute注解的方法拦截器
 */
@Component
@Aspect
@Slf4j
public class TaskComputeAspect {

    private TaskComputeVo getComputeParam(ProceedingJoinPoint joinPoint, TaskCompute taskCompute) {
        int dataIdCount = taskCompute.dataIdArgCount();
        int userIdCount = taskCompute.userIdArgCount();
        TaskComputeVo taskComputeVo = new TaskComputeVo(taskCompute);
        if (dataIdCount == -1 && userIdCount == -1) {
            return taskComputeVo;
        }

        return taskComputeVo;
    }

    private static Map<String, TaskComputeVo> methodCodeMap=new ConcurrentHashMap<>(100);
    @Around(value = "@annotation(taskCompute)")
    public Object doAround(ProceedingJoinPoint joinPoint, TaskCompute taskCompute) throws Throwable {
        HttpServletRequest request = getRequest();

        long curTime = System.currentTimeMillis();
        Date curDate = new Date(curTime);
        String classMethod = getMethodKey(joinPoint, taskCompute);

        String source = "internal";
        if (request != null) {
            //优先取uri
            source = request.getRequestURI();
        } else {
            //获取最后一个参数作为source
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                Object obj = args[args.length - 1];
                if (obj instanceof String) {
                    source = (String) obj;
                }
            }
        }
        final String sourceFinal = source;
        Object result = null;
        String key = classMethod;
        TaskComputeVo  taskComputeVV = methodCodeMap.computeIfAbsent(key, k->{
            TaskComputeVo taskComputeVo = getComputeParam(joinPoint, taskCompute);
            Object target = joinPoint.getTarget();

            //初始化包名，来源，数据ID
            taskComputeVo.setPkg(target.getClass().getPackage().getName());
            taskComputeVo.setSource(sourceFinal);
            return taskComputeVo;
        });
        try {
            taskComputeVV.readTaskParam(joinPoint, taskCompute);
            if (taskCompute.showProcessing()) {
                TaskToolUtils.startTask(taskComputeVV, classMethod, curDate);
            }
            //	执行目标方法
            result = joinPoint.proceed();

            TaskToolUtils.finished(taskComputeVV, classMethod, curDate);
        } catch (Throwable e) {
            //	异常通知
            TaskToolUtils.error(classMethod, taskComputeVV.getDataId(), e.getMessage(), curDate);
            throw e;
        }

        // 返回结果
        return result;
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }


    private String getMethodKey(ProceedingJoinPoint proceedingJoinPoint, TaskCompute taskCompute) {
        String methodName = taskCompute.methodCode();
        if ("".equals(methodName)) {
            methodName = getMethodName(proceedingJoinPoint);
        }
        Object target = proceedingJoinPoint.getTarget();
        return target.getClass().getSimpleName() + "." + methodName;
    }

    public HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getRequest();
    }
}
