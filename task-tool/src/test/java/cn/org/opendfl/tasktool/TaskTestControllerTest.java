package cn.org.opendfl.tasktool;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * mock测试
 *
 * @author chenjh
 */
@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@WebAppConfiguration
class TaskTestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    private final String HEADER_IP = "x-forwarded-for";

    /**
     * 用户访问频率-没有用户（默认走IP）
     * user request limit-- no userId use IP as default
     */
    @Test
    void hello() throws Exception {
        int successCount = 0;
        long time = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/taskTest/hello")
                            .param("name", "data" + i)
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn();
            int status = mvcResult.getResponse().getStatus();                 //得到返回代码
            String content = mvcResult.getResponse().getContentAsString();    //得到返回结果
            System.out.println("----hello status=" + status + " content=" + content);
            successCount++;
        }
        System.out.println("----hello  successCount=" + successCount + " time=" + (System.currentTimeMillis() - time));
        Assertions.assertEquals(20, successCount, "successCount:" + successCount);
//        Assertions.assertEquals(10, limtCount, "limtCount:" + limtCount);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/taskInfo/runInfo")
                        .param("key", "tasktooltest")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();                 //得到返回代码
        String content = mvcResult.getResponse().getContentAsString();    //得到返回结果
        System.out.println("----hello status=" + status + " content=" + content);
    }
}
