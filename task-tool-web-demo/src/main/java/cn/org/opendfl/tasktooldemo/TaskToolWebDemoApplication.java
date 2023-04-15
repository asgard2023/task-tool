package cn.org.opendfl.tasktooldemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author chenjh
 */
@SpringBootApplication(scanBasePackages = {"cn.org.opendfl.tasktool", "cn.org.opendfl.tasktooldemo"})
@EnableConfigurationProperties
@ServletComponentScan(basePackages = "cn.org.opendfl.tasktooldemo.controller")
@EnableScheduling
public class TaskToolWebDemoApplication {
    public static final Logger logger = LoggerFactory.getLogger(TaskToolWebDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TaskToolWebDemoApplication.class, args);
    }
}
