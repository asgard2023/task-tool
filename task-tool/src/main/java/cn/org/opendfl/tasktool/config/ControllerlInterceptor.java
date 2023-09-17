package cn.org.opendfl.tasktool.config;

import cn.org.opendfl.tasktool.task.TaskControllerHandler;
import cn.org.opendfl.tasktool.task.TaskToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * web请求拦截器
 *
 * @author chenjh
 */
@Slf4j
public class ControllerlInterceptor extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (TaskToolUtils.getTaskToolConfig().getControllerConfig().isEnable()) {
            log.info("----addInterceptors--packages={}", TaskToolUtils.getTaskToolConfig().getControllerConfig().getPackages());
            registry.addInterceptor(new TaskControllerHandler())
                    .addPathPatterns("/**");
        }
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("----addResourceHandlers--");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/scripts/**").addResourceLocations("classpath:/scripts/");
        super.addResourceHandlers(registry);
    }
}