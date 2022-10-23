package cn.org.opendfl.task.mapper;

import cn.org.opendfl.task.po.TaMethodCountSourcePo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

/**
 * @author chenjh
 * @Version V1.0
 * @Description: ta_method_count_source Mapper
 * @Date: 2022年10月15日 下午9:41:27
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
public interface TaMethodCountSourceMapper extends Mapper<TaMethodCountSourcePo> {
    @Update("update ta_method_count_source set run_count=run_count+#{runCount},modify_time=#{modifyTime}" +
            " where id=#{id} and if_del=0")
    int updateTaskRunCountSource(@Param("id") Integer id, @Param("runCount") Integer runCount, @Param("modifyTime") Date modifyTime);
}