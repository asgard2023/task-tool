package cn.org.opendfl.tasktool.controller;

import cn.org.opendfl.tasktool.biz.ITaskTestBiz;
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
    public String hello(@RequestParam("name") String name) {
        return taskTestBiz.hello(name);
    }
}
