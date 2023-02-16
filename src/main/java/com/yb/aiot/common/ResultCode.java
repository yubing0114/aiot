package com.yb.aiot.common;

/**
 * <p>
 * ResultCode的枚举类
 * <p>
 *
 * @author author
 * @date 2022/12/16 16:52
 */
public enum ResultCode {

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    ORGINFO_UNINIT(202, "机构信息未初始化"),
    LOGIN_TIMEOUT(203, "登陆过期");

    private final Integer code;

    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
