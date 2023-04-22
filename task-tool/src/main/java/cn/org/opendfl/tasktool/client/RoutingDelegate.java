package cn.org.opendfl.tasktool.client;

import cn.hutool.core.text.CharSequenceUtil;
import cn.org.opendfl.tasktool.task.RouteApiVo;
import cn.org.opendfl.tasktool.utils.CommUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class RoutingDelegate {

    @Resource
    private RestTemplate restTemplate;

    public ResponseEntity<String> redirect(HttpServletRequest request, HttpServletResponse response, String prefix, RouteApiVo routeApi) {
        String redirectUrl=null;
        try {
            // build up the redirect URL
            String routeUrl = routeApi.getApiUrl();
            redirectUrl = createRedictUrl(request, routeUrl, prefix, routeApi);
            log.info("----redirect--redirectUrl={}", redirectUrl);
            RequestEntity requestEntity = createRequestEntity(request, redirectUrl, routeApi);
            return route(requestEntity);
        } catch (Exception e) {
            log.error("----redirect--redirectUrl={} error={}", redirectUrl, e.getMessage(), e);
            return new ResponseEntity("REDIRECT ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String createRedictUrl(HttpServletRequest request, String routeUrl, String prefix, RouteApiVo routeApi) {
        String queryString = request.getQueryString();
        queryString = CommUtils.removeParam("&"+queryString, new String[]{"taskHostCode", "authKey"});
        if(queryString.startsWith("&")){
            queryString = queryString.substring(1);
        }
        return routeUrl + request.getRequestURI().replace(prefix, "") +
                (queryString != null ? "?" + queryString : "");
    }


    private RequestEntity createRequestEntity(HttpServletRequest request, String url, RouteApiVo routeApi) throws URISyntaxException, IOException {
        String method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.resolve(method);
        MultiValueMap<String, String> headers = parseRequestHeader(request, routeApi);
        byte[] body = parseRequestBody(request);
        return new RequestEntity<>(body, headers, httpMethod, new URI(url));
    }

    private ResponseEntity<String> route(RequestEntity requestEntity) {
        return restTemplate.exchange(requestEntity, String.class);
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