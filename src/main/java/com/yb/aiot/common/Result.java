package com.yb.aiot.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 公共消息包装类
 * <p>
 *
 * @author author
 * @date 2022/5/7 8:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "公共消息包装")
public class Result {

    @ApiModelProperty("请求状态码")
    private int code;

    @ApiModelProperty("携带消息")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private Object data;

    private static Result result(int code, String msg, Object data) {
        Result apiResult = new Result();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
    }

    public static Result ok() {
        return result(200, "success", null);
    }

    public static Result ok(Object data) {
        return result(200, "success", data);
    }

    public static Result fail() {
        return result(201, "fail", null);
    }

    public static Result fail(String msg) {
        return result(201, msg, null);
    }

    public static Result fail(int code, String msg) {
        return result(code, msg, null);
    }

    public static Result fail(Object data) {
        return result(201, "fail", data);
    }

}
