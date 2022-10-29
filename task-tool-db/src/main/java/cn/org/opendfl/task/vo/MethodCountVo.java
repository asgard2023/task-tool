package cn.org.opendfl.task.vo;

import cn.org.opendfl.task.po.TaDataMethodPo;
import lombok.Data;

@Data
public class MethodCountVo {
    private int dataMethodId;
    private TaDataMethodPo dataMethod;
    private String  methodCode;
    private Integer rowCount;
    private Integer runCountTotal;
}
