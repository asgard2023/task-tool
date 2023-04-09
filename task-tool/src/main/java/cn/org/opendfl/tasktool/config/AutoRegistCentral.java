package cn.org.opendfl.tasktool.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.tasktool.client.TaskHostRest;
import cn.org.opendfl.tasktool.config.vo.TaskLocalVo;
import cn.org.opendfl.tasktool.config.vo.TaskToolVo;
import cn.org.opendfl.tasktool.task.TaskHostVo;
import cn.org.opendfl.tasktool.utils.CommUtils;
import cn.org.opendfl.tasktool.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自动把当前服务注册到taskToolCentral，方便查看
 *
 * @author chenjh
 */
@Component
@Slf4j
public class AutoRegistCentral implements ApplicationListener<ApplicationReadyEvent> {
    @Resource
    private TaskToolConfiguration taskToolConfiguration;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.autoRegistHost();
    }

    private TaskHostVo toHost(TaskLocalVo taskLocalVo) {
        TaskHostVo taskHost = new TaskHostVo();
        BeanUtil.copyProperties(taskLocalVo, taskHost);
        return taskHost;
    }

    public void autoRegistHost() {
        TaskToolVo taskToolCentral = taskToolConfiguration.getTaskToolCentral();
        if (taskToolCentral.isOpen()) {
            TaskHostRest taskHostRest = SpringUtils.getBean(TaskHostRest.class);
            Environment environment = SpringUtils.getBean(Environment.class);
            TaskLocalVo taskLocal = taskToolCentral.getTaskLocal();
            if (CharSequenceUtil.isBlank(taskLocal.getAuthKey())) {
                taskLocal.setAuthKey(taskToolConfiguration.getSecurityKey());
            }
            if (taskLocal.getPort() == 0) {
                taskLocal.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
            }

            TaskHostVo taskHostVo = toHost(taskLocal);
            taskHostVo.setName(environment.getProperty("spring.application.name"));
            taskHostVo.setProfile(CommUtils.join(environment.getActiveProfiles(), ","));
            try {
                String result = taskHostRest.addHost(taskHostVo);
                log.info("---autoRegistHost--remoteApi={} result={} taskHost={}", taskToolCentral.getApiUrl(), result, taskLocal);
            } catch (Exception e) {
                log.warn("---autoRegistHost--remoteApi={} error={}", taskToolCentral.getApiUrl(), e.getMessage(), e);
            }
        } else {
            log.info("---autoRegistHost--taskToolCentral.open={}", taskToolCentral.isOpen());
        }
    }
}
