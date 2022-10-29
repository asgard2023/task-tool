package cn.org.opendfl.task.biz;

import cn.org.opendfl.task.po.TaMethodCountPo;
import cn.org.opendfl.task.vo.MethodCountStatisticVo;
import cn.org.opendfl.task.vo.MethodCountVo;
import cn.org.opendfl.tasktool.task.TaskCountVo;
import org.ccs.opendfl.base.IBaseService;
import org.ccs.opendfl.base.MyPageInfo;

import java.util.Date;
import java.util.List;

/**
 * @author chenjh
 * @Version V1.0
 * 任务运行次数统计记录 业务接口
 * @Date: 2022年10月15日 下午8:15:58
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
public interface ITaMethodCountBiz extends IBaseService<TaMethodCountPo> {
    public TaMethodCountPo getDataById(Integer id);

    public TaMethodCountPo getDataById(Integer id, String ignoreFields);

    public List<TaMethodCountPo> getDataByIds(List<Integer> ids, String ignoreFields);

    public TaMethodCountPo getDataByIdByProperties(Integer id, String properties);

    public TaMethodCountPo getMethodCountByTimeType(String timeType, Integer timeValue, Integer dataMethodId, Integer timeSeconds, Date date);

    /**
     * 任务运行次数统计记录 保存
     *
     * @param entity
     * @return Integer
     * @author chenjh
     * @date 2022年10月15日 下午8:15:58
     */
    Integer saveTaMethodCount(TaMethodCountPo entity);

    /**
     * 任务运行次数统计记录 更新
     *
     * @param entity
     * @return Integer
     * @author chenjh
     * @date 2022年10月15日 下午8:15:58
     */
    Integer updateTaMethodCount(TaMethodCountPo entity);

    /**
     * 任务运行次数统计记录 删除
     *
     * @param id       主键ID
     * @param operUser 操作人
     * @param remark   备注
     * @return Integer
     * @author chenjh
     * @date 2022年10月15日 下午8:15:58
     */
    Integer deleteTaMethodCount(Integer id, Integer operUser, String remark);

    Integer autoSave(String timeType, Integer timeValue, Integer dataMethodId, int timeSeconds, Date date);

    int updateTaskRunCount(Integer id, TaskCountVo taskCountVo, Date date);

    public int updateTaskErrorInfo(Integer id, TaskCountVo taskCountVo, Date date);

    public int updateTaskMaxRunTime(Integer id, TaskCountVo taskCountVo, Date date);
}