package com.nicholaszhou.log.interfacesupport;

import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;

/**
 *  对http请求中response的拦截处理
 */
public interface ReadableBodyResponseHandler {

    /**
     * 处理可读请求体的请求
     *
     * @param response
     */
    boolean handlerReadableBodyResponse(HttpServletRequest request, ContentCachingResponseWrapper response);


}
