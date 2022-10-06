package cn.org.opendfl.tasktool.config.vo;

import lombok.Data;

import java.util.List;

/**
 * 信息系统
 *
 * @author chenjh
 */
@Data
public class AppInfoVo {
    /**
     * 版本号
     */
    private String version;

    private List<TaskCountTypeVo> counterTimeTypes;

    private int runTimeBase;
    /**
     * 系统启动时间
     */
    private long startTime;
    /**
     * 当前时间
     */
    private long systemTime;

}