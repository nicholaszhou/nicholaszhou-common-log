package com.test.demo.support;

import com.nicholaszhou.config.CommonLogProperties;
import com.nicholaszhou.log.interfacesupport.HttpLogConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;

/**
 * 健康检查的日志关闭
 */
@Configuration
@Slf4j
public class HealthLogDisableConfigurer implements HttpLogConfigurer {

    public HealthLogDisableConfigurer() {
        log.debug("HealthLogDisableConfigurer");
    }

    @Override
    public void addHttpLogConfig(List<CommonLogProperties.HttpPathProperties> logHttpPropertiesMap) {

        CommonLogProperties.HttpPathProperties commonLogHttpProperties = new CommonLogProperties.HttpPathProperties();
        commonLogHttpProperties.setPath("/actuator/**");
        commonLogHttpProperties.setMethods(Collections.singleton(HttpMethod.GET));

        CommonLogProperties.LogProperties logProperties = new CommonLogProperties.LogProperties();
        logProperties.setDisableReq(true);
        logProperties.setDisableResp(true);

        commonLogHttpProperties.setLog(logProperties);
        logHttpPropertiesMap.add(commonLogHttpProperties);
    }
}
