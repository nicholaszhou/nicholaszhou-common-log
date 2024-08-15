package io.github.nicholaszhou.filter;

import io.github.nicholaszhou.log.MvcPathMappingOperator;
import io.github.nicholaszhou.properties.CommonLogProperties;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 脱敏上下文绑定过滤器
 **/
@Order(-101)
public class SensitizationFilter extends OncePerRequestFilter {

    private MvcPathMappingOperator mvcPathMappingOperator;

    public SensitizationFilter(MvcPathMappingOperator mvcPathMappingOperator) {
        this.mvcPathMappingOperator = mvcPathMappingOperator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CommonLogProperties.SensitizationProperties sensitizationProperties = mvcPathMappingOperator.findSensitizationProperties(request);

        if (sensitizationProperties == null) {
            // 不需要脱敏
            filterChain.doFilter(request, response);
            return;
        }

        // 脱敏上下文绑定
        MvcPathMappingOperator.bindSensitizationContext(sensitizationProperties.getSensitizationFields(),
                sensitizationProperties.getLogger());
        try {
            filterChain.doFilter(request, response);
        } finally {
            MvcPathMappingOperator.removeSensitizationContext();
        }
    }


}