package cn.org.opendfl.task.biz;

import cn.org.opendfl.base.MyPageInfo;
import cn.org.opendfl.task.vo.MethodCountStatisticVo;
import cn.org.opendfl.task.vo.MethodCountVo;
import cn.org.opendfl.task.vo.MethodRunTimeVo;
import cn.org.opendfl.task.vo.MethodTimeValueCountVo;

import java.util.Date;

public interface ITaMethodCountReportBiz {

    public MyPageInfo<MethodCountStatisticVo> getMethodCountStatistic(Integer dataMethodId, String timeType, Date startTime, Date endTime, MyPageInfo<MethodCountStatisticVo> pageInfo);

    /**
     * 统计接口调用次数最多的接口
     * 按createTime查时间
     *
     * @param timeType  时间类型
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageInfo  翻页
     * @return
     */
    public MyPageInfo<MethodCountVo> reportMaxRunCount(String timeType, Date startTime, Date endTime, MyPageInfo<MethodCountVo> pageInfo);

    /**
     * 统计接口异常次数最多的接口
     * 按errorNewlyTime查时间
     *
     * @param timeType  时间类型
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageInfo  翻页
     * @return
     */
    public MyPageInfo<MethodCountVo> reportMaxErrorCount(String timeType, Date startTime, Date endTime, MyPageInfo<MethodCountVo> pageInfo);

    /**
     * 统计最大运行时间的接口
     * 按createTime查时间
     *
     * @param timeType  时间类型
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageInfo  翻页
     * @return
     */
    public MyPageInfo<MethodRunTimeVo> reportMaxRunTime(String timeType, Date startTime, Date endTime, MyPageInfo<MethodRunTimeVo> pageInfo);

    public MyPageInfo<MethodRunTimeVo> reportAvgMaxRunTime(String timeType, Date startTime, Date endTime, MyPageInfo<MethodRunTimeVo> pageInfo);


    public MyPageInfo<MethodTimeValueCountVo> reportTimeValueRunCount(String timeType, Date startTime, Date endTime, MyPageInfo<MethodTimeValueCountVo> pageInfo);
}
