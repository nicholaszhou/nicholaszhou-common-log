package com.nicholaszhou.log;

import com.nicholaszhou.properties.CommonLogProperties;
import com.nicholaszhou.log.interfacesupport.ReadableBodyRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.MimeHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class MvcLogRequestHandler implements ReadableBodyRequestHandler {

    private final MvcPathMappingOperator mvcPathMappingOperator;

    public MvcLogRequestHandler(MvcPathMappingOperator mvcPathMappingOperator) {
        this.mvcPathMappingOperator = mvcPathMappingOperator;
    }

    public boolean handlerReadableBodyRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            dealRequest(request);
        } catch (Exception e) {
            log.error("输出请求日志异常: ", e);
        }
        return true;
    }

    private void dealRequest(HttpServletRequest request) {
        CommonLogProperties.LogProperties logProperties = mvcPathMappingOperator.findLogProperties(request);
        Boolean disableReqBody = Optional.ofNullable(logProperties).map(CommonLogProperties.LogProperties::getDisableReqBody).orElse(false);
        Boolean disableReq = Optional.ofNullable(logProperties).map(CommonLogProperties.LogProperties::getDisableReq).orElse(false);
        Map<String, String> showedHeaderMap = new HashMap<>();
        if (!disableReq) {
            // 从请求中获取的header
            Enumeration<String> headerEnum = request.getHeaderNames();
            Map<String, String> headerMap = new LinkedHashMap<>();
            // 使用索引作为键，遍历 Enumeration
            while (headerEnum.hasMoreElements()) {
                String headerName = headerEnum.nextElement();
                String headerValue = request.getHeader(headerName); // 获取第一个值
                headerMap.put(headerName, headerValue);
            }

            // 获取配置文件中定义的header
            Set<String> requestHeaderKeys = logProperties.getLogReqHeader();
            // 使用toLowerCase()方法将每个字符串转换为小写
            requestHeaderKeys = requestHeaderKeys.stream().map(String::toLowerCase).collect(Collectors.toSet());
            if (requestHeaderKeys != null) {
                // 遍历原始Map的键，并与keysToFilter进行比较
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    if (requestHeaderKeys.contains(entry.getKey())) {
                        showedHeaderMap.put(entry.getKey(), entry.getValue());
                    }
                }
            } else {
                showedHeaderMap = headerMap;
            }
        }
        HttpLogger.logForRequest(request, showedHeaderMap, disableReqBody, disableReq);
    }
}
