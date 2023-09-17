package cn.org.opendfl.tasktool.controller;

import cn.org.opendfl.tasktool.base.PageVO;
import cn.org.opendfl.tasktool.biz.ITaskHostBiz;
import cn.org.opendfl.tasktool.biz.TaskHostBiz;
import cn.org.opendfl.tasktool.task.TaskHostVo;
import cn.org.opendfl.tasktool.task.TaskToolUtils;
import cn.org.opendfl.tasktool.utils.RequestParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 集群查询
 */
//@RestController
//@RequestMapping("taskHost")
@Slf4j
public class TaskHostController {

    private ITaskHostBiz taskHostBiz = new TaskHostBiz();
    public void setTaskHostBiz(ITaskHostBiz taskHostBiz){
        this.taskHostBiz = taskHostBiz;
    }


    /**
     * 增加host
     *
     * @param taskHost
     * @return
     */
    @PostMapping("add")
    public Object addHost(@RequestParam(value = RequestParams.AUTH_KEY, required = false) String authKey, @RequestBody TaskHostVo taskHost, HttpServletRequest request) {
        if (!TaskToolUtils.getTaskToolConfig().isAuth(authKey, request)) {
            log.warn("----key={} invalid", authKey);
            return "{\"errorMsg\":\"auth fail\"}";
        }
        this.taskHostBiz.addHost(taskHost);
        return "{\"success\":true,\"errorMsg\":\"ok\"}";
    }

    /**
     * 增加host
     *
     * @param taskHost
     * @return
     */
    @PostMapping("save")
    public Object save(@RequestParam(value = RequestParams.AUTH_KEY, required = false) String authKey, @RequestBody TaskHostVo taskHost, HttpServletRequest request) {
        if (!TaskToolUtils.getTaskToolConfig().isAuth(authKey, request)) {
            log.warn("----save--key={} invalid", authKey);
            return "{\"errorMsg\":\"auth fail\"}";
        }
        this.taskHostBiz.save(taskHost);
        return "{\"success\":true,\"errorMsg\":\"ok\"}";
    }

    @RequestMapping(value = "hosts", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getHosts(@RequestParam(value = RequestParams.AUTH_KEY, required = false) String authKey
            , TaskHostVo taskHostVo, PageVO<TaskHostVo> page, HttpServletRequest request) {
        if (!TaskToolUtils.getTaskToolConfig().isAuth(authKey, request)) {
            log.warn("----hosts--key={} invalid", authKey);
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
     * @param authKey
     * @return
     */
    @RequestMapping(value = "hostList", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getConfig(@RequestParam(value = RequestParams.AUTH_KEY, required = false) String authKey
            , TaskHostVo taskHostVo, PageVO<TaskHostVo> page, HttpServletRequest request) {
        if (!TaskToolUtils.getTaskToolConfig().isAuth(authKey, request)) {
            log.warn("----hostList--key={} invalid", authKey);
            return "{\"errorMsg\":\"auth fail\"}";
        }

        page.setSort("type");
        page.setOrder("asc");
        List<TaskHostVo> list = this.taskHostBiz.getHosts(taskHostVo, null, null, page);
        list.stream().forEach(t -> t.setRemark(t.getType() + "-" + t.getName()));
        List<TaskHostVo> list2 = new ArrayList<>();
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
     * @param authKey
     * @param code
     * @param request
     * @return
     */
    @RequestMapping(value = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    public Object delete(@RequestParam(value = RequestParams.AUTH_KEY, required = false) String authKey
            , @RequestParam(name = "code", required = false) String code, HttpServletRequest request) {
        if (!TaskToolUtils.getTaskToolConfig().isAuth(authKey, request)) {
            log.warn("----delete--key={} invalid", authKey);
            return "{\"errorMsg\":\"auth fail\"}";
        }
        boolean isDel = taskHostBiz.delete(code);
        return "{\"success\":" + isDel + ",\"errorMsg\":\"ok\"}";
    }
}
