package cn.org.opendfl.tasktool.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * web请求拦截器
 *
 * @author chenjh
 */
@Configuration
@Slf4j
public class ControllerlInterceptor extends WebMvcConfigurationSupport {
    @Resource(name = "taskControllerHandler")
    private HandlerInterceptor controllerHandler;


    @Resource
    private TaskToolConfiguration taskToolConfiguration;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (taskToolConfiguration.getControllerConfig().isEnable()) {
            log.info("----addInterceptors--packages={}", taskToolConfiguration.getControllerConfig().getPackages());
            registry.addInterceptor(controllerHandler)
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