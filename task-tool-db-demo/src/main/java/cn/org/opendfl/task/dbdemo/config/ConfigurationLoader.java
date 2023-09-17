package cn.org.opendfl.task.dbdemo.config;

import cn.org.opendfl.tasktool.task.TaskToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 启动后加载
 */
@Component
@Slf4j
public class ConfigurationLoader implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired(required = false)
    private TaskToolConfiguration taskToolConfiguration;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (taskToolConfiguration != null) {
            TaskToolUtils.setTaskToolConfig(taskToolConfiguration);
        }
    }
}
