package cn.org.opendfl.tasktooldemo.controller;


import cn.org.opendfl.tasktooldemo.biz.ITaskTestBiz;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("taskTest")
public class TaskTestController {
    @Resource
    private ITaskTestBiz taskTestBiz;

    @RequestMapping(value = "hello", method = {RequestMethod.POST, RequestMethod.GET})
    public String hello(@RequestParam("name") String name, @RequestParam(value = "sleep", defaultValue = "1") Integer sleep) {
        return taskTestBiz.hello(name, sleep);
    }

    @RequestMapping(value = "helloError", method = {RequestMethod.POST, RequestMethod.GET})
    public String helloError(@RequestParam("name") String name, @RequestParam(value = "sleep", defaultValue = "1") Integer sleep) {
        return taskTestBiz.helloError(name, sleep);
    }
}
