package cn.org.opendfl.tasktool;

import cn.org.opendfl.tasktool.base.PageVO;
import cn.org.opendfl.tasktool.biz.ITaskHostBiz;
import cn.org.opendfl.tasktool.client.TaskHostRest;
import cn.org.opendfl.tasktool.client.TaskInfoRest;
import cn.org.opendfl.tasktool.task.TaskCountVo;
import cn.org.opendfl.tasktool.task.TaskHostVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

@SpringBootTest
@ActiveProfiles(value = "dev")
public class TaskHostRestTest {
    @Resource
    private ITaskHostBiz taskHostBiz;
    @Resource
    private TaskHostRest taskHostRest;

    @Resource
    private TaskInfoRest taskInfoRest;

    @Test
    public void addHost(){
        TaskHostVo hostVo = new TaskHostVo();
        hostVo.setCode("local");
        hostVo.setType("test");
        hostVo.setIp("localhost");
        hostVo.setPort(8080);
        hostVo.setAuthKey("");
        String result=taskHostRest.addHost(hostVo);
        System.out.println(result);
    }

    @Test
    public void getRunInfo(){
        addHost();
        TaskCountVo taskCountVo = new TaskCountVo();
        PageVO page = new PageVO();
        Object object=taskInfoRest.getRunInfo("local", taskCountVo,page);
        System.out.println(object);

    }
}
