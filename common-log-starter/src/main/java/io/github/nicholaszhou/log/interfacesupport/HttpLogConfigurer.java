package io.github.nicholaszhou.log.interfacesupport;

import io.github.nicholaszhou.properties.CommonLogProperties;

import java.util.List;

/**
 * 增加Http禁止输出配置
 */
public interface HttpLogConfigurer {


    /**
     * @param logHttpProperties
     */
    default void addHttpLogConfig(List<CommonLogProperties.HttpPathProperties> logHttpProperties) {

    }
}
