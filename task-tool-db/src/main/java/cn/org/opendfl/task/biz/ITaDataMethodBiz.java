package cn.org.opendfl.task.biz;

import cn.org.opendfl.task.po.TaDataMethodPo;
import cn.org.opendfl.tasktool.task.TaskCountVo;
import org.ccs.opendfl.base.IBaseService;


/**
 * @author chenjh
 * @Version V1.0
 * ta_data_method 业务接口
 * @Date: 2022年10月15日 下午8:16:35
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
public interface ITaDataMethodBiz extends IBaseService<TaDataMethodPo> {
    /**
     * 按ID查数据
     *
     * @param id 数据id
     * @return 据对象
     */
    public TaDataMethodPo getDataById(Integer id);

    /**
     * 按ID查数据
     *
     * @param id           数据id
     * @param ignoreFields 忽略的属性
     * @return 据对象
     */
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

    Integer autoSave(String methodCode, TaskCountVo taskCountVo);
}