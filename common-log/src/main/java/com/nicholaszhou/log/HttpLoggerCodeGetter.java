package com.nicholaszhou.log;

import com.github.yitter.idgen.YitIdHelper;
import com.nicholaszhou.constant.MDCConstants;
import com.nicholaszhou.log.interfacesupport.LoggerTraceCodeGetter;
import com.nicholaszhou.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 获取日志追踪码和用户编码
 */
public class HttpLoggerCodeGetter implements LoggerTraceCodeGetter<HttpServletRequest> {
    @Override
    public String getTraceCode(HttpServletRequest request) {
        return tryGetFromUserHeader(request)
                .filter(StringUtils::isNotBlank)
                .orElse(getUserIp(request));
    }

    @Override
    public String getUserCode(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(MDCConstants.LogTraceConstant.HTTP_TRACE_CODE_HEADER))
                .filter(StringUtils::isNotBlank)
                .orElse(String.valueOf(YitIdHelper.nextId()));
    }

    /**
     * 从上游的 header中获取
     *
     * @param request
     * @return
     */
    private Optional<String> tryGetFromUserHeader(HttpServletRequest request) {
        String header = request.getHeader(MDCConstants.LogTraceConstant.HTTP_USER_CODE_HEADER);
        return Optional.ofNullable(header);
    }


    /**
     * 当无法获取用户信息时 使用用户ip代替
     *
     * @param request
     * @return
     */
    private String getUserIp(HttpServletRequest request) {
        return getIp(request);
    }

    private String getIp(HttpServletRequest request) {
        String ip = "";
        if (StringUtils.isNotBlank(ip = request.getHeader("X-Forwarded-For"))) {
            // X-FORWARDED-FOR 的第一个ip为真实ip
            return ip.split(",")[0];
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("Proxy-Client-IP"))) {
            return ip;
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("WL-Proxy-Client-IP"))) {
            return ip;
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("HTTP_CLIENT_IP"))) {
            return ip;
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("HTTP_X_FORWARDED_FOR"))) {
            return ip;
        }
        return request.getRemoteAddr();
    }

}
