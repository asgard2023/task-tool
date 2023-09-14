package cn.org.opendfl.tasktool.config.vo;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 接口请求统计，即用于web接口或controller接口
 *
 * @author chenjh
 */
@Data
public class ControllerConfigVo {
    private boolean enable = true;
    /**
     * 支持的包名，默认*表示全部
     * 多个以“,”隔开
     * 算法为实际包名以任何一个package开头进行匹配
     */
    private List<String> packages = Arrays.asList("*");
    /**
     * uri接口白名单，忽略计算处理
     * 比发/test/hello
     * 在白名单内的接口，忽略统计处理
     */
    private String uriWhitelist = "none";
    /**
     * userId参数名，,用于记录最大执行时间或错时对应的userId,没有dataId时才用userId，也没有userId时，用ip
     */
    private String userIdField = "userId";

    /**
     * dataId参数名,用于记录最大执行时间或错时对应的dataId
     */
    private String dataIdField = "dataId";

    /**
     * 是否记录请求来源(按请求参数分别累计)次数
     */
    private boolean source=false;
    /**
     * 是否显示正在处理的请求
     */
    private boolean processing=false;
}
