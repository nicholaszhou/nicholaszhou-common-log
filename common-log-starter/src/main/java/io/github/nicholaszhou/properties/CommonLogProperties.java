package io.github.nicholaszhou.properties;

import io.github.nicholaszhou.log.HttpLogger;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 共通属性类
 */
@Data
@ConfigurationProperties(prefix = "common.log")
public class CommonLogProperties {

    /**
     * http 路径 相关配置
     */
    private List<HttpPathProperties> disablePath;

    /**
     * log全局配置
     */
    private LogProperties logProperties;

    /**
     * 脱敏相关配置
     */
    private SensitizationProperties sensitizationProperties;


    /**
     * http 路径 相关配置
     */
    @Data
    public static class HttpPathProperties {

        private static final Set<HttpMethod> SUPPORT_METHOD =
                Stream.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).collect(Collectors.toSet());

        /**
         * 请求 method
         */
        private Set<HttpMethod> methods = SUPPORT_METHOD;

        /**
         * 不打log地址
         */
        private String path;

        /**
         * 日志配置
         */
        private LogProperties log;

        /**
         * 脱敏相关配置
         */
        private SensitizationProperties sensitizationProperties;
    }

    /**
     * log全局配置
     */
    @Data
    public static class LogProperties {
        /**
         * 需要输出的请求头
         */
        private Set<String> logReqHeader;

        /**
         * 需要输出的响应请头
         */
        private Set<String> logRespHeader;

        /**
         * 是否禁止请求日志输出
         */
        private Boolean disableReq;

        /**
         * 禁止请求体日志输出
         */
        private Boolean disableReqBody;

        /**
         * 是否禁止响应日志输出
         */
        private Boolean disableResp;

        /**
         * 禁止响应体日志输出
         */
        private Boolean disableRespBody;
    }


    /**
     * 脱敏相关配置
     */
    @Data
    public static class SensitizationProperties {

        /**
         * 敏感字段
         */
        private Set<String> sensitizationFields;
        /**
         * Logger 用于精确匹配
         */
        private String logger = HttpLogger.class.getName();

    }


}
