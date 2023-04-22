package cn.org.opendfl.tasktool.client;

import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import cn.org.opendfl.tasktool.config.vo.TaskToolVo;
import cn.org.opendfl.tasktool.task.TaskHostVo;
import cn.org.opendfl.tasktool.utils.CommUtils;
import cn.org.opendfl.tasktool.utils.RestTemplateUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 用于远程调用
 */
@Service
@Slf4j
public class TaskHostRest {

    @Resource
    private TaskToolConfiguration taskToolConfiguration;


    public String addHost(TaskHostVo taskHostVo) {
        TaskToolVo taskToolVo = taskToolConfiguration.getTaskToolCentral();
        String url = CommUtils.appendUrl(taskToolVo.getApiUrl(), "taskHost/add");
        url += "?authKey=" + taskToolVo.getAuthKey();
        String bodyString = JSON.toJSONString(taskHostVo);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntitys = new HttpEntity<>(bodyString, httpHeaders);
        ResponseEntity<String> exchanges = RestTemplateUtils.getRestTemplate().postForEntity(url, httpEntitys, String.class);
        if (exchanges.getStatusCode() != HttpStatus.OK) {
            log.warn("----addHost={}, errorCode={} resultRemote={}", taskHostVo.getCode(), exchanges.getStatusCode(), exchanges.getBody());
            return null;
        }
        return exchanges.getBody();
    }

}
