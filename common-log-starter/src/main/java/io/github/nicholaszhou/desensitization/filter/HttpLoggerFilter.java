package io.github.nicholaszhou.desensitization.filter;

import io.github.nicholaszhou.constant.MDCConstants;
import io.github.nicholaszhou.log.CopyContentCachingRequestWrapper;
import io.github.nicholaszhou.log.interfacesupport.ReadableBodyRequestHandler;
import io.github.nicholaszhou.log.interfacesupport.ReadableBodyResponseHandler;
import io.github.nicholaszhou.utils.HttpRequestUtils;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;

import java.io.IOException;
import java.util.List;

@Order(-100)
public class HttpLoggerFilter extends OncePerRequestFilter {

    @Autowired
    private List<ReadableBodyRequestHandler> requestHandlerList;
    @Autowired
    private List<ReadableBodyResponseHandler> readableBodyResponseHandlerList;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        setRequestAttributes(request);
        // 获取请求体内容
        boolean includePayload = HttpRequestUtils.isIncludePayload(request);
        if (includePayload) {
            request = new CopyContentCachingRequestWrapper(request);
        }
        response = new ContentCachingResponseWrapper(response);
        try {
            boolean shouldReturn = handlerReadableBodyRequest(request, response);
            if (shouldReturn) {
                return;
            }
            filterChain.doFilter(request, response);

            handlerReadableBodyResponse(request, (ContentCachingResponseWrapper) response);
        } finally {
            ((ContentCachingResponseWrapper) response).copyBodyToResponse();
        }
    }

    private boolean handlerReadableBodyRequest(HttpServletRequest request, HttpServletResponse response) {
        boolean exit = false;
        for (ReadableBodyRequestHandler handler : requestHandlerList) {
            boolean continueNext = handler.handlerReadableBodyRequest(request, response);
            if (!continueNext) {
                exit = true;
                break;
            }
        }
        return exit;
    }

    private void handlerReadableBodyResponse(HttpServletRequest request, ContentCachingResponseWrapper response) {
        for (ReadableBodyResponseHandler handler : readableBodyResponseHandlerList) {
            boolean continueNext = handler.handlerReadableBodyResponse(request, response);
            if (!continueNext) {
                break;
            }
        }
    }

    private void setRequestAttributes(HttpServletRequest request) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null || request == null) {
            return;
        }
        requestAttributes.setAttribute(MDCConstants.HttpServletConstant.REQUEST_METHOD, request.getMethod(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(MDCConstants.HttpServletConstant.REQUEST_PATH, request.getRequestURI(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(MDCConstants.HttpServletConstant.REQUEST_CONTENT_TYPE, request.getContentType(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(MDCConstants.HttpServletConstant.REQUEST_PARAMETER_MAP, request.getParameterMap(), RequestAttributes.SCOPE_REQUEST);
        requestAttributes.setAttribute(MDCConstants.HttpServletConstant.REQUEST_TIMESTAMP, System.currentTimeMillis(), RequestAttributes.SCOPE_REQUEST);
    }
}
