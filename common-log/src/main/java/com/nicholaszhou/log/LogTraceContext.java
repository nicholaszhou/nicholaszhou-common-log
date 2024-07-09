package com.nicholaszhou.log;

import com.nicholaszhou.constant.MDCConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.util.Optional;

/**
 * 日志相关上下文
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogTraceContext {
    public static void setTraceCodeAndUserCode(String traceCode, String userCode) {
        MDC.put(MDCConstants.MDC_TID, traceCode);
        MDC.put(MDCConstants.MDC_UID, userCode);
        // todo: 后续考虑增加ip输出
    }

    /**
     * 获取 traceCode
     *
     * @return
     */
    public static Optional<String> getTraceCode() {
        return Optional.ofNullable(MDC.get(MDCConstants.MDC_TID));
    }

    /**
     * 清除
     */
    public static void clear() {
        MDC.remove(MDCConstants.MDC_TID);
        MDC.remove(MDCConstants.MDC_UID);
    }
}
