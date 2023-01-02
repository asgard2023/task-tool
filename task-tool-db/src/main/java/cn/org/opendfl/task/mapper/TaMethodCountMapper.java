package cn.org.opendfl.task.mapper;

import cn.org.opendfl.task.po.TaMethodCountPo;
import cn.org.opendfl.task.vo.MethodCountStatisticVo;
import cn.org.opendfl.task.vo.MethodCountVo;
import cn.org.opendfl.task.vo.MethodRunTimeVo;
import cn.org.opendfl.task.vo.MethodTimeValueCountVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @author chenjh
 * @Version V1.0
 * @Description: 任务运行次数统计记录 Mapper
 * @Date: 2022年10月15日 下午8:15:58
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
public interface TaMethodCountMapper extends Mapper<TaMethodCountPo> {
    @Update("update ta_method_count set run_count=run_count+#{dataCount.runCount}, run_uid=#{dataCount.runUid}, run_time=#{dataCount.runTime}, run_time_date=#{dataCount.runTimeDate}" +
            ", run_server=#{dataCount.runServer}" +
            " where id=#{id} and if_del=0")
    int updateTaskRunCount(@Param("id") Integer id, @Param("dataCount") TaMethodCountPo dataCount);

    @Update("update ta_method_count set error_count=error_count+#{dataCount.errorCount}, error_newly_uid=#{dataCount.errorNewlyUid}, error_newly_time=#{dataCount.errorNewlyTime}" +
            ", error_newly_info=#{dataCount.errorNewlyInfo}, error_newly_server=#{dataCount.errorNewlyServer}, error_newly_data_id=#{dataCount.errorNewlyDataId}" +
            " where id=#{id} and if_del=0 and (error_newly_time is null or error_newly_time<#{dataCount.errorNewlyTime})")
    int updateTaskErrorInfo(@Param("id") Integer id, @Param("dataCount") TaMethodCountPo dataCount);

    @Select("select data_method_id dataMethodId, time_type timeType, count(*) rowCount, min(time_value) timeValueMin, max(time_value) timeValueMax, sum(run_count) runCountTotal" +
            ", max(run_time) maxRunTime, min(create_time) minDate, max(run_time_date) maxDate , sum(error_count) errorCountTotal" +
            ", min(error_newly_time) minErrorDate, max(error_newly_time) maxErrorDate " +
            " from ta_method_count where if_del=0" +
            " and (data_method_id=#{dataMethodId} or #{dataMethodId} is null)" +
            " and (time_type=#{timeType} or #{timeType} is null)" +
            " and create_time>=#{startTime}" +
            " and create_time<#{endTime}" +
            " GROUP BY data_method_id, time_type")
    List<MethodCountStatisticVo> getMethodCountStatistic(@Param("dataMethodId") Integer dataMethodId, @Param("timeType") String timeType, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select data_method_id dataMethodId, sum(run_count) runCountTotal, count(*) rowCount from ta_method_count" +
            " where time_type=#{timeType}" +
            " and create_time>=#{startTime}" +
            " and create_time<#{endTime}" +
            " GROUP BY data_method_id" +
            " order by runCountTotal desc")
    public List<MethodCountVo> reportMaxRunCount(@Param("timeType") String timeType, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select data_method_id dataMethodId, sum(error_count) runCountTotal, count(*) rowCount from ta_method_count" +
            " where time_type=#{timeType}" +
            " and error_newly_time>=#{startTime}" +
            " and error_newly_time<#{endTime}" +
            " GROUP BY data_method_id" +
            " order by runCountTotal desc")
    public List<MethodCountVo> reportMaxErrorCount(@Param("timeType") String timeType, @Param("startTime") Date startTime, @Param("endTime") Date endTime);


    @Select("select data_method_id dataMethodId, max(max_run_time) maxRunTime from ta_method_count" +
            " where time_type=#{timeType}" +
            " and create_time>=#{startTime}" +
            " and create_time<#{endTime}" +
            " GROUP BY data_method_id" +
            " order by maxRunTime desc")
    public List<MethodRunTimeVo> reportMaxRunTime(@Param("timeType") String timeType, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
  @Select("select data_method_id dataMethodId, avg(max_run_time) maxRunTime from ta_method_count" +
            " where time_type=#{timeType}" +
            " and create_time>=#{startTime}" +
            " and create_time<#{endTime}" +
            " GROUP BY data_method_id" +
            " order by maxRunTime desc")
    public List<MethodRunTimeVo> reportAvgMaxRunTime(@Param("timeType") String timeType, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("select data_method_id dataMethodId, time_value timeValue, run_count runCount, max_run_time maxRunTime, error_count errorCount, 1 rowCount from ta_method_count" +
            " where time_type=#{timeType}" +
            " and create_time>=#{startTime}" +
            " and create_time<#{endTime} order by data_method_id, time_value, run_count desc")
    public List<MethodTimeValueCountVo> reportTimeValueRunCount(@Param("timeType") String timeType, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}