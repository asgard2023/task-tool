package cn.org.opendfl.task.controller;

import cn.org.opendfl.base.BaseController;
import cn.org.opendfl.base.MyPageInfo;
import cn.org.opendfl.base.PageVO;
import cn.org.opendfl.exception.ResultData;
import cn.org.opendfl.exception.ValidateUtils;
import cn.org.opendfl.task.biz.ITaDataMethodBiz;
import cn.org.opendfl.task.biz.ITaMethodCountBiz;
import cn.org.opendfl.task.po.TaDataMethodPo;
import cn.org.opendfl.task.po.TaMethodCountPo;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenjh
 * @Version V1.0
 * 任务运行次数统计记录 Controller
 * @Date: 2022年10月15日 下午8:15:58
 * @Company: opendfl
 * @Copyright: 2022 opendfl Inc. All rights reserved.
 */
@Api(tags = "任务运行次数统计记录接口")
@RestController
@RequestMapping("task/taMethodCount")
public class TaMethodCountController extends BaseController {

    static Logger logger = LoggerFactory.getLogger(TaMethodCountController.class);

    @Autowired
    private ITaMethodCountBiz taMethodCountBiz;

    @Autowired
    private ITaDataMethodBiz taDataMethodBiz;

    /**
     * 任务运行次数统计记录列表查询
     *
     * @param request  请求req
     * @param entity   任务运行次数统计记录对象
     * @param pageInfo 翻页对象
     * @return MyPageInfo 带翻页的数据集
     * @author chenjh
     * @date 2022年10月15日 下午8:15:58
     */
    @ApiOperation(value = "任务运行次数统计记录列表", notes = "任务运行次数统计记录列表翻页查询")
    @RequestMapping(value = "list", method = {RequestMethod.POST, RequestMethod.GET})
    public MyPageInfo<TaMethodCountPo> queryPage(HttpServletRequest request, TaMethodCountPo entity, MyPageInfo<TaMethodCountPo> pageInfo) {
        if (entity == null) {
            entity = new TaMethodCountPo();
        }
        if (pageInfo.getPageSize() == 0) {
            pageInfo.setPageSize(getPageSize());
        }
        pageInfo = taMethodCountBiz.findPageBy(entity, pageInfo, this.createAllParams(request));
        List<Integer> dataMethodIdList = pageInfo.getList().stream().map(TaMethodCountPo::getDataMethodId).distinct().collect(Collectors.toList());
        List<TaDataMethodPo> dataMethodPos = this.taDataMethodBiz.getDataByIds(dataMethodIdList, "createTime,modifyTime");
        pageInfo.getList().stream().forEach(t -> {
            for (TaDataMethodPo taDataMethodPo : dataMethodPos) {
                if (taDataMethodPo.getId().intValue() == t.getDataMethodId().intValue()) {
                    t.setDataMethod(taDataMethodPo);
                    break;
                }
            }
        });
        return pageInfo;
    }

    @ApiOperation(value = "任务运行次数统计记录列表(easyui)", notes = "任务运行次数统计记录列表翻页查询，用于兼容easyui的rows方式")
    @RequestMapping(value = "/list2", method = {RequestMethod.POST, RequestMethod.GET})
    public PageVO<TaMethodCountPo> findByPage(HttpServletRequest request, TaMethodCountPo entity, MyPageInfo<TaMethodCountPo> pageInfo) {
        logger.debug("-------findByPage-------");
        this.pageSortBy(pageInfo);
        pageInfo = queryPage(request, entity, pageInfo);
        return new PageVO<>(pageInfo);
    }

    /**
     * 任务运行次数统计记录 新增
     *
     * @param request 请求req
     * @param entity  任务运行次数统计记录对象
     * @return ResultData 返回数据
     * @author chenjh
     * @date 2022年10月15日 下午8:15:58
     */
    @ApiOperation(value = "添加任务运行次数统计记录", notes = "添加一个任务运行次数统计记录")
    @RequestMapping(value = "save", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultData edit(TaMethodCountPo entity, HttpServletRequest request) {
        if (entity.getId() != null && entity.getId() > 0) {
            return update(entity, request);
        }
//		entity.setUpdateUser(getCurrentUserId());
//		entity.setCreateUser(getCurrentUserId());
        taMethodCountBiz.saveTaMethodCount(entity);
        return ResultData.success();
    }

    /**
     * 任务运行次数统计记录 更新
     *
     * @param request 请求req
     * @param entity  任务运行次数统计记录对象
     * @return ResultData 返回数据
     * @author chenjh
     * @date 2022年10月15日 下午8:15:58
     */
    @ApiOperation(value = "修改任务运行次数统计记录", notes = "根据传入的角色信息修改")
    @RequestMapping(value = "update", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultData update(TaMethodCountPo entity, HttpServletRequest request) {
//		entity.setUpdateUser(getCurrentUserId());
        int v = taMethodCountBiz.updateTaMethodCount(entity);
        return ResultData.success(v);
    }

    /**
     * 任务运行次数统计记录 删除
     *
     * @param request 请求req
     * @param id      任务运行次数统计记录ID
     * @return ResultData 返回数据
     * @author chenjh
     * @date 2022年10月15日 下午8:15:58
     */
    @ApiOperation(value = "删除任务运行次数统计记录 ", notes = "根据传入id进行删除状态修改(即软删除)")
    @RequestMapping(value = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultData delete(@RequestParam(name = "id", required = false) Integer id, HttpServletRequest request) {
        ValidateUtils.notNull(id, "id不能为空");
        String remark = request.getParameter("remark");
        int v = taMethodCountBiz.deleteTaMethodCount(id, this.getCurrentUserId(), remark);
        return ResultData.success(v);
    }
}