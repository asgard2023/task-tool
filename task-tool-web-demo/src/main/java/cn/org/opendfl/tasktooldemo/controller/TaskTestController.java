package cn.org.opendfl.tasktooldemo.controller;


import cn.org.opendfl.tasktool.task.annotation.TaskComputeController;
import cn.org.opendfl.tasktooldemo.biz.ITaskTestBiz;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("taskTest")
public class TaskTestController {
    @Resource
    private ITaskTestBiz taskTestBiz;

    @RequestMapping(value = "hello", method = {RequestMethod.POST, RequestMethod.GET})
    public String hello(@RequestParam("name") String name, @RequestParam(value = "sleep", defaultValue = "1") Integer sleep) {
        return taskTestBiz.hello(name, sleep);
    }

    @RequestMapping(value = "hello2", method = {RequestMethod.POST, RequestMethod.GET})
    @TaskComputeController(dataIdParamName = "name", userIdParamName = "uid", sourceType = "url")
    public String hello2(@RequestParam("name") String name, @RequestParam("uid") String uid, @RequestParam(value = "sleep", defaultValue = "1") Integer sleep) {
        return taskTestBiz.hello(name, sleep);
    }

    @RequestMapping(value = "hello3", method = {RequestMethod.POST, RequestMethod.GET})
    @TaskComputeController(dataIdParamName = "name", userIdParamName = "uid", sourceType = "uri")
    public String hello3(@RequestParam("name") String name, @RequestParam("uid") String uid, @RequestParam(value = "sleep", defaultValue = "1") Integer sleep) {
        return taskTestBiz.hello(name, sleep);
    }

    @RequestMapping(value = "helloError", method = {RequestMethod.POST, RequestMethod.GET})
    public String helloError(@RequestParam("name") String name, @RequestParam(value = "sleep", defaultValue = "1") Integer sleep) {
        return taskTestBiz.helloError(name, sleep);
    }

    @RequestMapping(value = "randomNum", method = {RequestMethod.POST, RequestMethod.GET})
    public int randomNum() {
        return taskTestBiz.randomNum();
    }

    @RequestMapping(value = "unauthorized", method = {RequestMethod.POST, RequestMethod.GET})
    public String unauthorized(HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().append("fail");
        return null;
    }

    @GetMapping(value = "vod/{lineCode}/{roomId}")
    public Object vodM3u8(@PathVariable("lineCode") String lineCode, @PathVariable("roomId") String roomId
            , HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        String sign = request.getParameter("sign");
        String t = request.getParameter("t");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", "ok");

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("lineCode", lineCode);
        dataMap.put("roomId", roomId);
        resultMap.put("data", dataMap);
        return resultMap;
    }
}
