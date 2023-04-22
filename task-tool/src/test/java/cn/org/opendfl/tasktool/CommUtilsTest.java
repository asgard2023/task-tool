package cn.org.opendfl.tasktool;

import cn.org.opendfl.tasktool.utils.CommUtils;
import org.junit.jupiter.api.Test;

public class CommUtilsTest {
    @Test
    public void removeParam(){
        String url="https://www.lyf.com/user/info?uid=1&enc=88182&id=1001&t=1597372964477";
        System.out.println(url);
        url = CommUtils.removeParam(url, "enc", "uid");
        System.out.println(url);

        url="https://www.lyf.com/user/info?uid=1&enc=88182&id=1001&t=1597372964477";
        url = CommUtils.removeParam(url, "enc", "id");
        System.out.println(url);
    }
}
