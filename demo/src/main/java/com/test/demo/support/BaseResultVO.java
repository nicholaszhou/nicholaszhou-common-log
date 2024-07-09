package com.test.demo.support;

import lombok.Data;

import java.io.Serializable;

@SuppressWarnings({"rawtypes", "unchecked"})
@Data
public class BaseResultVO<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6424892182374472984L;

    /**
     * 状态码：1成功，其他为失败
     */

    private String code;

    /**
     * 成功为success，其他为失败原因
     */
    private String msg;

    /**
     * 数据结果集
     */

    private T data;

    public BaseResultVO() {

    }

    public BaseResultVO(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResultVO(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> BaseResultVO<T> build(String code, String msg, T data) {
        return new BaseResultVO(code, msg, data);
    }

    public static <T> BaseResultVO<T> build(String code, String msg) {
        return new BaseResultVO(code, msg, null);
    }
}
