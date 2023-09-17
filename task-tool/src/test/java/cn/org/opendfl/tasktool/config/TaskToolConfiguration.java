package cn.org.opendfl.tasktool.config;

import cn.org.opendfl.tasktool.config.vo.TaskToolConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 任务工具配置
 *
 * @author chenjh
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "task-tool")
@Slf4j
public class TaskToolConfiguration extends TaskToolConfig {

}
