package io.github.nicholaszhou.log.interfacesupport;

import io.github.nicholaszhou.properties.CommonLogProperties;

import java.util.List;

public interface HttpLogConfigurer {

    /**
     * 增加Http禁止输出配置
     */
    default void addHttpLogConfig(List<CommonLogProperties.HttpPathProperties> logHttpProperties) {

    }
}
