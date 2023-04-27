package cn.org.opendfl.tasktool.task.annotation;

import lombok.Data;

@Data
public class TaskComputeReq {
    public TaskComputeReq(){

    }
    public void load(TaskComputeServlet taskComputeServlet, String uri){
        this.type = "TaskComputeServlet";
        this.methodCode = taskComputeServlet.methodCode();
        this.dataIdParamName = taskComputeServlet.dataIdParamName();
        this.userIdParamName = taskComputeServlet.userIdParamName();
        this.category = taskComputeServlet.category();
        this.uri = uri;
    }
    public void load(TaskComputeController taskComputeController, String uri){
        this.type = "TaskComputeController";
        this.methodCode = taskComputeController.methodCode();
        this.dataIdParamName = taskComputeController.dataIdParamName();
        this.userIdParamName = taskComputeController.userIdParamName();
        this.category = taskComputeController.category();
        this.uri = uri;
    }
    /**
     * 方法编码，方法名，类名加方法名唯一，为空默认为当前方法名
     *
     * @return
     */
    private String methodCode;

    /**
     * 数据id的参数序号，-1表示无参
     * 0表示第一个参数，如果无参数的话也兼容
     * 建议放第1个，以免在中间增加参数时序号变了
     *
     * @return
     */
    private String userIdParamName;

    /**
     * 用于显示正在执行的数据
     *
     * @return
     */
    private String dataIdParamName;

    /**
     * 仅分类，没有其他作用
     *
     * @return
     */
    private String category;
    private String type;
    private String uri;
}
