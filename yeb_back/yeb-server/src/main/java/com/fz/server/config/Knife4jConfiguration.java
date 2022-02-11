package com.fz.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/02/10/16:59
 * @Description:
 */
@Configuration
//@EnableSwagger2
//@EnableKnife4j
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    // 增强功能必须yml 和 该扩展配合使用，否则无法生效
//    private final OpenApiExtensionResolver openApiExtensionResolver;
//
//    @Autowired
//    public Knife4jConfiguration(OpenApiExtensionResolver openApiExtensionResolver) {
//        this.openApiExtensionResolver = openApiExtensionResolver;
//    }

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fz.server.controller"))
                .paths(PathSelectors.any())
                .build()
                // 设置不拦截接口文档访问 jwt接口
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes())
                ;

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("云E办接口文档")
                .description("云E办接口文档")
                .contact(new Contact("范博", "http://localhost:8082/doc.html", "1939553481@qq.com"))
                .version("1.0")
                .build()
                ;
    }
    private List<ApiKey> securitySchemes(){
        // 设置请求头信息
        ArrayList<ApiKey> apiKeys = new ArrayList<>();
        ApiKey apiKey = new ApiKey(tokenHeader,tokenHeader,"Header");
        apiKeys.add(apiKey);
        System.out.println("接口文档所携带的请求头信息为："+apiKey.toString());
        return  apiKeys;
    }

    private List<SecurityContext> securityContexts(){
        //需要登录认证的路径
        ArrayList<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(getContextPath("/hello/.*"));
        return  securityContexts;
    }
    private SecurityContext getContextPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }
    private  List<SecurityReference> defaultAuth(){
        ArrayList<SecurityReference> securityReferences = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        securityReferences.add(new SecurityReference(tokenHeader,authorizationScopes));
        return  securityReferences;
    }
}
