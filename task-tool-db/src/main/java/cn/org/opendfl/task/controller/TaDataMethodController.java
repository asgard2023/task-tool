package cn.org.opendfl.task.controller;

import cn.org.opendfl.base.BaseController;
import cn.org.opendfl.base.MyPageInfo;
import cn.org.opendfl.base.PageVO;
import cn.org.opendfl.exception.ResultData;
import cn.org.opendfl.exception.ValidateUtils;
import cn.org.opendfl.task.biz.ITaDataMethodBiz;
import cn.org.opendfl.task.po.TaDataMethodPo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenjh
 * @Version V1.0
 * ta_data_method Controller
 * @Date: 2022年10月15日 下午8:16:35
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
@Api(tags = "ta_data_method接口")
@RestController
@RequestMapping("task/taDataMethod")
public class TaDataMethodController extends BaseController {

    static Logger logger = LoggerFactory.getLogger(TaDataMethodController.class);

    @Autowired
    private ITaDataMethodBiz taDataMethodBiz;

    /**
     * ta_data_method列表查询
     *
     * @param request  请求req
     * @param entity   ta_data_method对象
     * @param pageInfo 翻页对象
     * @return MyPageInfo 带翻页的数据集
     * @author chenjh
     * @date 2022年10月15日 下午8:16:35
     */
    @ApiOperation(value = "ta_data_method列表", notes = "ta_data_method列表翻页查询")
    @RequestMapping(value = "list", method = {RequestMethod.POST, RequestMethod.GET})
    public MyPageInfo<TaDataMethodPo> queryPage(HttpServletRequest request, TaDataMethodPo entity, MyPageInfo<TaDataMethodPo> pageInfo) {
        if (entity == null) {
            entity = new TaDataMethodPo();
        }
        if (pageInfo.getPageSize() == 0) {
            pageInfo.setPageSize(getPageSize());
        }
        pageInfo = taDataMethodBiz.findPageBy(entity, pageInfo, this.createAllParams(request));
        return pageInfo;
    }

    @ApiOperation(value = "ta_data_method列表(easyui)", notes = "ta_data_method列表翻页查询，用于兼容easyui的rows方式")
    @RequestMapping(value = "/list2", method = {RequestMethod.POST, RequestMethod.GET})
    public PageVO<TaDataMethodPo> findByPage(HttpServletRequest request, TaDataMethodPo entity, MyPageInfo<TaDataMethodPo> pageInfo) {
        logger.debug("-------findByPage-------");
        this.pageSortBy(pageInfo);
        pageInfo = queryPage(request, entity, pageInfo);
        return new PageVO<>(pageInfo);
    }

    /**
     * ta_data_method 新增
     *
     * @param request 请求req
     * @param entity  ta_data_method对象
     * @return ResultData 返回数据
     * @author chenjh
     * @date 2022年10月15日 下午8:16:35
     */
    @ApiOperation(value = "添加ta_data_method", notes = "添加一个ta_data_method")
    @RequestMapping(value = "save", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultData edit(TaDataMethodPo entity, HttpServletRequest request) {
        if (entity.getId() != null && entity.getId() > 0) {
            return update(entity, request);
        }
        entity.setModifyUser(getCurrentUserId());
        entity.setCreateUser(getCurrentUserId());
        taDataMethodBiz.saveTaDataMethod(entity);
        return ResultData.success();
    }

    /**
     * ta_data_method 更新
     *
     * @param request 请求req
     * @param entity  ta_data_method对象
     * @return ResultData 返回数据
     * @author chenjh
     * @date 2022年10月15日 下午8:16:35
     */
    @ApiOperation(value = "修改ta_data_method", notes = "根据传入的角色信息修改")
    @RequestMapping(value = "update", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultData update(TaDataMethodPo entity, HttpServletRequest request) {
        entity.setModifyUser(getCurrentUserId());
        int v = taDataMethodBiz.updateTaDataMethod(entity);
        return ResultData.success(v);
    }

    /**
     * ta_data_method 删除
     *
     * @param request 请求req
     * @param id      ta_data_methodID
     * @return ResultData 返回数据
     * @author chenjh
     * @date 2022年10月15日 下午8:16:35
     */
    @ApiOperation(value = "删除ta_data_method ", notes = "根据传入id进行删除状态修改(即软删除)")
    @RequestMapping(value = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultData delete(@RequestParam(name = "id", required = false) Integer id, HttpServletRequest request) {
        ValidateUtils.notNull(id, "id不能为空");
        String remark = request.getParameter("remark");
        int v = taDataMethodBiz.deleteTaDataMethod(id, this.getCurrentUserId(), remark);
        return ResultData.success(v);
    }
}