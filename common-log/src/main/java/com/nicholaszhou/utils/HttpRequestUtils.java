package com.nicholaszhou.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.server.PathContainer;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestUtils {
    // 分隔符
    private static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String BOUNDARY_BODY = "Binary data";
    private static final PathPatternParser PATH_MATCHER = new PathPatternParser();
    private static final Pattern CAN_LOG_PATTERN = Pattern.compile("text|json|xml|html|plain|form-urlencoded");

    public static boolean isIncludePayload(HttpServletRequest request) {
        int contentLength = request.getContentLength();
        return contentLength != 0;
    }

    // 读取请求json数据
    public static String readRequestBodyForJson(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
            String charEncoding = requestWrapper.getCharacterEncoding();
            try {
                return new String(requestWrapper.getContentAsByteArray(), charEncoding);
            } catch (UnsupportedEncodingException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            String str = null;
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = null;
            try {
                reader = request.getReader();
                while ((str = reader.readLine()) != null) {
                    stringBuilder.append(str).append(LINE_SEPARATOR);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            if (stringBuilder.length() > 0) {
                return stringBuilder.substring(0, stringBuilder.length() - LINE_SEPARATOR.length());
            }
            return StringUtils.EMPTY;
        }
    }

    public static String parameterMapToString(Map<String, String[]> parameterMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(parameterMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取请求属性
     *
     * @param requestAttributes
     * @param name
     * @param <T>
     * @return
     */
    public static <T> T getRequestAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }


    // 判断是否是二进制类型请求
    public static boolean notBoundaryBody(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            return true;
        }
        Matcher matcher = CAN_LOG_PATTERN.matcher(contentType);
        return matcher.find();
    }

    /**
     * 路径是否匹配
     *
     * @param pattern
     * @param path
     * @return
     */
    public static boolean isMatchPath(String pattern, String path) {
        return PATH_MATCHER.parse(pattern).matches(PathContainer.parsePath(path));
    }
}
