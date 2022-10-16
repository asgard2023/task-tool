package cn.org.opendfl.tasktool.task;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author chenjh
 * @TaskCompute注解的方法拦截器
 */
@Component
@Aspect
@Slf4j
public class TaskComputeAspect {

    private String getDataId(ProceedingJoinPoint joinPoint, TaskCompute taskCompute) {
        int argCount = taskCompute.dataIdArgCount();
        if (argCount == -1) {
            return null;
        }

        Object[] args = joinPoint.getArgs();
        int argLen = args.length;
        if (argLen == 0 || argCount >= argLen) {
            return null;
        }

        Object object = args[argCount];
        if (object == null) {
            return null;
        }
        String dataId = null;
        if (object instanceof Date) {
            dataId = "" + ((Date) object).getTime();
        } else {
            dataId = "" + object;
        }
        return dataId;
    }


    @Around(value = "@annotation(taskCompute)")
    public Object doAround(ProceedingJoinPoint joinPoint, TaskCompute taskCompute) throws Throwable {
        HttpServletRequest request = getRequest();

        long curTime = System.currentTimeMillis();
        Date curDate = new Date(curTime);
        String classMethod = getMethodKey(joinPoint, taskCompute);

        String source = "internal";
        if (request != null) {
            source = request.getRequestURI();
        }
        Object result = null;
        String dataId = getDataId(joinPoint, taskCompute);

        try {
            if (taskCompute.showProcessing()) {
                TaskToolUtils.startTask(taskCompute, classMethod, source, dataId, curDate);
            }
            //	执行目标方法
            result = joinPoint.proceed();

            TaskToolUtils.finished(taskCompute, classMethod, source, dataId, curDate);
        } catch (Throwable e) {
            //	异常通知
            TaskToolUtils.error(classMethod, dataId, e.getMessage(), curDate);
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
