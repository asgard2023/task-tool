package org.ccs.opendfldemo;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author chenjh
 */
@SpringBootApplication(scanBasePackages = {"cn.org.opendfl.task", "cn.org.opendfl.test", "cn.org.opendfl.tasktool"})
@EnableConfigurationProperties
@EnableSwagger2
@EnableKnife4j
@EnableScheduling
@MapperScan(basePackages = "cn.org.opendfl.task")
public class TaskDbDemoApplication {
    public static final Logger logger = LoggerFactory.getLogger(TaskDbDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TaskDbDemoApplication.class, args);
    }
}
