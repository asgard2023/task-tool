package cn.org.opendfl.task.vo;

import cn.org.opendfl.task.po.TaDataMethodPo;
import lombok.Data;

import java.util.Date;

@Data
public class MethodCountStatisticVo {
    private int dataMethodId;
    private TaDataMethodPo dataMethod;
    private String timeType;
    private Integer timeValueMin;
    private Integer timeValueMax;
    private Integer runCountTotal;
    private Integer errorCountTotal;
    private Integer maxRunTime;
    private Date minDate;
    private Date maxDate;
    private Date minErrorDate;
    private Date maxErrorDate;
}
