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

    /**
     * 统计时间类型
     */
    public enum countTimeType {
        /**
         * 分钟
         */
        MINUTE("MI", "Minute", "ddHHmm", 60),
        /**
         * 小时
         */
        HOUR("H", "Hour", "yyMMddHH", 3600),
        /**
         * 天
         */
        DAY("D", "Day", "yyMMdd", 86400),
        /**
         * 月
         */
        MONTH("M", "Month", "yyMM", 31 * 86400),
        /**
         * 全部
         */
        TOTAL("T", "Total", "0", -1),
        ;
        private String timeType;
        private String code;
        private String format;
        private int timeSeconds;

        countTimeType(String timeType, String code, String format, int timeSeconds) {
            this.timeType = timeType;
            this.code = code;
            this.format = format;
            this.timeSeconds = timeSeconds;
        }

        public String getTimeType() {
            return timeType;
        }

        public String getCode() {
            return code;
        }

        public String getFormat() {
            return format;
        }

        public int getTimeSeconds() {
            return timeSeconds;
        }

        public static final countTimeType parseTimeType(String timeTypeCode) {
            countTimeType[] types = countTimeType.values();
            for (countTimeType type : types) {
                if (type.timeType.equals(timeTypeCode)) {
                    return type;
                }
            }
            return null;
        }

        public static final countTimeType parseCode(String code) {
            countTimeType[] types = countTimeType.values();
            for (countTimeType type : types) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }

    public static int getDateInt(Date date, String countType, String format) {
        if (date == null) {
            return 0;
        }
        if (CharSequenceUtil.isBlank(format)) {
            countTimeType timeType = countTimeType.parseTimeType(countType);
            if (timeType != null) {
                if (countTimeType.TOTAL == timeType) {
                    return 0;
                }
                format = timeType.getFormat();
            }
        }
        String dateStr = DateUtil.format(date, format);
        return Integer.parseInt(dateStr);
    }
}
