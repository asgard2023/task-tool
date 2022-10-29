package cn.org.opendfl.task.vo;

import cn.org.opendfl.task.po.TaDataMethodPo;
import lombok.Data;

@Data
public class MethodTimeValueCountVo {
    private int dataMethodId;
    private TaDataMethodPo dataMethod;
    private String  methodCode;
    private Integer timeValue;
    private Integer rowCount;
    private Integer runCount;
    private Integer maxRunTime;
    private Integer errorCount;
}
