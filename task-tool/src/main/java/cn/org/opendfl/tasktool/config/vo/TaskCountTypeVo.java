package cn.org.opendfl.tasktool.config.vo;

import cn.org.opendfl.tasktool.constant.DateTimeConstant;
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
    public TaskCountTypeVo(DateTimeConstant.countTimeType countTimeType, boolean saveDb) {
        this.code = countTimeType.getTimeType();
        this.name = countTimeType.getCode();
        this.timeSeconds = countTimeType.getTimeSeconds();
        this.dateFormat = countTimeType.getFormat();
        this.saveDb = saveDb;
    }

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
     * 保留历史个数
     */
    private int hisCount=1;
    /**
     * 与TaskToolConfiguration.source共同起作用，即都为true才开启接口来源统计
     */
    private boolean source;

    /**
     * 与TaskToolConfiguration.processing共同起作用，即都为true才开启接口来源统计执行过程
     */
    private boolean processing;
    /**
     * 是否保存到数据库
     */
    private boolean saveDb;
}
