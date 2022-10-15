package cn.org.opendfl.task.biz.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.task.biz.ITaMethodCountSourceBiz;
import cn.org.opendfl.task.mapper.TaMethodCountSourceMapper;
import cn.org.opendfl.task.po.TaMethodCountSourcePo;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Version V1.0
 * ta_method_count_source 业务实现
 * @author: chenjh
 * @Date: 2022年10月15日 下午9:41:27
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
@Service(value = "taMethodCountSourceBiz")
public class TaMethodCountSourceBiz extends BaseService<TaMethodCountSourcePo> implements ITaMethodCountSourceBiz {
    @Resource
    private TaMethodCountSourceMapper mapper;

    static Logger logger = LoggerFactory.getLogger(TaMethodCountSourceBiz.class);

    @Override
    public Mapper<TaMethodCountSourcePo> getMapper() {
        return mapper;
    }

    public TaMethodCountSourcePo getDataById(Integer id) {
        return getDataById(id, null);
    }

    /**
     * 按ID查数据
     *
     * @param id           数据id
     * @param ignoreFields 支持忽略属性，例如：ignoreFields=ifDel,createTime,createUser将不返回这些属性
     * @return 数据
     */
    public TaMethodCountSourcePo getDataById(Integer id, String ignoreFields) {
        if (id == null || id == 0) {
            return null;
        }
        Example example = new Example(TaMethodCountSourcePo.class);
        if (CharSequenceUtil.isNotBlank(ignoreFields)) {
            String props = BeanUtils.getAllProperties(TaMethodCountSourcePo.class, ignoreFields);
            example.selectProperties(props.split(","));
        }
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        return this.mapper.selectOneByExample(example);
    }

    @Override
    public Example createConditions(TaMethodCountSourcePo entity, Map<String, Object> otherParams) {
        Example example = new Example(entity.getClass());
        Example.Criteria criteria = example.createCriteria();
        searchCondition(entity, otherParams, criteria);
        addFilters(criteria, otherParams);
        return example;
    }

    private void searchCondition(TaMethodCountSourcePo entity, Map<String, Object> otherParams, Example.Criteria criteria) {
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
        this.addEqualByKey(criteria, "methodCountId", otherParams);
        this.addEqualByKey(criteria, "source", otherParams);
    }

    @Override
    public MyPageInfo<TaMethodCountSourcePo> findPageBy(TaMethodCountSourcePo entity, MyPageInfo<TaMethodCountSourcePo> pageInfo, Map<String, Object> otherParams) {
        if (entity == null) {
            entity = new TaMethodCountSourcePo();
        }
        Example example = createConditions(entity, otherParams);
        if (StringUtil.isNotEmpty(pageInfo.getOrderBy()) && StringUtil.isNotEmpty(pageInfo.getOrder())) {
            example.setOrderByClause(StringUtil.camelhumpToUnderline(pageInfo.getOrderBy()) + " " + pageInfo.getOrder());
        }
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<TaMethodCountSourcePo> list = this.getMapper().selectByExample(example);
        return new MyPageInfo<>(list);
    }

    @Override
    public Integer saveTaMethodCountSource(TaMethodCountSourcePo entity) {
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
    public Integer updateTaMethodCountSource(TaMethodCountSourcePo entity) {
        entity.setModifyTime(new Date());
        if (entity.getIfDel() == null) {
            entity.setIfDel(0);
        }
        return this.updateByPrimaryKeySelective(entity);

    }

    @Override
    public Integer deleteTaMethodCountSource(Integer id, Integer operUser, String remark) {
        TaMethodCountSourcePo po = new TaMethodCountSourcePo();
        po.setId(id);
        po.setIfDel(1); // 0未删除,1已删除
//        po.setUpdateUser(operUser);
//        po.setRemark(remark);
        po.setModifyTime(new Date());
        return this.updateByPrimaryKeySelective(po);
    }
}