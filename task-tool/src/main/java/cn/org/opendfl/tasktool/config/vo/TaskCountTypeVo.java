package cn.org.opendfl.tasktool.config.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCountTypeVo {
    private String code;
    private int timeSeconds;
    private String name;
    /**
     * 是否保存到数据库
     */
    private boolean saveDb;
}
