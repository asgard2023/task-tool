package cn.org.opendfl.task.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Version V1.0
 * 任务运行次数统计记录 实体
 * @author: chenjh
 * @Date: 2022年10月15日 下午8:15:58
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
@Data
@Table(name = "ta_method_count")
@JsonInclude(Include.ALWAYS)
public class TaMethodCountPo implements Serializable {

    /**
     * id
     */
    @Id
    @Column(name = "id")
    private Integer id;
    /**
     * 数据方法id
     */
    @Column(name = "data_method_id")
    private Integer dataMethodId;
    /**
     * 时间值
     */
    @Column(name = "time_value")
    private Integer timeValue;
    /**
     * 时间类型
     */
    @Column(name = "time_type")
    @Length(message = "timeType超出最大长度10限制", max = 10)
    private String timeType;
    /**
     * 运行次数
     */
    @Column(name = "run_count")
    private Long runCount;
    /**
     * 首次运行时间
     */
    @Column(name = "first_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date firstTime;
    /**
     * 最新运行时间(ms)
     */
    @Column(name = "run_time")
    private Integer runTime;
    /**
     * 错误次数
     */
    @Column(name = "error_count")
    private Integer errorCount;
    /**
     * 最新错误信息
     */
    @Column(name = "error_newly_info")
    @Length(message = "errorNewlyInfo超出最大长度255限制", max = 255)
    private String errorNewlyInfo;
    /**
     * 最新错误dataId
     */
    @Column(name = "error_newly_data_id")
    @Length(message = "errorNewlyDataId超出最大长度64限制", max = 64)
    private String errorNewlyDataId;
    /**
     * 最新错误时间
     */
    @Column(name = "error_newly_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date errorNewlyTime;
    /**
     * 最新错误对应的服务器名
     */
    @Column(name = "error_newly_server")
    @Length(message = "errorNewlyServer超出最大长度64限制", max = 64)
    private String errorNewlyServer;
    /**
     * 最大执行时间(ms)
     */
    @Column(name = "max_run_time")
    private Integer maxRunTime;
    /**
     * 最大执行时间发生时间
     */
    @Column(name = "max_run_time_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date maxRunTimeDate;
    /**
     * 最大执行时间对应dataId
     */
    @Column(name = "max_run_time_data_id")
    @Length(message = "maxRunTimeDataId超出最大长度64限制", max = 64)
    private String maxRunTimeDataId;
    /**
     * 最大执行时间对应的服务器名
     */
    @Column(name = "max_run_server")
    @Length(message = "maxRunServer超出最大长度64限制", max = 64)
    private String maxRunServer;
    /**
     * 是否删除
     */
    @Column(name = "if_del")
    private Integer ifDel;
    /**
     * 是否有效
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;
}