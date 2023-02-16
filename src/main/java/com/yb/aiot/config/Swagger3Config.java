package com.yb.aiot.config;

import com.yb.aiot.common.ResultCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableOpenApi
public class Swagger3Config {

    // 模块序号
    private static int orderNumber = 1;

    /**
     * @param moduleName 模块名称
     * @param enable     是否开启
     * @return springfox.documentation.spring.web.plugins.Docket
     */
    public Docket docket(String moduleName, boolean enable) {
        List<Response> responseList = new ArrayList<>();
        Arrays.stream(ResultCode.values()).forEach(resultCode -> {
            responseList.add(
                    new ResponseBuilder().code(resultCode.getCode().toString()).description(resultCode.getMessage()).build()
            );
        });
        return new Docket(DocumentationType.OAS_30)
                .globalResponses(HttpMethod.GET,responseList)
                .globalResponses(HttpMethod.POST,responseList)
                .apiInfo(apiInfo())
                .groupName(String.format("%s. %s模块", orderNumber++, moduleName))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yslz.aiot.module." + moduleName))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .enable(enable);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("aiot 接口文档")
                .description("aiot")
                .contact(new Contact("110", "648540858", "648540858@qq.com"))
                .version("3.0")
                .build();
    }

    @Bean
    public Docket auth() {
        return docket("auth", true);
    }

    @Bean
    public Docket common() {
        return docket("common", true);
    }

    @Bean
    public Docket device() {
        return docket("device", true);
    }

    @Bean
    public Docket sdk() {
        return docket("sdk", true);
    }

    @Bean
    public Docket scheduled() {
        return docket("scheduled", true);
    }

}
