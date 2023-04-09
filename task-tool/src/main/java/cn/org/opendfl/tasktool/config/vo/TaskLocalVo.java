package cn.org.opendfl.tasktool.config.vo;

import lombok.Data;

@Data
public class TaskLocalVo {
    private String code;
    private String type;
    private String ip;
    private int port;
    private String authKey;
}
