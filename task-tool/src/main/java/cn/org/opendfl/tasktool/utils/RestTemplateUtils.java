package cn.org.opendfl.tasktool.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class RestTemplateUtils {
    private RestTemplateUtils(){

    }
    private static RestTemplate restTemplate;
    private static boolean isContainRestTemplateBeanCheck = false;

    public static RestTemplate getRestTemplate() {
        if (restTemplate == null && !isContainRestTemplateBeanCheck) {
            isContainRestTemplateBeanCheck = true;
            boolean isContain = isContainRestTemplate();
            if (isContain) {
                restTemplate = SpringUtils.getBean(RestTemplate.class);
            }
        }
        return restTemplate;
    }

    public static boolean isContainRestTemplate(){
        boolean isContain = SpringUtils.getApplicationContext().containsBean("restTemplate");
        if(!isContain){
            log.warn("A component required a bean of type 'org.springframework.web.client.RestTemplate' that could not be found.");
        }
        return isContain;
    }
}
