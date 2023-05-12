package cn.org.opendfl.tasktool.client;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.tasktool.task.RouteApiVo;
import cn.org.opendfl.tasktool.utils.CommUtils;
import cn.org.opendfl.tasktool.utils.RestTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Service
@Slf4j
public class RoutingDelegate {

    public ResponseEntity<String> redirectEntity(HttpServletRequest request, HttpServletResponse response, String prefix, RouteApiVo routeApi) {
        String redirectUrl=null;
        try {
            // build up the redirect URL
            String routeUrl = routeApi.getApiUrl();
            redirectUrl = createRedictUrl(request, routeUrl, prefix);
            RequestEntity<?> requestEntity = createRequestEntity(request, redirectUrl, routeApi);
            ResponseEntity<String> responseEntity = route(requestEntity);
            if(responseEntity.getStatusCode()!= HttpStatus.OK){
                log.warn("----redirect--redirectUrl={} statusCode={}", redirectUrl, responseEntity.getStatusCode());
            }
            return responseEntity;
        } catch (Exception e) {
            log.error("----redirect--redirectUrl={} error={}", redirectUrl, e.getMessage(), e);
            return new ResponseEntity<>("{\"errorMsg\":\"REDIRECT ERROR:"+e.getMessage()+"\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String redirect(HttpServletRequest request, HttpServletResponse response, String prefix, RouteApiVo routeApi) {
        ResponseEntity<String> responseEntity = redirectEntity(request, response, prefix, routeApi);
        return responseEntity.getBody();
    }

    private String createRedictUrl(HttpServletRequest request, String routeUrl, String prefix) {
        String queryString = request.getQueryString();
        if(CharSequenceUtil.isNotBlank(queryString)) {
            queryString = CommUtils.removeParam("&" + queryString, "taskHostCode", "authKey");
            if (queryString.startsWith("&")) {
                queryString = queryString.substring(1);
            }
        }
        if("POST".equalsIgnoreCase(request.getMethod())) {
            queryString = appendPostParams(request, queryString);
        }
        return routeUrl + request.getRequestURI().replace(prefix, "") +
                (queryString != null ? "?" + queryString : "");
    }

    private static String appendPostParams(HttpServletRequest request, String queryString) {
        if(CharSequenceUtil.isBlank(queryString)){
            queryString ="";
        }
        Enumeration<String> params = request.getParameterNames();
        StringBuilder paramSB = new StringBuilder();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (!queryString.contains(param)) {
                paramSB .append("&").append( param).append("=").append(URLEncodeUtil.encode(request.getParameter(param)));
            }
        }
        String paramStr = paramSB.toString();
        if (paramStr.length() > 0) {
            paramStr = paramStr.substring(1);
        }
        if (CharSequenceUtil.isBlank(queryString)) {
            queryString = paramStr;
        } else {
            queryString += paramStr;
        }
        return queryString;
    }


    private RequestEntity<?> createRequestEntity(HttpServletRequest request, String url, RouteApiVo routeApi) throws URISyntaxException, IOException {
        String method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.resolve(method);
        MultiValueMap<String, String> headers = parseRequestHeader(request, routeApi);
        byte[] body = parseRequestBody(request);
        return new RequestEntity<>(body, headers, httpMethod, new URI(url));
    }

    private ResponseEntity<String> route(RequestEntity<?> requestEntity) {
        return RestTemplateUtils.getRestTemplate().exchange(requestEntity, String.class);
    }


    private byte[] parseRequestBody(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        return StreamUtils.copyToByteArray(inputStream);
    }

    private MultiValueMap<String, String> parseRequestHeader(HttpServletRequest request, RouteApiVo routeApi) {
        HttpHeaders headers = new HttpHeaders();
        if (CharSequenceUtil.isNotBlank(routeApi.getSource())) {
            headers.add("source", routeApi.getSource());
        }
        headers.add("authKey", routeApi.getAuthKey());
        List<String> headerNames = Collections.list(request.getHeaderNames());
        for (String headerName : headerNames) {
            List<String> headerValues = Collections.list(request.getHeaders(headerName));
            for (String headerValue : headerValues) {
                headers.add(headerName, headerValue);
            }
        }
        return headers;
    }
}