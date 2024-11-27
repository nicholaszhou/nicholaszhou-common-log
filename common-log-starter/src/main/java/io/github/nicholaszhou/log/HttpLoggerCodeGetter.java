package io.github.nicholaszhou.log;

import com.github.yitter.idgen.YitIdHelper;
import io.github.nicholaszhou.constant.MDCConstants;
import io.github.nicholaszhou.log.interfacesupport.LoggerTraceCodeGetter;
import io.github.nicholaszhou.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * 获取日志追踪码和用户编码
 */
public class HttpLoggerCodeGetter implements LoggerTraceCodeGetter<HttpServletRequest> {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "127.0.0.1";
    // 客户端与服务器同为一台机器，获取的 ip 有时候是 ipv6 格式
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String SEPARATOR = ",";

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
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCALHOST_IP.equalsIgnoreCase(ip) || LOCALHOST_IPV6.equalsIgnoreCase(ip)) {
                // 根据网卡取本机配置的 IP
                InetAddress iNet = null;
                try {
                    iNet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (iNet != null)
                    ip = iNet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，分割出第一个 IP
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(SEPARATOR) > 0) {
                ip = ip.substring(0, ip.indexOf(SEPARATOR));
            }
        }
        return LOCALHOST_IPV6.equals(ip) ? LOCALHOST_IP : ip;
    }

}
