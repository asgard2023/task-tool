package cn.org.opendfl.task.biz.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.task.biz.ITaDataMethodBiz;
import cn.org.opendfl.task.mapper.TaDataMethodMapper;
import cn.org.opendfl.task.mapper.TaDataMethodMyMapper;
import cn.org.opendfl.task.po.TaDataMethodPo;
import cn.org.opendfl.tasktool.task.TaskCountVo;
import com.github.pagehelper.PageHelper;
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
 * ta_data_method 业务实现
 * @Date: 2022年10月15日 下午8:16:35
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
@Service(value = "taDataMethodBiz")
public class TaDataMethodBiz extends BaseService<TaDataMethodPo> implements ITaDataMethodBiz {
    @Resource
    private TaDataMethodMapper mapper;

    @Resource
    private TaDataMethodMyMapper myMapper;

    static Logger logger = LoggerFactory.getLogger(TaDataMethodBiz.class);

    @Override
    public Mapper<TaDataMethodPo> getMapper() {
        return mapper;
    }

    public TaDataMethodPo getDataById(Integer id) {
        return getDataById(id, null);
    }

    /**
     * 按ID查数据
     *
     * @param id           数据id
     * @param ignoreFields 支持忽略属性，例如：ignoreFields=ifDel,createTime,createUser将不返回这些属性
     * @return 数据
     */
    public TaDataMethodPo getDataById(Integer id, String ignoreFields) {
        if (id == null || id == 0) {
            return null;
        }
        List<TaDataMethodPo> list = getDataByIds(Arrays.asList(id), ignoreFields);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public List<TaDataMethodPo> getDataByIds(List<Integer> ids, String ignoreFields) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        Example example = new Example(TaDataMethodPo.class);
        if (CharSequenceUtil.isNotBlank(ignoreFields)) {
            String props = BeanUtils.getAllProperties(TaDataMethodPo.class, ignoreFields);
            example.selectProperties(props.split(","));
        }
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids);
        return this.mapper.selectByExample(example);
    }

    @Override
    public Example createConditions(TaDataMethodPo entity, Map<String, Object> otherParams) {
        Example example = new Example(entity.getClass());
        Example.Criteria criteria = example.createCriteria();
        searchCondition(entity, otherParams, criteria);
        addFilters(criteria, otherParams);
        return example;
    }

    private void searchCondition(TaDataMethodPo entity, Map<String, Object> otherParams, Example.Criteria criteria) {
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
        this.addEqualByKey(criteria, "code", otherParams);
        this.addEqualByKey(criteria, "name", otherParams);
        this.addEqualByKey(criteria, "category", otherParams);
        this.addEqualByKey(criteria, "ifLogDetail", otherParams);
        this.addEqualByKey(criteria, "ifRemind", otherParams);
    }

    @Override
    public MyPageInfo<TaDataMethodPo> findPageBy(TaDataMethodPo entity, MyPageInfo<TaDataMethodPo> pageInfo, Map<String, Object> otherParams) {
        if (entity == null) {
            entity = new TaDataMethodPo();
        }
        Example example = createConditions(entity, otherParams);
        if (StringUtil.isNotEmpty(pageInfo.getOrderBy()) && StringUtil.isNotEmpty(pageInfo.getOrder())) {
            example.setOrderByClause(StringUtil.camelhumpToUnderline(pageInfo.getOrderBy()) + " " + pageInfo.getOrder());
        }
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<TaDataMethodPo> list = this.getMapper().selectByExample(example);
        return new MyPageInfo<>(list);
    }

    @Override
    public Integer saveTaDataMethod(TaDataMethodPo entity) {
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
    public Integer updateTaDataMethod(TaDataMethodPo entity) {
        entity.setModifyTime(new Date());
        if (entity.getIfDel() == null) {
            entity.setIfDel(0);
        }
        return this.updateByPrimaryKeySelective(entity);

    }

    @Override
    public Integer deleteTaDataMethod(Integer id, Integer operUser, String remark) {
        TaDataMethodPo po = new TaDataMethodPo();
        po.setId(id);
        po.setIfDel(1); // 0未删除,1已删除
        po.setModifyUser(operUser);
        po.setRemark(remark);
        po.setModifyTime(new Date());
        return this.updateByPrimaryKeySelective(po);
    }

    public TaDataMethodPo findTaDataMethodByCode(String code) {
        TaDataMethodPo search = new TaDataMethodPo();
        search.setCode(code);
        search.setIfDel(0);
        List<TaDataMethodPo> list = this.findBy(search);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public Integer autoSave(String methodCode, TaskCountVo taskCountVo) {
        TaDataMethodPo exist = this.findTaDataMethodByCode(methodCode);
        if (exist == null) {
            TaDataMethodPo entity = new TaDataMethodPo();
            entity.setIfDel(0);
            entity.setStatus(1);
            entity.setCode(methodCode);
            entity.setDataIdArgCount(taskCountVo.getTaskCompute().getDataIdArgCount());
            entity.setCategory(taskCountVo.getTaskCompute().getCategory());
            Integer ifShowProcessing = 0;
            if (taskCountVo.getTaskCompute().isShowProcessing()) {
                ifShowProcessing = 1;
            }
            entity.setShowProcessing(ifShowProcessing);
            entity.setCreateTime(new Date());
            this.myMapper.insertUseGeneratedKeys(entity);
            return entity.getId();
        }
        return exist.getId();
    }
}