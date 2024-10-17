package io.github.nicholaszhou.config;

import io.github.nicholaszhou.filter.HttpLoggerFilter;
import io.github.nicholaszhou.filter.SensitizationFilter;
import io.github.nicholaszhou.filter.TraceCodeFilter;
import io.github.nicholaszhou.log.MvcLogRequestHandler;
import io.github.nicholaszhou.log.MvcLogResponseHandler;
import io.github.nicholaszhou.log.MvcPathMappingOperator;
import io.github.nicholaszhou.log.interfacesupport.HttpLogConfigurer;
import io.github.nicholaszhou.properties.CommonLogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties({CommonLogProperties.class})
@ConditionalOnProperty(prefix = "common.log", name = "enable", havingValue = "true", matchIfMissing = true)
public class LogAutoConfiguration {
    private final CommonLogProperties commonLogProperties;
    private List<HttpLogConfigurer> logDisableConfigurer;

    @Autowired(required = false)
    public void setLogDisableConfigurer(List<HttpLogConfigurer> logDisableConfigurer) {
        this.logDisableConfigurer = logDisableConfigurer;
    }

    public LogAutoConfiguration(CommonLogProperties commonLogProperties) {
        this.commonLogProperties = commonLogProperties;
    }

    @Bean
    @ConditionalOnProperty(prefix = "common.log.trace", name = "enable", havingValue = "true", matchIfMissing = true)
    public TraceCodeFilter traceCodeFilter() {
        return new TraceCodeFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "common.log.sensitization", name = "enable", havingValue = "true", matchIfMissing = true)
    public SensitizationFilter sensitizationFilter() {
        return new SensitizationFilter(commonLogProperties);
    }

    @Bean
    public MvcLogRequestHandler mvcLogRequestHandler() {
        return new MvcLogRequestHandler(mvcPathMappingOperator());
    }

    @Bean
    public MvcLogResponseHandler mvcLogResponseHandler() {
        return new MvcLogResponseHandler(mvcPathMappingOperator());
    }

    @Bean
    @ConditionalOnProperty(prefix = "common.log.http", name = "enable", havingValue = "true", matchIfMissing = true)
    public HttpLoggerFilter httpLoggerFilter() {
        return new HttpLoggerFilter();
    }

    // 解决 RequestContextHolder.getRequestAttributes() 为 null 的问题,参考https://blog.csdn.net/qq_38846242/article/details/83382969
    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

    public MvcPathMappingOperator mvcPathMappingOperator() {
        List<CommonLogProperties.HttpPathProperties> codeLogHttpProperties = new ArrayList<>();
        if (!CollectionUtils.isEmpty(logDisableConfigurer)) {
            logDisableConfigurer.forEach(l -> l.addHttpLogConfig(codeLogHttpProperties));
        }
        List<CommonLogProperties.HttpPathProperties> httpPath = commonLogProperties.getDisablePath();
        return new MvcPathMappingOperator(httpPath, codeLogHttpProperties);
    }
}
