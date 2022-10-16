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
    /**
     * 类型编码，如D,M,MI等
     */
    private String code;
    private int timeSeconds;
    private String name;
    /**
     * 日期格式化，格式化的结果要转成数字Integer，不能含特殊字符，比如yyyyMMdd，也不能太长，造成超出Integer最大值
     */
    private String dateFormat;
    /**
     * 是否保存到数据库
     */
    private boolean saveDb;
}
