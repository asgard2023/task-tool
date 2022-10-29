package cn.org.opendfl.task.controller;

import cn.org.opendfl.task.biz.ITaDataMethodBiz;
import cn.org.opendfl.task.biz.ITaMethodCountBiz;
import cn.org.opendfl.task.biz.ITaMethodCountSourceBiz;
import cn.org.opendfl.task.po.TaDataMethodPo;
import cn.org.opendfl.task.po.TaMethodCountPo;
import cn.org.opendfl.task.po.TaMethodCountSourcePo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ccs.opendfl.base.BaseController;
import org.ccs.opendfl.base.MyPageInfo;
import org.ccs.opendfl.base.PageVO;
import org.ccs.opendfl.exception.ResultData;
import org.ccs.opendfl.exception.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenjh
 * @Version V1.0
 * ta_method_count_source Controller
 * @Date: 2022年10月15日 下午9:41:27
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
@Api(tags = "ta_method_count_source接口")
@RestController
@RequestMapping("task/taMethodCountSource")
public class TaMethodCountSourceController extends BaseController {

    static Logger logger = LoggerFactory.getLogger(TaMethodCountSourceController.class);

    @Autowired
    private ITaMethodCountSourceBiz taMethodCountSourceBiz;
    @Autowired
    private ITaMethodCountBiz taMethodCountBiz;

    @Autowired
    private ITaDataMethodBiz taDataMethodBiz;

    /**
     * ta_method_count_source列表查询
     *
     * @param request  请求req
     * @param entity   ta_method_count_source对象
     * @param pageInfo 翻页对象
     * @return MyPageInfo 带翻页的数据集
     * @author chenjh
     * @date 2022年10月15日 下午9:41:27
     */
    @ApiOperation(value = "ta_method_count_source列表", notes = "ta_method_count_source列表翻页查询")
    @RequestMapping(value = "list", method = {RequestMethod.POST, RequestMethod.GET})
    public MyPageInfo<TaMethodCountSourcePo> queryPage(HttpServletRequest request, TaMethodCountSourcePo entity, MyPageInfo<TaMethodCountSourcePo> pageInfo) {
        if (entity == null) {
            entity = new TaMethodCountSourcePo();
        }
        if (pageInfo.getPageSize() == 0) {
            pageInfo.setPageSize(getPageSize());
        }
        pageInfo = taMethodCountSourceBiz.findPageBy(entity, pageInfo, this.createAllParams(request));

        showDataIdInfo(pageInfo);

        return pageInfo;
    }

    private void showDataIdInfo(MyPageInfo<TaMethodCountSourcePo> pageInfo) {
        List<TaMethodCountSourcePo> list = pageInfo.getList();
        List<Integer> methodCountIdList = list.stream().map(TaMethodCountSourcePo::getMethodCountId).collect(Collectors.toList());
        List<TaMethodCountPo> methodCountPos = this.taMethodCountBiz.getDataByIds(methodCountIdList, "createTime,modifyTime");

        List<Integer> methodIdList = methodCountPos.stream().map(TaMethodCountPo::getDataMethodId).collect(Collectors.toList());
        List<TaDataMethodPo> methodList = taDataMethodBiz.getDataByIds(methodIdList, "createTime,modifyTime");
        for (TaMethodCountPo methodCountPo : methodCountPos) {
            for (TaDataMethodPo methodPo : methodList) {
                if (methodCountPo.getDataMethodId().intValue() == methodPo.getId().intValue()) {
                    methodCountPo.setDataMethod(methodPo);
                    break;
                }
            }
        }

        for (TaMethodCountSourcePo countSourcePo : list) {
            for (TaMethodCountPo methodCountPo : methodCountPos) {
                if (countSourcePo.getMethodCountId().intValue() == methodCountPo.getId().intValue()) {
                    countSourcePo.setMethodCount(methodCountPo);
                    break;
                }
            }
        }
    }

    @ApiOperation(value = "ta_method_count_source列表(easyui)", notes = "ta_method_count_source列表翻页查询，用于兼容easyui的rows方式")
    @RequestMapping(value = "/list2", method = {RequestMethod.POST, RequestMethod.GET})
    public PageVO<TaMethodCountSourcePo> findByPage(HttpServletRequest request, TaMethodCountSourcePo entity, MyPageInfo<TaMethodCountSourcePo> pageInfo) {
        logger.debug("-------findByPage-------");
        this.pageSortBy(pageInfo);
        pageInfo = queryPage(request, entity, pageInfo);
        return new PageVO<>(pageInfo);
    }

    /**
     * ta_method_count_source 新增
     *
     * @param request 请求req
     * @param entity  ta_method_count_source对象
     * @return ResultData 返回数据
     * @author chenjh
     * @date 2022年10月15日 下午9:41:27
     */
    @ApiOperation(value = "添加ta_method_count_source", notes = "添加一个ta_method_count_source")
    @RequestMapping(value = "save", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultData edit(TaMethodCountSourcePo entity, HttpServletRequest request) {
        if (entity.getId() != null && entity.getId() > 0) {
            return update(entity, request);
        }
//		entity.setUpdateUser(getCurrentUserId());
//		entity.setCreateUser(getCurrentUserId());
        taMethodCountSourceBiz.saveTaMethodCountSource(entity);
        return ResultData.success();
    }

    /**
     * ta_method_count_source 更新
     *
     * @param request 请求req
     * @param entity  ta_method_count_source对象
     * @return ResultData 返回数据
     * @author chenjh
     * @date 2022年10月15日 下午9:41:27
     */
    @ApiOperation(value = "修改ta_method_count_source", notes = "根据传入的角色信息修改")
    @RequestMapping(value = "update", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultData update(TaMethodCountSourcePo entity, HttpServletRequest request) {
//		entity.setUpdateUser(getCurrentUserId());
        int v = taMethodCountSourceBiz.updateTaMethodCountSource(entity);
        return ResultData.success(v);
    }

    /**
     * ta_method_count_source 删除
     *
     * @param request 请求req
     * @param id      ta_method_count_sourceID
     * @return ResultData 返回数据
     * @author chenjh
     * @date 2022年10月15日 下午9:41:27
     */
    @ApiOperation(value = "删除ta_method_count_source ", notes = "根据传入id进行删除状态修改(即软删除)")
    @RequestMapping(value = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultData delete(@RequestParam(name = "id", required = false) Integer id, HttpServletRequest request) {
        ValidateUtils.notNull(id, "id不能为空");
        String remark = request.getParameter("remark");
        int v = taMethodCountSourceBiz.deleteTaMethodCountSource(id, this.getCurrentUserId(), remark);
        return ResultData.success(v);
    }
}