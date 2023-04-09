package cn.org.opendfl.tasktool.controller;

import cn.org.opendfl.tasktool.base.PageVO;
import cn.org.opendfl.tasktool.biz.ITaskHostBiz;
import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import cn.org.opendfl.tasktool.task.TaskHostVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 集群查询
 */
@RestController
@RequestMapping("taskHost")
@Slf4j
public class TaskHostController {
    @Resource
    private TaskToolConfiguration taskToolConfiguration;

    @Resource
    private ITaskHostBiz taskHostBiz;

    /**
     * 增加host
     *
     * @param taskHost
     * @return
     */
    @PostMapping("add")
    public Object addHost(@RequestParam(value = "authKey", required = false) String key, @RequestBody TaskHostVo taskHost) {
        if (!taskToolConfiguration.getSecurityKey().equals(key)) {
            log.warn("----key={} invalid", key);
            return "{\"errorMsg\":\"auth fail\"}";
        }
        this.taskHostBiz.addHost(taskHost);
        return "{\"success\":\"ok\"}";
    }

    @RequestMapping(value = "hosts", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getHosts(@RequestParam(value = "authKey", required = false) String key
            , TaskHostVo taskHostVo, PageVO page) {
        if (!taskToolConfiguration.getSecurityKey().equals(key)) {
            log.warn("----key={} invalid", key);
            return "{\"errorMsg\":\" auth fail\"}";
        }
        List<TaskHostVo> list = this.taskHostBiz.getHosts(taskHostVo, null, null, page);
        page.setTotalSize(list.size());
        page.setDatas(list);
        return page;
    }

    /**
     * 配置查询
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "hostList", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getConfig(@RequestParam(value = "authKey", required = false) String key
            , TaskHostVo taskHostVo, PageVO page) {
        if (!taskToolConfiguration.getSecurityKey().equals(key)) {
            return "{\"errorMsg\":\"auth fail\"}";
        }

        List<TaskHostVo> list = this.taskHostBiz.getHosts(taskHostVo, null, null, page);
        List<TaskHostVo> list2=new ArrayList<>();
        TaskHostVo local = new TaskHostVo();
        local.setCode("");
        local.setRemark("local");
        list2.add(local);
        list2.addAll(list);
        return list2;
    }

    /**
     * 删除
     *
     * @param key
     * @param code
     * @param request
     * @return
     */
    @RequestMapping(value = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    public Object delete(@RequestParam(value = "authKey", required = false) String key
            , @RequestParam(name = "code", required = false) String code, HttpServletRequest request) {
        if (!taskToolConfiguration.getSecurityKey().equals(key)) {
            log.warn("----key={} invalid", key);
            return "{\"errorMsg\":\"auth fail\"}";
        }
        boolean isDel = taskHostBiz.delete(code);
        return "{\"success\":\"" + isDel + "\"}";
    }
}
