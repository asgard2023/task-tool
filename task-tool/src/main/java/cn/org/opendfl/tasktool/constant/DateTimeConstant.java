package cn.org.opendfl.tasktool.constant;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;

import java.util.Date;

/**
 * 时间毫秒数
 *
 * @author chenjh
 */
public class DateTimeConstant {
    private DateTimeConstant() {

    }

    /**
     * 秒毫秒数
     */
    public static final int SECOND_MILLIS = 1000;
    /**
     * 分钟毫秒数
     */
    public static final int MINUTE_MILLIS = 60000;
    /**
     * 小时毫秒数
     */
    public static final int HOUR_MILLIS = 3600000;
    /**
     * 日期毫秒数
     */
    public static final int DAY_MILLIS = 86400000;

    public static int getDateInt(Date date, String countType, String format) {
        if (date == null) {
            return 0;
        }
        if (CharSequenceUtil.isBlank(format)) {
            if ("D".equals(countType)) {
                format = "yyMMdd";
            } else if ("H".equals(countType)) {
                format = "yyMMddHH";
            } else if ("MI".equals(countType)) {
                format = "yyMMddHHmm";
                Long time=date.getTime()/MINUTE_MILLIS;
                return time.intValue();
            } else if ("M".equals(countType)) {
                format = "yyMM";
            } else if ("T".equals(countType)) {
                return 0;
            }
        }
        String dateStr = DateUtil.format(date, format);
        return Integer.parseInt(dateStr);
    }
}
