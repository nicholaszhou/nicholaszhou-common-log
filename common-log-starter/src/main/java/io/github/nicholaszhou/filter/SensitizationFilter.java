package io.github.nicholaszhou.filter;

import io.github.nicholaszhou.log.MvcPathMappingOperator;
import io.github.nicholaszhou.properties.CommonLogProperties;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 脱敏上下文绑定过滤器
 **/
@Order(-101)
public class SensitizationFilter extends OncePerRequestFilter {

    private CommonLogProperties commonLogProperties;

    public SensitizationFilter(CommonLogProperties commonLogProperties) {
        this.commonLogProperties = commonLogProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (commonLogProperties.getSensitizationProperties() == null || commonLogProperties.getSensitizationProperties().getSensitizationFields() == null) {
            // 不需要脱敏
            filterChain.doFilter(request, response);
            return;
        }
        // 脱敏上下文绑定
        MvcPathMappingOperator.bindSensitizationContext(commonLogProperties.getSensitizationProperties().getSensitizationFields(),
                commonLogProperties.getSensitizationProperties().getLogger());
        try {
            filterChain.doFilter(request, response);
        } finally {
            MvcPathMappingOperator.removeSensitizationContext();
        }
    }


}
