package cn.org.opendfl.task.biz.impl;

import cn.org.opendfl.base.MyPageInfo;
import cn.org.opendfl.task.biz.ITaMethodCountReportBiz;
import cn.org.opendfl.task.mapper.TaMethodCountMapper;
import cn.org.opendfl.task.vo.MethodCountStatisticVo;
import cn.org.opendfl.task.vo.MethodCountVo;
import cn.org.opendfl.task.vo.MethodRunTimeVo;
import cn.org.opendfl.task.vo.MethodTimeValueCountVo;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service(value = "taMethodCountReportBiz")
public class TaMethodCountReportBiz implements ITaMethodCountReportBiz {
    @Resource
    private TaMethodCountMapper mapper;

    public MyPageInfo<MethodCountStatisticVo> getMethodCountStatistic(Integer dataMethodId, String timeType, Date startTime, Date endTime, MyPageInfo<MethodCountStatisticVo> pageInfo) {
        if (endTime == null) {
            endTime = new Date();
        }

        String orderBy = null;
        if (StringUtils.isNotBlank(pageInfo.getOrderBy())) {
            orderBy = pageInfo.getOrderBy() + " " + pageInfo.getOrder();
        }
        boolean isCountTotal = pageInfo.isCountTotal();
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize(), isCountTotal).setOrderBy(orderBy);
        List<MethodCountStatisticVo> list = mapper.getMethodCountStatistic(dataMethodId, timeType, startTime, endTime);
        pageInfo = new MyPageInfo(list);
        pageInfo.setCountTotal(isCountTotal);
        if (!pageInfo.isCountTotal()) {
            pageInfo.setPages(100);
        }
        return pageInfo;
    }

    public MyPageInfo<MethodCountVo> reportMaxRunCount(String timeType, Date startTime, Date endTime, MyPageInfo<MethodCountVo> pageInfo) {
        if (endTime == null) {
            endTime = new Date();
        }

        String orderBy = null;
        if (StringUtils.isNotBlank(pageInfo.getOrderBy())) {
            orderBy = pageInfo.getOrderBy() + " " + pageInfo.getOrder();
        }
        boolean isCountTotal = pageInfo.isCountTotal();
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize(), isCountTotal).setOrderBy(orderBy);
        List<MethodCountVo> list = mapper.reportMaxRunCount(timeType, startTime, endTime);
        pageInfo = new MyPageInfo(list);
        pageInfo.setCountTotal(isCountTotal);
        if (!pageInfo.isCountTotal()) {
            pageInfo.setPages(100);
        }
        return pageInfo;
    }

    public MyPageInfo<MethodCountVo> reportMaxErrorCount(String timeType, Date startTime, Date endTime, MyPageInfo<MethodCountVo> pageInfo) {
        if (endTime == null) {
            endTime = new Date();
        }

        String orderBy = null;
        if (StringUtils.isNotBlank(pageInfo.getOrderBy())) {
            orderBy = pageInfo.getOrderBy() + " " + pageInfo.getOrder();
        }
        boolean isCountTotal = pageInfo.isCountTotal();
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize(), isCountTotal).setOrderBy(orderBy);
        List<MethodCountVo> list = mapper.reportMaxErrorCount(timeType, startTime, endTime);
        pageInfo = new MyPageInfo(list);
        pageInfo.setCountTotal(isCountTotal);
        if (!pageInfo.isCountTotal()) {
            pageInfo.setPages(100);
        }
        return pageInfo;
    }

    public MyPageInfo<MethodRunTimeVo> reportMaxRunTime(String timeType, Date startTime, Date endTime, MyPageInfo<MethodRunTimeVo> pageInfo) {
        if (endTime == null) {
            endTime = new Date();
        }

        String orderBy = null;
        if (StringUtils.isNotBlank(pageInfo.getOrderBy())) {
            orderBy = pageInfo.getOrderBy() + " " + pageInfo.getOrder();
        }
        boolean isCountTotal = pageInfo.isCountTotal();
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize(), isCountTotal).setOrderBy(orderBy);
        List<MethodRunTimeVo> list = mapper.reportMaxRunTime(timeType, startTime, endTime);
        pageInfo = new MyPageInfo(list);
        pageInfo.setCountTotal(isCountTotal);
        if (!pageInfo.isCountTotal()) {
            pageInfo.setPages(100);
        }
        return pageInfo;
    }

    public MyPageInfo<MethodRunTimeVo> reportAvgMaxRunTime(String timeType, Date startTime, Date endTime, MyPageInfo<MethodRunTimeVo> pageInfo) {
        if (endTime == null) {
            endTime = new Date();
        }

        String orderBy = null;
        if (StringUtils.isNotBlank(pageInfo.getOrderBy())) {
            orderBy = pageInfo.getOrderBy() + " " + pageInfo.getOrder();
        }
        boolean isCountTotal = pageInfo.isCountTotal();
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize(), isCountTotal).setOrderBy(orderBy);
        List<MethodRunTimeVo> list = mapper.reportAvgMaxRunTime(timeType, startTime, endTime);
        pageInfo = new MyPageInfo(list);
        pageInfo.setCountTotal(isCountTotal);
        if (!pageInfo.isCountTotal()) {
            pageInfo.setPages(100);
        }
        return pageInfo;
    }

    public MyPageInfo<MethodTimeValueCountVo> reportTimeValueRunCount(String timeType, Date startTime, Date endTime, MyPageInfo<MethodTimeValueCountVo> pageInfo) {
        if (endTime == null) {
            endTime = new Date();
        }

        String orderBy = null;
        if (StringUtils.isNotBlank(pageInfo.getOrderBy())) {
            orderBy = pageInfo.getOrderBy() + " " + pageInfo.getOrder();
        }
        boolean isCountTotal = pageInfo.isCountTotal();
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize(), isCountTotal).setOrderBy(orderBy);
        List<MethodTimeValueCountVo> list = mapper.reportTimeValueRunCount(timeType, startTime, endTime);
        pageInfo = new MyPageInfo(list);
        pageInfo.setCountTotal(isCountTotal);
        if (!pageInfo.isCountTotal()) {
            pageInfo.setPages(100);
        }
        return pageInfo;
    }
}
