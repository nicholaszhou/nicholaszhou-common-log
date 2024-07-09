package com.nicholaszhou.log;

import com.nicholaszhou.constant.MDCConstants;
import com.nicholaszhou.utils.HttpRequestUtils;
import com.nicholaszhou.utils.IOUtils;
import com.nicholaszhou.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 处理http请求的log
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpLogger {
    public static void logForRequest(HttpServletRequest request, Set<String> headerKeyList, Boolean disableReqBody) {
        Map<String, String> headerMap = headerMap(headerKeyList, request::getHeader);
        String body = HttpRequestUtils.BOUNDARY_BODY;
        if (!Boolean.TRUE.equals(disableReqBody)) {
            body = reqBody(request);
        }
        logForRequest(request.getRequestURI(), request.getMethod(), request.getParameterMap(),
                headerMap, body);
    }

    private static void logForRequest(String path, String method, Map<String, String[]> parameterMap,
                                      Map<String, String> headerMap, String body) {
        parameterMap = Optional.ofNullable(parameterMap)
                .orElse(Collections.emptyMap());
        if (headerMap == null) {
            log.info("Http请求 Path: {}, Method: {}, Parameter: {}, Body: {}",
                    path,
                    method,
                    HttpRequestUtils.parameterMapToString(parameterMap),
                    body);
        } else {
            log.info("Http请求 Path: {}, Method: {}, Parameter: {}, Header: {}, Body: {}",
                    path,
                    method,
                    HttpRequestUtils.parameterMapToString(parameterMap),
                    headerStr(headerMap),
                    body);
        }

    }

    public static void logResponseBody(ContentCachingResponseWrapper response, String requestPath, Set<String> headerKeyList, Boolean disableRespBody) {
        String body = HttpRequestUtils.BOUNDARY_BODY;
        if (!Boolean.TRUE.equals(disableRespBody)) {
            body = respBody(response);
        }
        Map<String, String> headerMap = headerMap(headerKeyList, response::getHeader);
        logResponseBody(requestPath,body, response.getStatus(), headerMap);
    }

    public static void logResponseBody(String requestPath,String body, int status, Map<String, String> headerMap) {
        Long timestamp = HttpRequestUtils.getRequestAttribute(Objects.requireNonNull(RequestContextHolder.getRequestAttributes()), MDCConstants.HttpServletConstant.REQUEST_TIMESTAMP);
        if (headerMap == null) {
            log.info("Http响应 路径:{}, Status: {}, Body: {}, 请求耗时(0.1秒): {}",
                    requestPath,
                    status,
                    body,
                    (System.currentTimeMillis() - timestamp)/100);
        } else {
            log.info("Http响应 路径:{}, Status: {}, Header: {}, Body: {}, 请求耗时(0.1秒): {}",
                    requestPath,
                    status,
                    headerStr(headerMap),
                    body,
                    (System.currentTimeMillis() - timestamp)/100);
        }

    }

    private static String respBody(ContentCachingResponseWrapper response) {
        String contentType = response.getContentType();
        if (isBoundaryBody(contentType)) {
            return HttpRequestUtils.BOUNDARY_BODY;
        }
        InputStream contentInputStream = response.getContentInputStream();
        try {
            return IOUtils.inputStreamToString(contentInputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String headerStr(Map<String, String> headerMap) {
        return headerMap.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(","));
    }

    private static String reqBody(HttpServletRequest request) {
        boolean includePayload = HttpRequestUtils.isIncludePayload(request);
        if (!includePayload) {
            return "";
        }
        String contentType = request.getContentType();
        if (isBoundaryBody(contentType)) {
            return HttpRequestUtils.BOUNDARY_BODY;
        }
        return HttpRequestUtils.readRequestBodyForJson(request);
    }


    // 判断是否是可读log
    private static boolean isBoundaryBody(String contentType) {
        return StringUtils.isNotBlank(contentType)
                && !HttpRequestUtils.notBoundaryBody(contentType);
    }

    private static Map<String, String> headerMap(Set<String> headerKeyList, Function<String, String> getHeaderF) {
        return Optional.ofNullable(headerKeyList)
                .map(l -> l.stream()
                        .map(k -> new AbstractMap.SimpleEntry<>(k, getHeaderF.apply(k)))
                        .filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n)))
                .orElse(null);
    }
}