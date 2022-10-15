package cn.org.opendfl.task.biz;

import cn.org.opendfl.task.po.TaDataMethodPo;
import org.ccs.opendfl.base.IBaseService;


/**
 * @Version V1.0
 * ta_data_method 业务接口
 * @author: chenjh
 * @Date: 2022年10月15日 下午8:16:35
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
public interface ITaDataMethodBiz extends IBaseService<TaDataMethodPo> {
    public TaDataMethodPo getDataById(Integer id);

    public TaDataMethodPo getDataById(Integer id, String ignoreFields);

    /**
     * ta_data_method 保存
     *
     * @param entity
     * @return Integer
     * @author chenjh
     * @date 2022年10月15日 下午8:16:35
     */
    Integer saveTaDataMethod(TaDataMethodPo entity);

    /**
     * ta_data_method 更新
     *
     * @param entity
     * @return Integer
     * @author chenjh
     * @date 2022年10月15日 下午8:16:35
     */
    Integer updateTaDataMethod(TaDataMethodPo entity);

    /**
     * ta_data_method 删除
     *
     * @param id       主键ID
     * @param operUser 操作人
     * @param remark   备注
     * @return Integer
     * @author chenjh
     * @date 2022年10月15日 下午8:16:35
     */
    Integer deleteTaDataMethod(Integer id, Integer operUser, String remark);
}