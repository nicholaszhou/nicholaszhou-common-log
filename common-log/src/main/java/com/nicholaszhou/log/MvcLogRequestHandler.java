package com.nicholaszhou.log;

import com.nicholaszhou.properties.CommonLogProperties;
import com.nicholaszhou.log.interfacesupport.ReadableBodyRequestHandler;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
public class MvcLogRequestHandler implements ReadableBodyRequestHandler {

    private final MvcPathMappingOperator mvcPathMappingOperator;

    public MvcLogRequestHandler(MvcPathMappingOperator mvcPathMappingOperator) {
        this.mvcPathMappingOperator = mvcPathMappingOperator;
    }

    public boolean handlerReadableBodyRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            dealRequest(request, response);
        } catch (Exception e) {
            log.error("输出请求日志异常: ", e);
        }
        return true;
    }

    private void dealRequest(HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> headerEnum = request.getHeaderNames();
        Set<String> headerList = new HashSet<>(Collections.list(headerEnum));
        CommonLogProperties.LogProperties logProperties = mvcPathMappingOperator.findLogProperties(request);
        Boolean disableReqBody = Optional.ofNullable(logProperties).map(CommonLogProperties.LogProperties::getDisableReqBody).orElse(false);
        Boolean disableReq = Optional.ofNullable(logProperties).map(CommonLogProperties.LogProperties::getDisableReq).orElse(false);
        HttpLogger.logForRequest(request, headerList, disableReqBody,disableReq);
    }
}
