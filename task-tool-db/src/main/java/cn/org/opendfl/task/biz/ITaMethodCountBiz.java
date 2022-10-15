package cn.org.opendfl.task.biz;

import cn.org.opendfl.task.po.TaMethodCountPo;

import org.ccs.opendfl.base.IBaseService;

/**
 *
 * @Version V1.0
 *  任务运行次数统计记录 业务接口
 * @author: chenjh
 * @Date: 2022年10月15日 下午8:15:58
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
*/
public interface ITaMethodCountBiz extends IBaseService<TaMethodCountPo> {
    public TaMethodCountPo getDataById(Integer id);

    public TaMethodCountPo getDataById(Integer id, String ignoreFields);

    /**
     * 任务运行次数统计记录 保存
     * @author chenjh
     * @date 2022年10月15日 下午8:15:58
     * @param entity
     * @return Integer
    */
    Integer saveTaMethodCount(TaMethodCountPo entity);

    /**
     * 任务运行次数统计记录 更新
     * @param entity
     * @return Integer
     * @author chenjh
     * @date 2022年10月15日 下午8:15:58
    */
    Integer updateTaMethodCount(TaMethodCountPo entity);

    /**
     * 任务运行次数统计记录 删除
     * @param id 主键ID
     * @param operUser 操作人
     * @param remark 备注
     * @return Integer
     * @author chenjh
     * @date 2022年10月15日 下午8:15:58
    */
    Integer deleteTaMethodCount(Integer id, Integer operUser, String remark);
}