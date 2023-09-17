package cn.org.opendfl.tasktool.config.vo;

import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.tasktool.constant.DateTimeConstant;
import cn.org.opendfl.tasktool.utils.RequestParams;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Data
public class TaskToolConfig {
    /**
     * 版本号
     */
    private String version = "1.8";

    /**
     * 任务统计类型配置
     */
    private List<TaskCountTypeVo> counterTimeTypes = Arrays.asList(new TaskCountTypeVo(DateTimeConstant.countTimeType.HOUR, true)
            , new TaskCountTypeVo(DateTimeConstant.countTimeType.DAY, true)
            , new TaskCountTypeVo(DateTimeConstant.countTimeType.TOTAL, true));

    /**
     * 基本执行时间(单位ms)
     * 即：低于这个时间的不记录最大执行时间
     */
    private int runTimeBase = 100;
    /**
     * 用于控制日志输出量，使日志只在启动时输出一段时间，后面不再输出
     */
    private int startLogCount = 100;

    private String securityKey = "tasktooltest";

    /**
     * 是否保存服务器名
     */
    private boolean isSaveServerName;
    private boolean restTemplateConfig = true;

    private ControllerConfigVo controllerConfig = new ControllerConfigVo();

    private TaskToolVo taskToolCentral = new TaskToolVo();
    private String taskHostBizName;

    public boolean isAuth(String authKey, HttpServletRequest request) {
        if (CharSequenceUtil.isBlank(authKey)) {
            authKey = request.getHeader(RequestParams.AUTH_KEY);
        }
        return getSecurityKey().equals(authKey);
    }
}
