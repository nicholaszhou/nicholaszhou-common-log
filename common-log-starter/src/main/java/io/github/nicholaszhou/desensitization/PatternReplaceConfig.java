package io.github.nicholaszhou.desensitization;

import java.util.Map;

/**
 * JDK java.util.ServiceLoader 加载 自定配置
 * 需要 classPath/META-INF/services/  目录下自定义实现
 **/
public interface PatternReplaceConfig {

    /**
     * 增加 字段处方式  {@link DesensitizationLogMessageConverter}
     */
    Map<String, DesensitizationLogMessageConverter.PatternReplace> addPatternReplace();

}
