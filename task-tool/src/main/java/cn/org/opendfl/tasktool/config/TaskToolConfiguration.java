package cn.org.opendfl.tasktool.config;

import cn.org.opendfl.tasktool.config.vo.TaskCountTypeVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * 任务工具配置
 *
 * @author chenjh
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "task-tool")
@Slf4j
public class TaskToolConfiguration {
    /**
     * 版本号
     */
    private String version = "1";

    /**
     * 任务统计类型配置
     */
    private List<TaskCountTypeVo> counterTimeTypes = Arrays.asList(new TaskCountTypeVo("H", 3600, "Hour", true)
            , new TaskCountTypeVo("D", 86400, "Day", true)
            , new TaskCountTypeVo("T", -1, "total", true));

    /**
     * 基本执行时间(单位ms)
     * 即：低于这个时间的不记录最大执行时间
     */
    private int runTimeBase = 100;

    private String securityKey = "tasktooltest";

    /**
     * 是否保存服务器名
     */
    private boolean isSaveServerName;
}
