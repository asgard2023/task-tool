package cn.org.opendfl.tasktooldemo.controller;


import cn.org.opendfl.tasktool.task.annotation.TaskComputeServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = "/taskTest")
@TaskComputeServlet(userIdParamName = "u", dataIdParamName = "sign")
public class TaskTestServlet extends HttpServlet {
    static Logger logger = LoggerFactory.getLogger(TaskTestServlet.class);
    private static final long serialVersionUID = 1L;

    public TaskTestServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = getHeaderOrParam(request, "request_uri");
        if (path == null) {
            path = getHeaderOrParam(request, "path");
        }

        String u = request.getParameter("u");
        String dataId = request.getParameter("sign");
        logger.debug("----path={} u={} sign={}", path, u, dataId);


        boolean isValid = path != null && path.startsWith("/vod/");
        if (isValid) {
            response.getWriter().append("ok");
            return;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().append("fail");
    }


    private String getHeaderOrParam(HttpServletRequest request, String paramName) {
        String value = request.getHeader(paramName);
        if (value == null) {
            value = request.getParameter(paramName);
        }
        return value;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
