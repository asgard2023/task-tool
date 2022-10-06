package cn.org.opendfl.tasktool;


import cn.hutool.json.JSONUtil;
import cn.org.opendfl.tasktool.biz.ITaskTestBiz;
import cn.org.opendfl.tasktool.task.TaskCountVo;
import cn.org.opendfl.tasktool.task.TaskToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = TaskToolApplication.class)
@ActiveProfiles(value = "test")
@Slf4j
public class TaskComputeTest {
    @Resource
    private ITaskTestBiz taskTestBiz;


    @Test
    public void hello() {
        String result = this.taskTestBiz.hello("chenjh");
        System.out.println(result);
        List<TaskCountVo> list= TaskToolUtils.getTaskCountInfo();
        System.out.println(JSONUtil.toJsonStr(list));
    }


}
