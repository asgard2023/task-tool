package cn.org.opendfl.tasktooldemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("taskHost")
@Slf4j
public class TaskHostController extends cn.org.opendfl.tasktool.controller.TaskHostController {
}
