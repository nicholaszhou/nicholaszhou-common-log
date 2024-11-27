package io.github.nicholaszhou.log.interfacesupport;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingResponseWrapper;


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
