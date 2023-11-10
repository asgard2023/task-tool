package cn.org.opendfl.tasktool.utils;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.org.opendfl.tasktool.config.vo.ControllerConfigVo;
import cn.org.opendfl.tasktool.config.vo.TaskToolConfig;

import javax.servlet.http.HttpServletRequest;

/**
 * servletRequest参数处理
 *
 * @author chenjh
 */
public class RequestUtils {
    private RequestUtils() {

    }

    public static final String EXCEPTION_MSG_KEY = "expMsgKey";

    /**
     * 数据dataId，优先dataId，没有取userId，再没有取用户IP
     *
     * @param request
     * @return
     */
    public static String getDataId(TaskToolConfig taskToolConfiguration, HttpServletRequest request) {
        ControllerConfigVo controllerConfigVo = taskToolConfiguration.getControllerConfig();
        return getRequestValue(request, controllerConfigVo.getDataIdField());
    }

    public static String getUserId(TaskToolConfig taskToolConfiguration, HttpServletRequest request) {
        ControllerConfigVo controllerConfigVo = taskToolConfiguration.getControllerConfig();
        String userId = getRequestValue(request, controllerConfigVo.getUserIdField());
        if (userId == null) {
            userId = ServletUtil.getClientIP(request);
        }
        return userId;
    }

    /**
     * 从request获取参数，优先取参数，没有取attribute
     *
     * @param request   request
     * @param fieldName 参数名
     * @return
     */
    public static String getRequestValue(HttpServletRequest request, String fieldName) {
        if (CharSequenceUtil.isBlank(fieldName)) {
            return null;
        }
        Object fieldValue = request.getParameter(fieldName);
        if(fieldValue==null){
            fieldValue = request.getHeader(fieldName);
        }
        if (fieldValue == null) {
            fieldValue = request.getAttribute(fieldName);
        }
        if (fieldValue != null) {
            return "" + fieldValue;
        }
        return null;
    }

    /**
     * 根据类型获取uri或url
     * @param request
     * @param uriFinal
     * @param sourceType
     * @return
     */
    public static String getUriBySource(final HttpServletRequest request, final String uriFinal, final String sourceType){
        String uri = null;
        if(sourceType==null || "uri".equals(sourceType)) {
            uri = uriFinal;
        }
        else if("url".equals(sourceType)){
            uri = uriFinal;
            String queryString = request.getQueryString();
            if(CharSequenceUtil.isNotBlank(queryString)) {
                uri+="?" + queryString;
            }
        }
        return uri;
    }
}
