package cn.org.opendfl.task.biz.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.task.biz.ITaMethodCountBiz;
import cn.org.opendfl.task.mapper.TaMethodCountMapper;
import cn.org.opendfl.task.mapper.TaMethodCountMyMapper;
import cn.org.opendfl.task.po.TaDataMethodPo;
import cn.org.opendfl.task.po.TaMethodCountPo;
import cn.org.opendfl.task.vo.MethodCountStatisticVo;
import cn.org.opendfl.task.vo.MethodCountVo;
import cn.org.opendfl.task.vo.MethodRunTimeVo;
import cn.org.opendfl.tasktool.task.TaskCountVo;
import cn.org.opendfl.tasktool.utils.InetAddressUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.ccs.opendfl.base.BaseService;
import org.ccs.opendfl.base.BeanUtils;
import org.ccs.opendfl.base.MyPageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author chenjh
 * @Version V1.0
 * 任务运行次数统计记录 业务实现
 * @Date: 2022年10月15日 下午8:15:58
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
@Service(value = "taMethodCountBiz")
public class TaMethodCountBiz extends BaseService<TaMethodCountPo> implements ITaMethodCountBiz {
    @Resource
    private TaMethodCountMapper mapper;

    @Resource
    private TaMethodCountMyMapper myMapper;

    static Logger logger = LoggerFactory.getLogger(TaMethodCountBiz.class);

    @Override
    public Mapper<TaMethodCountPo> getMapper() {
        return mapper;
    }

    public TaMethodCountPo getDataById(Integer id) {
        return getDataById(id, null);
    }

    /**
     * 按ID查数据
     *
     * @param id           数据id
     * @param ignoreFields 支持忽略属性，例如：ignoreFields=ifDel,createTime,createUser将不返回这些属性
     * @return 数据
     */
    public TaMethodCountPo getDataById(Integer id, String ignoreFields) {
        if (id == null || id == 0) {
            return null;
        }
        List<TaMethodCountPo> list = getDataByIds(Arrays.asList(id), ignoreFields);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }


    public List<TaMethodCountPo> getDataByIds(List<Integer> ids, String ignoreFields) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        Example example = new Example(TaMethodCountPo.class);
        if (CharSequenceUtil.isNotBlank(ignoreFields)) {
            String props = BeanUtils.getAllProperties(TaMethodCountPo.class, ignoreFields);
            example.selectProperties(props.split(","));
        }
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids);
        return this.mapper.selectByExample(example);
    }

    public TaMethodCountPo getDataByIdByProperties(Integer id, String properties) {
        if (id == null || id == 0) {
            return null;
        }
        Example example = new Example(TaMethodCountPo.class);
        if (CharSequenceUtil.isNotBlank(properties)) {
            example.selectProperties(properties.split(","));
        }
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        return this.mapper.selectOneByExample(example);
    }

    public TaMethodCountPo getMethodCountByTimeType(String timeType, Integer timeValue, Integer dataMethodId, Integer timeSeconds, Date date) {
        Example example = new Example(TaMethodCountPo.class);
        example.selectProperties("id,dataMethodId,timeSeconds,timeType,timeValue,createTime".split(","));
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("timeType", timeType);
        criteria.andEqualTo("timeValue", timeValue);
        criteria.andEqualTo("dataMethodId", dataMethodId);
        criteria.andEqualTo("timeSeconds", timeSeconds);
        criteria.andEqualTo("ifDel", 0);

        //用于查两个间隔期内数据唯一
        if (timeSeconds > 0) {
            criteria.andGreaterThanOrEqualTo("createTime", DateUtils.addSeconds(date, -timeSeconds * 2));
        }
        List<TaMethodCountPo> list = this.mapper.selectByExample(example);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Example createConditions(TaMethodCountPo entity, Map<String, Object> otherParams) {
        Example example = new Example(entity.getClass());
        Example.Criteria criteria = example.createCriteria();
        searchCondition(entity, otherParams, criteria);
        addFilters(criteria, otherParams);
        return example;
    }

    private void searchCondition(TaMethodCountPo entity, Map<String, Object> otherParams, Example.Criteria criteria) {
        String startTime = (String) otherParams.get("startTime");
        if (StringUtil.isNotEmpty(startTime)) {
            criteria.andGreaterThanOrEqualTo("createTime", startTime);
        }
        String endTime = (String) otherParams.get("endTime");
        if (StringUtil.isNotEmpty(endTime)) {
            criteria.andLessThanOrEqualTo("createTime", endTime);
        }

        if (entity.getIfDel() != null) {
            criteria.andEqualTo("ifDel", entity.getIfDel());
        }
        this.addEqualByKey(criteria, "id", otherParams);
        this.addEqualByKey(criteria, "dataMethodId", otherParams);
        this.addEqualByKey(criteria, "timeValue", otherParams);
        this.addEqualByKey(criteria, "timeType", otherParams);
        TaDataMethodPo dataMethod = entity.getDataMethod();
        if (dataMethod != null && StringUtils.isNotBlank(dataMethod.getCode())) {
            criteria.andCondition(" data_method_id in (select id from ta_data_method where if_del=0 and code like '" + dataMethod.getCode() + "%')");
        }
    }

    @Override
    public MyPageInfo<TaMethodCountPo> findPageBy(TaMethodCountPo entity, MyPageInfo<TaMethodCountPo> pageInfo, Map<String, Object> otherParams) {
        if (entity == null) {
            entity = new TaMethodCountPo();
        }
        Example example = createConditions(entity, otherParams);
        if (StringUtil.isNotEmpty(pageInfo.getOrderBy()) && StringUtil.isNotEmpty(pageInfo.getOrder())) {
            example.setOrderByClause(StringUtil.camelhumpToUnderline(pageInfo.getOrderBy()) + " " + pageInfo.getOrder());
        }
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<TaMethodCountPo> list = this.getMapper().selectByExample(example);
        return new MyPageInfo<>(list);
    }

    @Override
    public Integer saveTaMethodCount(TaMethodCountPo entity) {
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(new Date());
        }
        entity.setModifyTime(new Date());
        if (entity.getIfDel() == null) {
            entity.setIfDel(0);
        }
        return this.mapper.insert(entity);
    }

    @Override
    public Integer updateTaMethodCount(TaMethodCountPo entity) {
        entity.setModifyTime(new Date());
        if (entity.getIfDel() == null) {
            entity.setIfDel(0);
        }
        return this.updateByPrimaryKeySelective(entity);

    }

    @Override
    public Integer deleteTaMethodCount(Integer id, Integer operUser, String remark) {
        TaMethodCountPo po = new TaMethodCountPo();
        po.setId(id);
        po.setIfDel(1); // 0未删除,1已删除
//        po.setUpdateUser(operUser);
//        po.setRemark(remark);
        po.setModifyTime(new Date());
        return this.updateByPrimaryKeySelective(po);
    }

    public Integer autoSave(String timeType, Integer timeValue, Integer dataMethodId, int timeSeconds, Date date) {
        TaMethodCountPo exist = this.getMethodCountByTimeType(timeType, timeValue, dataMethodId, timeSeconds, date);
        if (exist == null) {
            TaMethodCountPo entity = new TaMethodCountPo();
            entity.load(timeType, timeValue, dataMethodId, timeSeconds);
            entity.setIfDel(0);
            entity.setStatus(1);
            entity.setRunCount(0L);
            entity.setErrorCount(0);
            entity.setFirstTime(date);
            entity.setCreateTime(date);
            entity.setMaxRunTime(0);
            myMapper.insertUseGeneratedKeys(entity);
            return entity.getId();
        }
        return exist.getId();
    }

    public int updateTaskRunCount(Integer id, TaskCountVo taskCountVo, Date date) {
        int runCount = taskCountVo.getRunCounter().get();
        if (runCount == 0) {
            return 0;
        }
        TaMethodCountPo update = new TaMethodCountPo();
        update.setId(id);
        update.loadRun(taskCountVo, InetAddressUtils.getLocalHostIp());
        logger.debug("-----updateTaskRunCount--type={} runCount={}/{}/{}", taskCountVo.getCountType(), runCount, update.getRunCount(), taskCountVo.getRunCounter().get());
        return this.mapper.updateTaskRunCount(id, update);
    }

    public int updateTaskErrorInfo(Integer id, TaskCountVo taskCountVo, Date date) {
        int errorCount = taskCountVo.getErrorCounter().get();
        if (errorCount == 0) {
            return 0;
        }
        TaMethodCountPo update = new TaMethodCountPo();
        update.loadErrorNewly(taskCountVo, InetAddressUtils.getLocalHostIp());
        update.setModifyTime(date);
        return this.mapper.updateTaskErrorInfo(id, update);
    }

    public int updateTaskMaxRunTime(Integer id, TaskCountVo taskCountVo, Date date) {
        TaMethodCountPo update = new TaMethodCountPo();
        update.loadMax(taskCountVo, InetAddressUtils.getLocalHostIp());
        update.setModifyTime(date);
        Example example = new Example(TaMethodCountPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        criteria.andLessThanOrEqualTo("maxRunTime", update.getMaxRunTime());
        return this.mapper.updateByExampleSelective(update, example);
    }
}