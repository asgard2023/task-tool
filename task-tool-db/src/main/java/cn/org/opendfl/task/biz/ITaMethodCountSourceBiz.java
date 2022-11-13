package cn.org.opendfl.task.biz;

import cn.org.opendfl.base.IBaseService;
import cn.org.opendfl.task.po.TaMethodCountSourcePo;

/**
 * @author chenjh
 * @Version V1.0
 * ta_method_count_source 业务接口
 * @Date: 2022年10月15日 下午9:41:27
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
public interface ITaMethodCountSourceBiz extends IBaseService<TaMethodCountSourcePo> {
    public TaMethodCountSourcePo getDataById(Integer id);

    public TaMethodCountSourcePo getDataById(Integer id, String ignoreFields);

    public TaMethodCountSourcePo getDataBySource(Integer methodCountId, String source);

    /**
     * ta_method_count_source 保存
     *
     * @param entity
     * @return Integer
     * @author chenjh
     * @date 2022年10月15日 下午9:41:27
     */
    Integer saveTaMethodCountSource(TaMethodCountSourcePo entity);

    /**
     * ta_method_count_source 更新
     *
     * @param entity
     * @return Integer
     * @author chenjh
     * @date 2022年10月15日 下午9:41:27
     */
    Integer updateTaMethodCountSource(TaMethodCountSourcePo entity);

    /**
     * ta_method_count_source 删除
     *
     * @param id       主键ID
     * @param operUser 操作人
     * @param remark   备注
     * @return Integer
     * @author chenjh
     * @date 2022年10月15日 下午9:41:27
     */
    Integer deleteTaMethodCountSource(Integer id, Integer operUser, String remark);

    public Integer updateTaskRunCountSource(Integer id, Integer runCount);

    public Integer autoSave(Integer methodCountId, String source);
}