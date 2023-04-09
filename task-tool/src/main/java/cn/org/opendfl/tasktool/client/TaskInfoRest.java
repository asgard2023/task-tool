package cn.org.opendfl.tasktool.client;

import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.tasktool.base.PageVO;
import cn.org.opendfl.tasktool.biz.ITaskHostBiz;
import cn.org.opendfl.tasktool.task.TaskCountVo;
import cn.org.opendfl.tasktool.task.TaskHostVo;
import cn.org.opendfl.tasktool.utils.CommUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class TaskInfoRest {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ITaskHostBiz taskHostBiz;

    private String getApiUrl(TaskHostVo taskHostVo) {
        return "http://" + taskHostVo.getIp() + ":" + taskHostVo.getPort();
    }

    public Object getRunInfo(String taskHostCode, TaskCountVo taskCountVo, PageVO page) {
        TaskHostVo taskHostVo = taskHostBiz.getTaskHost(taskHostCode);
        String apiUrl = getApiUrl(taskHostVo);
        String url = CommUtils.appendUrl(apiUrl, "taskInfo/runInfoJson");
        url += "?authKey=" + taskHostVo.getAuthKey();
        if (CharSequenceUtil.isNotBlank(page.getSort())) {
            url += "&sort=" + page.getSort();
        }
        if (CharSequenceUtil.isNotBlank(page.getOrder())) {
            url += "&order=" + page.getOrder();
        }


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String bodyString = JSON.toJSONString(taskCountVo);
        HttpEntity<String> httpEntitys = new HttpEntity<>(bodyString, httpHeaders);
        ResponseEntity<String> exchanges = restTemplate.postForEntity(url, httpEntitys, String.class);
        log.info("----getRunInfo--bodyString={}, resultRemote={}", bodyString, exchanges.getBody());
        return exchanges.getBody();
    }

    public Object getConfig(String taskHostCode, String type) {
        TaskHostVo taskHostVo = taskHostBiz.getTaskHost(taskHostCode);
        String apiUrl = getApiUrl(taskHostVo);
        String url = CommUtils.appendUrl(apiUrl, "taskInfo/config");
        url += "?authKey=" + taskHostVo.getAuthKey();
        if (CharSequenceUtil.isNotBlank(type)) {
            url += "&type=" + type;
        }


        ResponseEntity<String> exchanges = restTemplate.getForEntity(url, String.class);
        log.info("----getConfig--taskHostCode={}, resultRemote={}", taskHostCode, exchanges.getBody());
        return exchanges.getBody();
    }
}
