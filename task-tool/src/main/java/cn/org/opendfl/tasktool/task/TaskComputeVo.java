package cn.org.opendfl.tasktool.task;

import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import cn.org.opendfl.tasktool.task.annotation.TaskCompute;
import cn.org.opendfl.tasktool.task.annotation.TaskComputeReq;
import cn.org.opendfl.tasktool.utils.RequestUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * TaskCompute的vo对象
 */
@Data
@NoArgsConstructor
public class TaskComputeVo {
    public TaskComputeVo(TaskCompute taskCompute) {
        this.methodCode = taskCompute.methodCode();
        this.category = taskCompute.category();
        this.showProcessing = taskCompute.showProcessing();
        this.dataIdArg = ""+taskCompute.dataIdArgCount();
        this.userIdArg = ""+taskCompute.userIdArgCount();
        this.type = "taskCompute";
    }

    /**
     * 方法编码，方法名，类名加方法名唯一，为空默认为当前方法名
     */
    private String methodCode;
    /**
     * 仅分类，没有其他作用
     */
    private String category;
    /**
     * 数据id的参数序号，-1表示无参
     * 0表示第一个参数，如果无参数的话也兼容
     * 建议放第1个，以免在中间增加参数时序号变了
     */
    private String dataIdArg;
    private String userIdArg;

    /**
     * 用于显示正在执行的数据
     */
    private boolean showProcessing;


    /**
     * 包名
     */
    private String pkg;
    /**
     * 类型
     */
    private String type;
    /**
     * 来源
     */
    private String source;
    /**
     * 数据dataId，优先dataId，没有取userId，再没有取用户IP
     */
    private String dataId;
    private String userId;

    public void readTaskParam(TaskToolConfiguration taskToolConfiguration, HttpServletRequest request, TaskComputeReq taskComputeReq){
        this.type = taskComputeReq.getType();
        this.category = taskComputeReq.getCategory();
        this.methodCode = taskComputeReq.getMethodCode();
        if(taskComputeReq.getType()!=null) {
            this.userIdArg = taskComputeReq.getUserIdParamName();
            this.dataIdArg = taskComputeReq.getDataIdParamName();
        }
        else {
            this.userIdArg = taskToolConfiguration.getControllerConfig().getUserIdField();
            this.dataIdArg = taskToolConfiguration.getControllerConfig().getDataIdField();
        }
        this.setDataId(RequestUtils.getRequestValue(request, dataIdArg));
        this.setUserId(RequestUtils.getRequestValue(request, userIdArg));
    }

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
