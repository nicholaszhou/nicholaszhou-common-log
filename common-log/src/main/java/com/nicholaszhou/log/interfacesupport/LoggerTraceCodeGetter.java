package com.nicholaszhou.log.interfacesupport;

public interface LoggerTraceCodeGetter<T> {
    /**
     * 获取 TraceCode
     *
     * @return
     */
    String getTraceCode(T t);

    /**
     * 获取 userCode
     *
     * @return
     */
    String getUserCode(T t);

}
