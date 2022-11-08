package cn.org.opendfl.tasktool.utils;

import cn.hutool.extra.servlet.ServletUtil;
import cn.org.opendfl.tasktool.config.TaskToolConfiguration;
import cn.org.opendfl.tasktool.config.vo.ControllerConfigVo;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
    public static final String EXCEPTION_MSG_KEY = "expMsgKey";

    /**
     * 数据dataId，优先dataId，没有取userId，再没有取用户IP
     *
     * @param request
     * @return
     */
    public static String getDataId(TaskToolConfiguration taskToolConfiguration, HttpServletRequest request) {
        ControllerConfigVo controllerConfigVo = taskToolConfiguration.getControllerConfig();
        String dataId = getRequestValue(request, controllerConfigVo.getDataIdField());
        if (dataId == null) {
            dataId = getRequestValue(request, controllerConfigVo.getUserIdField());
        }
        if (dataId == null) {
            dataId = ServletUtil.getClientIP(request);
        }
        return dataId;
    }

    /**
     * 从request获取参数，优先取参数，没有取attribute
     *
     * @param request   request
     * @param fieldName 参数名
     * @return
     */
    public static String getRequestValue(HttpServletRequest request, String fieldName) {
        Object fieldValue = request.getParameter(fieldName);
        if (fieldValue == null) {
            fieldValue = request.getAttribute(fieldName);
        }
        if (fieldValue != null) {
            return "" + fieldValue;
        }
        return null;
    }
}
