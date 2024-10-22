package io.github.nicholaszhou.log;

import io.github.nicholaszhou.properties.CommonLogProperties;
import io.github.nicholaszhou.log.interfacesupport.ReadableBodyResponseHandler;
import io.github.nicholaszhou.utils.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
public class MvcLogResponseHandler implements ReadableBodyResponseHandler {


    private final MvcPathMappingOperator mvcPathMappingOperator;

    public MvcLogResponseHandler(MvcPathMappingOperator mvcPathMappingOperator) {
        this.mvcPathMappingOperator = mvcPathMappingOperator;
    }

    public boolean handlerReadableBodyResponse(HttpServletRequest request, ContentCachingResponseWrapper response) {
        try {
            dealResponse(request, response);
        } catch (Exception e) {
            log.error("输出响应日志异常: ", e);
        }
        return true;
    }

    private void dealResponse(HttpServletRequest request, ContentCachingResponseWrapper response) {
        CommonLogProperties.LogProperties logProperties = mvcPathMappingOperator.findLogProperties(request);
//        Set<String> headerKeyList = Optional.ofNullable(logProperties).map(CommonLogProperties.LogProperties::getLogReqHeader).orElse(null);
        // 从请求中获取的header
        Enumeration<String> headerEnum = request.getHeaderNames();
        Map<String, String> headerMap = new LinkedHashMap<>();
        // 使用索引作为键，遍历 Enumeration
        while (headerEnum.hasMoreElements()) {
            String headerName = headerEnum.nextElement();
            String headerValue = request.getHeader(headerName); // 获取第一个值
            headerMap.put(headerName, headerValue);
        }
        Boolean disableRespBody = Optional.ofNullable(logProperties).map(CommonLogProperties.LogProperties::getDisableRespBody).orElse(false);
        Boolean disableResp = Optional.ofNullable(logProperties).map(CommonLogProperties.LogProperties::getDisableResp).orElse(false);
        HttpLogger.logResponseBody(response, request.getRequestURI(), HttpRequestUtils.headerStr(headerMap), disableRespBody, disableResp);
    }
}
