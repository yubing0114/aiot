package com.yb.aiot.common;

import com.yb.aiot.module.auth.entity.User;

/**
 * <p>
 * 权限码常量
 * <p>
 *
 * @author author
 * @date 2022/11/10 10:21
 */
public final class AuthConst {

    private AuthConst() {
    }

    // 1级-系统最高权限
    public static final String MASTER = "master";

    // 2级-系统管理员权限
    public static final String ADMIN = "admin";

    // 3级-系统用户权限
    public static final String USER = "user";

    // 3级-app API调用权限
    public static final String APP = "app";

    // 3级-开放API调用权限
    public static final String OPEN = "open";

    public User getMaster() {
        return User.builder().username("master").password("master").build();

    }

    String ROLE = "[\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"name\": \"超级管理员\",\n" +
            "      \"code\": \"master\",\n" +
            "      \"description\": \"1级-系统最高权限\",\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"name\": \"管理员\",\n" +
            "      \"code\": \"admin\",\n" +
            "      \"description\": \"2级-系统管理员权限\",\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 3,\n" +
            "      \"name\": \"普通用户\",\n" +
            "      \"code\": \"user\",\n" +
            "      \"description\": \"3级-系统用户权限\",\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 4,\n" +
            "      \"name\": \"app用户\",\n" +
            "      \"code\": \"app\",\n" +
            "      \"description\": \"3级-app API调用权限\",\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 5,\n" +
            "      \"name\": \"OpenApi用户\",\n" +
            "      \"code\": \"open\",\n" +
            "      \"description\": \"3级-open API调用权限\",\n" +
            "    }\n" +
            "  ]";



}
