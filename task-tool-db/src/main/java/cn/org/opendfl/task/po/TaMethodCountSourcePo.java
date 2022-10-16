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
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @Version V1.0
 * ta_method_count_source 实体
 * @author chenjh
 * @Date: 2022年10月15日 下午9:41:27
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
@Data
@Table(name = "ta_method_count_source")
@JsonInclude(Include.ALWAYS)
public class TaMethodCountSourcePo implements Serializable {

    /**
     * id
     */
    @Id
    @Column(name = "id")
    private Integer id;
    /**
     * method_count_id
     */
    @Column(name = "method_count_id")
    private Integer methodCountId;
    /**
     * source
     */
    @Column(name = "source")
    @NotBlank(message = "source不能为空")
    @Length(message = "source超出最大长度128限制", max = 128)
    private String source;
    /**
     * run_count
     */
    @Column(name = "run_count")
    private Integer runCount;
    /**
     * if_del
     */
    @Column(name = "if_del")
    private Integer ifDel;
    /**
     * create_time
     */
    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * modify_time
     */
    @Column(name = "modify_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;
}