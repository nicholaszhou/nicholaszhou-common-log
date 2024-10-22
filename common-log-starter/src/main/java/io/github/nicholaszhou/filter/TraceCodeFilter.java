package io.github.nicholaszhou.filter;

import io.github.nicholaszhou.constant.MDCConstants;
import io.github.nicholaszhou.log.HttpLoggerCodeGetter;
import io.github.nicholaszhou.log.LogTraceContext;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 监听请求,设置mdc的traceCode
 */
@Order(-102 )
public class TraceCodeFilter extends OncePerRequestFilter {
    private final HttpLoggerCodeGetter logTraceCodeGetter = new HttpLoggerCodeGetter();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            LogTraceContext.setTraceCodeAndUserCode(logTraceCodeGetter.getTraceCode(request),
                    logTraceCodeGetter.getUserCode(request));
            response.addHeader(MDCConstants.LogTraceConstant.HTTP_TRACE_CODE_HEADER, LogTraceContext.getTraceCode().orElse(""));
            filterChain.doFilter(request, response);
        } finally {
            LogTraceContext.clear();
        }
    }
}
