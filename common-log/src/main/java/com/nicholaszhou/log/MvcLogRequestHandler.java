package com.nicholaszhou.log;

import com.nicholaszhou.log.interfacesupport.ReadableBodyRequestHandler;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
public class MvcLogRequestHandler implements ReadableBodyRequestHandler {
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
        Set<String> headerList = new HashSet<String>(Collections.list(headerEnum));
//        MDCConstants.LogProperties logProperties = mvcPathMappingOperator.findLogProperties(request);
//        Boolean disableReqBody = Optional.ofNullable(logProperties)
//                .map(CommonLogProperties.LogProperties::getDisableReqBody)
//                .orElse(false);

        HttpLogger.logForRequest(request, headerList, false);
    }
}
