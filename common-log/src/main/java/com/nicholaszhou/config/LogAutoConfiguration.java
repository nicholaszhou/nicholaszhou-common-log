package com.nicholaszhou.config;

import com.nicholaszhou.filter.HttpLoggerFilter;
import com.nicholaszhou.filter.SensitizationFilter;
import com.nicholaszhou.filter.TraceCodeFilter;
import com.nicholaszhou.log.MvcLogRequestHandler;
import com.nicholaszhou.log.MvcLogResponseHandler;
import com.nicholaszhou.log.MvcPathMappingOperator;
import com.nicholaszhou.log.interfacesupport.HttpLogConfigurer;
import com.nicholaszhou.properties.CommonLogProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties({CommonLogProperties.class})
@ConditionalOnProperty(prefix = "common.log", name = "enable", havingValue = "true", matchIfMissing = true)
public class LogAutoConfiguration {
    private final CommonLogProperties commonLogProperties;
    private List<HttpLogConfigurer> logDisableConfigurer;


    public LogAutoConfiguration(CommonLogProperties commonLogProperties, List<HttpLogConfigurer> logDisableConfigurer) {
        this.commonLogProperties = commonLogProperties;
        this.logDisableConfigurer = logDisableConfigurer;
    }

    @Bean
    @ConditionalOnProperty(prefix = "common.log.trace", name = "enable", havingValue = "true", matchIfMissing = true)
    public TraceCodeFilter traceCodeFilter() {
        return new TraceCodeFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "common.log.sensitization", name = "enable", havingValue = "true",matchIfMissing = true)
    public SensitizationFilter sensitizationFilter() {
        return new SensitizationFilter(mvcPathMappingOperator());
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
    public HttpLoggerFilter httpLoggerFilter() {
        return new HttpLoggerFilter();
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
