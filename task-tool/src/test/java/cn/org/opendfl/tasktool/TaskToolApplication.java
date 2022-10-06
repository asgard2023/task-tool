package cn.org.opendfl.tasktool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author chenjh
 */
@SpringBootApplication
@EnableConfigurationProperties
public class TaskToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskToolApplication.class, args);
    }
}
