package cn.org.opendfl.task.mapper;

import cn.org.opendfl.task.po.TaMethodCountPo;
import cn.org.opendfl.tasktool.task.TaskCountVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Version V1.0
 * @Description: 任务运行次数统计记录 Mapper
 * @author chenjh
 * @Date: 2022年10月15日 下午8:15:58
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
public interface TaMethodCountMapper extends Mapper<TaMethodCountPo> {
    @Update("update ta_method_count set run_count=run_count+#{dataCount.runCount}, run_time=#{dataCount.runTime}, run_time_date=#{dataCount.runTimeDate}" +
            ", run_server=#{dataCount.runServer}" +
            " where id=#{id} and if_del=0")
    int updateTaskRunCount(@Param("id") Integer id, @Param("dataCount") TaMethodCountPo dataCount);

    @Update("update ta_method_count set error_count=error_count+#{dataCount.errorCount}, error_newly_time=#{dataCount.errorNewlyTime}" +
            ", error_newly_info=#{dataCount.errorNewlyInfo}, error_newly_server=#{dataCount.errorNewlyServer}, error_newly_data_id=#{dataCount.errorNewlyDataId}" +
            " where id=#{id} and if_del=0 and (error_newly_time is null or error_newly_time<#{dataCount.errorNewlyTime})")
    int updateTaskErrorInfo(@Param("id") Integer id, @Param("dataCount") TaMethodCountPo dataCount);
}