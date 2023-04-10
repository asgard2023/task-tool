package cn.org.opendfl.tasktool.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("systemInfo")
@Slf4j
public class SystemInfoController {

    @Resource
    private TaskToolConfiguration taskToolConfiguration;

    /**
     * 配置查询
     *
     * @return
     */
    @PostMapping(value = "config")
    public Object getConfig() {
//        String authKey = taskToolConfiguration.getSecurityKey();
//        return "{\"authKey\":\"" + authKey + "\"}";
        log.info("---config--");
        return null;
    }

    @PostMapping(value = "checkKey")
    public Object checkKey(@RequestParam(value = "authKey", required = false) String authKey) {
        boolean isOk = false;
        String info=null;
        if (CharSequenceUtil.equals(authKey, taskToolConfiguration.getSecurityKey())) {
            isOk = true;
            info = "ok";
        }
        else{
            info="invalid authKey";
        }
        log.info("---checkKey--isOk={} info={}", isOk, info);
        return "{\"success\":" + isOk + ",\"tokenExpireTime\":"+3600+",\"errorMsg\":\""+info+"\"}";
    }
}
