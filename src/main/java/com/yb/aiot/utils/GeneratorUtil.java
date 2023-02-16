package com.yb.aiot.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * <p>
 * 代码生成工具
 * <p>
 *
 * @author author
 * @date 2022/7/22 11:55
 */
public class GeneratorUtil {

    public static void main(String[] args) {

        String url = "jdbc:mysql://192.168.1.234:3306/aiot?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=utf8";
        String parent = "com.yslz.aiot.module";
        String moduleName = "device";
        String pathInfo = "/" + parent.replace(".", "/") + "/" + moduleName + "/mapper/xml";

//        Scanner input = new Scanner(System.in);
//        String str =input.next();

        FastAutoGenerator.create(url, "root", "root").globalConfig(builder -> {
                    builder.author("author") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出目录
                }).packageConfig(builder -> {
                    builder.parent(parent) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") + "/src/main/java" + pathInfo)); // 设置mapperXml生成路径
                }).strategyConfig(builder -> {
                    builder.addInclude("event_type") // 设置需要生成的表名
                    ; // 设置过滤表前缀
//            .addExclude("auth_role", "auth_user", "auth_user_role")
                }).templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
