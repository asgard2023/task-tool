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
 * ta_data_method 实体
 * @author chenjh
 * @Date: 2022年10月15日 下午8:16:35
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
@Data
@Table(name = "ta_data_method")
@JsonInclude(Include.ALWAYS)
public class TaDataMethodPo implements Serializable {

    /**
     * id
     */
    @Id
    @Column(name = "id")
    private Integer id;
    /**
     * 方法名
     */
    @Column(name = "code")
    @Length(message = "code超出最大长度64限制", max = 64)
    private String code;
    /**
     * 名称
     */
    @Column(name = "name")
    @Length(message = "name超出最大长度128限制", max = 128)
    private String name;
    /**
     * 分类：仅分类，没基他作用
     */
    @Column(name = "category")
    @Length(message = "category超出最大长度64限制", max = 64)
    private String category;

    @Column(name = "type")
    @Length(message = "type超出最大长度64限制", max = 32)
    private String type;

    @Column(name = "pkg")
    @Length(message = "pkg超出最大长度64限制", max = 128)
    private String pkg;
    /**
     * 是否显示正在进行中的任务
     */
    @Column(name = "show_processing")
    private Integer showProcessing;
    /**
     * dataId参数序号
     */
    @Column(name = "data_id_arg_count")
    private Integer dataIdArgCount;
    /**
     * 是否删除
     */
    @Column(name = "if_del")
    private Integer ifDel;
    /**
     * 状态
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 备注
     */
    @Column(name = "remark")
    @Length(message = "remark超出最大长度255限制", max = 255)
    private String remark;
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
    /**
     * 创建人
     */
    @Column(name = "create_user")
    private Integer createUser;
    /**
     * 修改人
     */
    @Column(name = "modify_user")
    private Integer modifyUser;
    /**
     * 是否日志详情
     */
    @Column(name = "if_log_detail")
    private Integer ifLogDetail;
    /**
     * 是否进行告警提示
     */
    @Column(name = "if_remind")
    private Integer ifRemind;
}