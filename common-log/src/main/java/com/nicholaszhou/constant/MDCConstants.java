package com.nicholaszhou.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 日志相关常量
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MDCConstants {
    public static final String MDC_UID = "uid";
    public static final String MDC_TID = "tid";
    public static final String MDC_IP = "ip";
    /**
     * 赋值符
     */
    public static final String ASSIGNOR_STR = "=";

    /**
     * 逗号
     */
    public static final String COMMA_STR = ",";

    /**
     * 连接符
     */
    public static final String CONNECTOR_STR = "&";

    public class LogTraceConstant {

        public static final String HTTP_TRACE_CODE_HEADER = "X-Tid";

        public static final String HTTP_USER_CODE_HEADER = "X-Uid";

    }

    /**
     * Http常量
     */
    public interface HttpServletConstant {

        String REQUEST_PATH = "http_request_path";

        /**
         * 请求方法
         */
        String REQUEST_METHOD = "http_request_method";

        /**
         * Content-Type
         */
        String REQUEST_CONTENT_TYPE = "http_request_content_type";


        /**
         * query string
         */
        String REQUEST_PARAMETER_MAP = "http_request_parameter_map";

        /**
         * 请求时间戳
         */
        String REQUEST_TIMESTAMP = "http_request_timestamp";

        /**
         * 内置 http 端点请求路径
         */
        String ENDPOINT_PATH_PREFIX = "/endpoint";


    }
}
