package com.nicholaszhou.log;

import com.nicholaszhou.config.CommonLogProperties;
import com.nicholaszhou.log.interfacesupport.ReadableBodyResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class MvcLogResponseHandler implements ReadableBodyResponseHandler {


    private MvcPathMappingOperator mvcPathMappingOperator;

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
        Set<String> headerKeyList = Optional.ofNullable(logProperties).map(CommonLogProperties.LogProperties::getLogReqHeader).orElse(null);

        Boolean disableRespBody = Optional.ofNullable(logProperties).map(CommonLogProperties.LogProperties::getDisableRespBody).orElse(false);

        HttpLogger.logResponseBody(response, request.getRequestURI(), headerKeyList, disableRespBody);
    }
}
