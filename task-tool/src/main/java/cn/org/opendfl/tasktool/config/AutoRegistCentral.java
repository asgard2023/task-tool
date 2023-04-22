package cn.org.opendfl.tasktool.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.tasktool.biz.ITaskHostBiz;
import cn.org.opendfl.tasktool.client.TaskHostRest;
import cn.org.opendfl.tasktool.config.vo.TaskLocalVo;
import cn.org.opendfl.tasktool.config.vo.TaskToolVo;
import cn.org.opendfl.tasktool.controller.TaskHostController;
import cn.org.opendfl.tasktool.controller.TaskInfoController;
import cn.org.opendfl.tasktool.task.TaskHostVo;
import cn.org.opendfl.tasktool.utils.CommUtils;
import cn.org.opendfl.tasktool.utils.RestTemplateUtils;
import cn.org.opendfl.tasktool.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${project.build.timestamp}")
    private String buildTime;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.taskHostBizInit();
        this.autoRegistHost();
    }

    private TaskHostVo toHost(TaskLocalVo taskLocalVo) {
        TaskHostVo taskHost = new TaskHostVo();
        BeanUtil.copyProperties(taskLocalVo, taskHost);
        return taskHost;
    }

    public void taskHostBizInit() {
        if (CharSequenceUtil.isNotBlank(taskToolConfiguration.getTaskHostBizName())) {
            Object taskHostBiz = SpringUtils.getBean(taskToolConfiguration.getTaskHostBizName());
            if (taskHostBiz instanceof ITaskHostBiz) {
                TaskHostController taskHostController = SpringUtils.getBean(TaskHostController.class);
                taskHostController.setTaskHostBiz((ITaskHostBiz) taskHostBiz);

                TaskInfoController taskInfoController = SpringUtils.getBean(TaskInfoController.class);
                taskInfoController.setTaskHostBiz((ITaskHostBiz) taskHostBiz);
                log.info("----taskHostBizInit--buildTime={} taskHostBiz={}", buildTime, taskHostBiz);
            }
        }
    }

    private void autoRegistHost() {
        TaskToolVo taskToolCentral = taskToolConfiguration.getTaskToolCentral();
        boolean isContainRestTemplate = RestTemplateUtils.isContainRestTemplate();
        if (taskToolCentral.isOpen() && isContainRestTemplate) {
            TaskHostRest taskHostRest = SpringUtils.getBean(TaskHostRest.class);
            Environment environment = SpringUtils.getBean(Environment.class);
            TaskLocalVo taskLocal = taskToolCentral.getTaskLocal();
            if (CharSequenceUtil.isBlank(taskLocal.getAuthKey())) {
                taskLocal.setAuthKey(taskToolConfiguration.getSecurityKey());
            }
            if (taskLocal.getPort() == 0 && !initServerPort(environment, taskLocal)) {
                log.warn("----autoRegistHost--serverPort is null");
                return;
            }

            TaskHostVo taskHostVo = toHost(taskLocal);
            if (CharSequenceUtil.isNumeric(buildTime)) {
                taskHostVo.setBuildTime(Long.parseLong(buildTime));
            }
            taskHostVo.setName(environment.getProperty("spring.application.name"));
            taskHostVo.setProfile(CommUtils.join(environment.getActiveProfiles(), ","));
            try {
                String result = taskHostRest.addHost(taskHostVo);
                log.info("---autoRegistHost--remoteApi={} result={} taskHost={}", taskToolCentral.getApiUrl(), result, taskHostVo);
            } catch (Exception e) {
                log.warn("---autoRegistHost--remoteApi={} error={}", taskToolCentral.getApiUrl(), e.getMessage(), e);
            }
        } else {
            log.warn("---autoRegistHost--taskToolCentral.open={} isContainRestTemplate={} not auto register host", taskToolCentral.isOpen(), isContainRestTemplate);
        }
    }

    private static boolean initServerPort(Environment environment, TaskLocalVo taskLocal) {
        String serverPort = environment.getProperty("local.server.port");
        if (serverPort == null) {
            serverPort = environment.getProperty("server.port");
        }
        boolean hasServrPort=serverPort!=null;
        if (hasServrPort) {
            taskLocal.setPort(Integer.parseInt(serverPort));
        }
        return hasServrPort;
    }
}
