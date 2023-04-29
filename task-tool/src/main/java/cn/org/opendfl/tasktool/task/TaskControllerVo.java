package cn.org.opendfl.tasktool.task;

import cn.org.opendfl.tasktool.task.annotation.TaskCompute;
import cn.org.opendfl.tasktool.utils.RequestUtils;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * controller或者web接口统计参数
 *
 * @author chenjh
 */
@Data
public class TaskControllerVo {
    private long startTime;
    private TaskComputeVo taskCompute;
        /**
     * 数据dataId，优先dataId，没有取userId，再没有取用户IP
     */
    private String dataId;
    private String userId;

    public void readTaskParam(ProceedingJoinPoint joinPoint, TaskCompute taskCompute){
        Object[] args = joinPoint.getArgs();
        int argLen = args.length;
        //取dataId参数
        Object dataId = getArgs(args, argLen, taskCompute.dataIdArgCount());
        //取userId参数
        Object userId = getArgs(args, argLen, taskCompute.userIdArgCount());

        this.setDataId(getDataId(dataId));
        if(userId!=null) {
            this.setUserId("" + userId);
        }
    }

    public void readParam(HttpServletRequest request){
        this.setDataId(RequestUtils.getRequestValue(request, getTaskCompute().getDataIdArg()));
        this.setUserId(RequestUtils.getRequestValue(request, getTaskCompute().getUserIdArg()));
    }

    /**
     * id转字符串
     * @param dataId
     * @return
     */
    private String getDataId(Object dataId) {
        String dataIdstr = null;
        if (dataId == null) {
            return null;
        }
        if (dataId instanceof Date) {
            dataIdstr = "" + ((Date) dataId).getTime();
        } else {
            dataIdstr = "" + dataId;
        }
        return dataIdstr;
    }

    /**
     *
     * @param args 参数数组
     * @param argLen 参数个数
     * @param idx 参数下标
     * @return
     */
    private Object getArgs(Object[] args, int argLen, int idx) {
        //idx=-1个表示无参，idx>=argLen表示配置错误，实际参数低于参数个数
        if (argLen == 0 || idx<0 || idx >= argLen) {
            return null;
        }
        return args[idx];
    }
}
