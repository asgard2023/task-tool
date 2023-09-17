package cn.org.opendfl.tasktool.client;

import cn.hutool.json.JSONUtil;
import cn.org.opendfl.tasktool.config.vo.TaskToolVo;
import cn.org.opendfl.tasktool.task.TaskHostVo;
import cn.org.opendfl.tasktool.task.TaskToolUtils;
import cn.org.opendfl.tasktool.utils.CommUtils;
import cn.org.opendfl.tasktool.utils.RestTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

/**
 * 用于远程调用
 */
@Slf4j
public class TaskHostRest {


    public String addHost(TaskHostVo taskHostVo) {
        TaskToolVo taskToolVo = TaskToolUtils.getTaskToolConfig().getTaskToolCentral();
        String url = CommUtils.appendUrl(taskToolVo.getApiUrl(), "taskHost/add");
        url += "?authKey=" + taskToolVo.getAuthKey();
        String bodyString = JSONUtil.toJsonStr(taskHostVo);
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
