package io.github.nicholaszhou.log.interfacesupport;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *  对http请求中request的拦截处理
 */
public interface ReadableBodyRequestHandler {

    /**
     * 处理可读请求体的请求
     *
     * @param request
     */
    boolean handlerReadableBodyRequest(HttpServletRequest request, HttpServletResponse response);

}
