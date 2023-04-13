package cn.org.opendfl.tasktool.task;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaskHostVo implements Serializable {
    private String code;
    private String type;
    private String ip;
    private int port;
    private String authKey;
    private Long buildTime;
    /**
     * 首次加入时间
     */
    private Date joinDate;
    /**
     * 修改时间
     */
    private Date updateDate;
    /**
     * 心路时间
     */
    private Date heartbeat;
    private String name;
    private String profile;
    private String remark;

}
