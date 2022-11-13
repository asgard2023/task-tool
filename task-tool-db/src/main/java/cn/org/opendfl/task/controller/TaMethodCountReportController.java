package cn.org.opendfl.task.controller;

import cn.hutool.core.date.DateUtil;
import cn.org.opendfl.base.BaseController;
import cn.org.opendfl.base.MyPageInfo;
import cn.org.opendfl.base.PageVO;
import cn.org.opendfl.exception.FailedException;
import cn.org.opendfl.task.biz.ITaDataMethodBiz;
import cn.org.opendfl.task.biz.ITaMethodCountReportBiz;
import cn.org.opendfl.task.po.TaDataMethodPo;
import cn.org.opendfl.task.vo.MethodCountStatisticVo;
import cn.org.opendfl.task.vo.MethodCountVo;
import cn.org.opendfl.task.vo.MethodRunTimeVo;
import cn.org.opendfl.task.vo.MethodTimeValueCountVo;
import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import cn.org.opendfl.tasktool.config.vo.TaskCountTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用于接口调用次数统计报表
 */
@Api(tags = "任务运行次数报表统计接口")
@RestController
@RequestMapping("task/taMethodCountReport")
public class TaMethodCountReportController extends BaseController {
    static Logger logger = LoggerFactory.getLogger(TaMethodCountReportController.class);


    @Autowired
    private ITaDataMethodBiz taDataMethodBiz;
    @Resource
    private ITaMethodCountReportBiz taMethodCountReportBiz;

    @Resource
    private TaskToolConfiguration taskToolConfiguration;

    @ApiOperation(value = "任务运行次数统计", notes = "任务运行次数统计记录列表翻页查询，用于兼容easyui的rows方式")
    @RequestMapping(value = "/methodCountStatistic", method = {RequestMethod.POST, RequestMethod.GET})
    public PageVO<MethodCountStatisticVo> methodCountStatistic(HttpServletRequest request
            , @RequestParam(value = "dataMethodCode", required = false) String dataMethodCode
            , @RequestParam(value = "timeType", required = false) String timeType
            , @RequestParam(value = "startTime", required = false) String startTime
            , @RequestParam(value = "endTime", required = false) String endTime, MyPageInfo<MethodCountStatisticVo> pageInfo) {
        logger.debug("-------methodCountStatistic-------");
        Date startTimeDate = null;
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss");
        }
        Date endimeDate = null;
        if (StringUtils.isNotBlank(endTime)) {
            endimeDate = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
        }


        Integer dataMethodId = null;
        if (StringUtils.isNotBlank(dataMethodCode)) {
            TaDataMethodPo dataMethodPo = taDataMethodBiz.findTaDataMethodByCode(dataMethodCode);
            if (dataMethodPo != null) {
                dataMethodId = dataMethodPo.getId();
            }
        }
        this.pageSortBy(pageInfo);
        pageInfo = taMethodCountReportBiz.getMethodCountStatistic(dataMethodId, timeType, startTimeDate, endimeDate, pageInfo);
        List<Integer> dataMethodIdList = pageInfo.getList().stream().map(MethodCountStatisticVo::getDataMethodId).distinct().collect(Collectors.toList());
        List<TaDataMethodPo> dataMethodPos = this.taDataMethodBiz.getDataByIds(dataMethodIdList, "createTime,modifyTime");
        pageInfo.getList().stream().forEach(t -> {
            for (TaDataMethodPo taDataMethodPo : dataMethodPos) {
                if (taDataMethodPo.getId().intValue() == t.getDataMethodId()) {
                    t.setDataMethod(taDataMethodPo);
                    break;
                }
            }
        });
        return new PageVO<>(pageInfo);
    }

    @ApiOperation(value = "统计接口调用次数最多的接口")
    @RequestMapping(value = "/reportMaxRunCount", method = {RequestMethod.POST, RequestMethod.GET})
    public PageVO<MethodCountVo> reportMaxRunCount(HttpServletRequest request
            , @RequestParam(value = "timeType", required = false) String timeType
            , @RequestParam(value = "startTime", required = false) String startTime
            , @RequestParam(value = "endTime", required = false) String endTime, MyPageInfo<MethodCountVo> pageInfo) {
        logger.debug("-------reportMaxRunCount----timeType={}", timeType);
        Date startTimeDate = null;
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss");
        }
        Date endimeDate = null;
        if (StringUtils.isNotBlank(endTime)) {
            endimeDate = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
        }


        this.pageSortBy(pageInfo);
        pageInfo = taMethodCountReportBiz.reportMaxRunCount(timeType, startTimeDate, endimeDate, pageInfo);
        showDataMethodInfo(pageInfo);
        return new PageVO<>(pageInfo);
    }

    @ApiOperation(value = "统计接口异常次数最多的接口")
    @RequestMapping(value = "/reportMaxErrorCount", method = {RequestMethod.POST, RequestMethod.GET})
    public PageVO<MethodCountVo> reportMaxErrorCount(HttpServletRequest request
            , @RequestParam(value = "timeType", required = false) String timeType
            , @RequestParam(value = "startTime", required = false) String startTime
            , @RequestParam(value = "endTime", required = false) String endTime, MyPageInfo<MethodCountVo> pageInfo) {
        logger.debug("-------reportMaxErrorCount----timeType={}", timeType);
        Date startTimeDate = null;
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss");
        }
        Date endimeDate = null;
        if (StringUtils.isNotBlank(endTime)) {
            endimeDate = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
        }


        this.pageSortBy(pageInfo);
        pageInfo = taMethodCountReportBiz.reportMaxErrorCount(timeType, startTimeDate, endimeDate, pageInfo);
        showDataMethodInfo(pageInfo);
        return new PageVO<>(pageInfo);
    }

    @ApiOperation(value = "统计接口执行时长最大的接口")
    @RequestMapping(value = "/reportMaxRunTime", method = {RequestMethod.POST, RequestMethod.GET})
    public PageVO<MethodRunTimeVo> reportMaxRunTime(HttpServletRequest request
            , @RequestParam(value = "timeType", required = false) String timeType
            , @RequestParam(value = "startTime", required = false) String startTime
            , @RequestParam(value = "endTime", required = false) String endTime, MyPageInfo<MethodRunTimeVo> pageInfo) {
        logger.debug("-------reportMaxRunTime----timeType={}", timeType);
        Date startTimeDate = null;
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss");
        }
        Date endimeDate = null;
        if (StringUtils.isNotBlank(endTime)) {
            endimeDate = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
        }


        this.pageSortBy(pageInfo);
        pageInfo = taMethodCountReportBiz.reportMaxRunTime(timeType, startTimeDate, endimeDate, pageInfo);
        showDataRunTimeMethodInfo(pageInfo);
        return new PageVO<>(pageInfo);
    }

    @ApiOperation(value = "统计接口平均最大执行时长的接口")
    @RequestMapping(value = "/reportAvgMaxRunTime", method = {RequestMethod.POST, RequestMethod.GET})
    public PageVO<MethodRunTimeVo> reportAvgMaxRunTime(HttpServletRequest request
            , @RequestParam(value = "timeType", required = false) String timeType
            , @RequestParam(value = "startTime", required = false) String startTime
            , @RequestParam(value = "endTime", required = false) String endTime, MyPageInfo<MethodRunTimeVo> pageInfo) {
        logger.debug("-------reportAvgMaxRunTime----timeType={}", timeType);
        Date startTimeDate = null;
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss");
        }
        Date endimeDate = null;
        if (StringUtils.isNotBlank(endTime)) {
            endimeDate = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
        }


        this.pageSortBy(pageInfo);
        pageInfo = taMethodCountReportBiz.reportMaxRunTime(timeType, startTimeDate, endimeDate, pageInfo);
        showDataRunTimeMethodInfo(pageInfo);
        return new PageVO<>(pageInfo);
    }

    @ApiOperation(value = "近期调用次数曲线图")
    @RequestMapping(value = "/reportTimeValueRunCount", method = {RequestMethod.POST, RequestMethod.GET})
    public PageVO<MethodTimeValueCountVo> reportTimeValueCount(HttpServletRequest request
            , @RequestParam(value = "timeType", required = false) String timeType
            , @RequestParam(value = "startTime", required = false) String startTime
            , @RequestParam(value = "timeValueCount", required = false, defaultValue = "7") Integer timeValueCount
            , @RequestParam(value = "endTime", required = false) String endTime, MyPageInfo<MethodTimeValueCountVo> pageInfo) {
        logger.debug("-------reportTimeValueCount----timeType={}", timeType);
        Date startTimeDate = null;
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss");
        }
        Date endimeDate = null;
        if (StringUtils.isNotBlank(endTime)) {
            endimeDate = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
        }

        if (timeValueCount > 0) {
            Optional<TaskCountTypeVo> opType = this.taskToolConfiguration.getCounterTimeTypes().stream().filter(t -> t.getCode().equals(timeType)).findFirst();
            if (!opType.isPresent()) {
                throw new FailedException("timeType:" + timeType + " 无效");
            }
            TaskCountTypeVo countType = opType.get();
            Date now = new Date();
            int timeSeconds = countType.getTimeSeconds();
            if (timeSeconds > 0) {
                startTimeDate = DateUtils.addSeconds(now, -countType.getTimeSeconds() * timeValueCount);
            } else {
                //total模式下的每个接口每个时间类型记录只有一条，那把开始时间多往前算10年
                startTimeDate = DateUtils.addYears(now, -10);
            }
        }


        this.pageSortBy(pageInfo);
        pageInfo = taMethodCountReportBiz.reportTimeValueRunCount(timeType, startTimeDate, endimeDate, pageInfo);
        showDataMethodCountInfo(pageInfo);
        return new PageVO<>(pageInfo);
    }

    private void showDataMethodInfo(MyPageInfo<MethodCountVo> pageInfo) {
        List<Integer> dataMethodIdList = pageInfo.getList().stream().map(MethodCountVo::getDataMethodId).distinct().collect(Collectors.toList());
        List<TaDataMethodPo> dataMethodPos = this.taDataMethodBiz.getDataByIds(dataMethodIdList, "createTime,modifyTime");
        pageInfo.getList().stream().forEach(t -> {
            for (TaDataMethodPo taDataMethodPo : dataMethodPos) {
                if (taDataMethodPo.getId().intValue() == t.getDataMethodId()) {
                    t.setDataMethod(taDataMethodPo);
                    t.setMethodCode(taDataMethodPo.getCode());
                    break;
                }
            }
        });
    }

    private void showDataRunTimeMethodInfo(MyPageInfo<MethodRunTimeVo> pageInfo) {
        List<Integer> dataMethodIdList = pageInfo.getList().stream().map(MethodRunTimeVo::getDataMethodId).distinct().collect(Collectors.toList());
        List<TaDataMethodPo> dataMethodPos = this.taDataMethodBiz.getDataByIds(dataMethodIdList, "createTime,modifyTime");
        pageInfo.getList().stream().forEach(t -> {
            for (TaDataMethodPo taDataMethodPo : dataMethodPos) {
                if (taDataMethodPo.getId().intValue() == t.getDataMethodId()) {
                    t.setDataMethod(taDataMethodPo);
                    t.setMethodCode(taDataMethodPo.getCode());
                    break;
                }
            }
        });
    }

    private void showDataMethodCountInfo(MyPageInfo<MethodTimeValueCountVo> pageInfo) {
        List<Integer> dataMethodIdList = pageInfo.getList().stream().map(MethodTimeValueCountVo::getDataMethodId).distinct().collect(Collectors.toList());
        List<TaDataMethodPo> dataMethodPos = this.taDataMethodBiz.getDataByIds(dataMethodIdList, "createTime,modifyTime");
        pageInfo.getList().stream().forEach(t -> {
            for (TaDataMethodPo taDataMethodPo : dataMethodPos) {
                if (taDataMethodPo.getId().intValue() == t.getDataMethodId()) {
                    t.setDataMethod(taDataMethodPo);
                    t.setMethodCode(taDataMethodPo.getCode());
                    break;
                }
            }
        });
    }
}
