package com.fz.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 启动类
 * @author Bo.Fan
 * @date 2021/9/9 14:11
 */
@SpringBootApplication
//@MapperScan("com.fz.server.mapper")
@EnableWebMvc
@EnableScheduling
public class YebApplication implements WebMvcConfigurer {


    /**
     * 与使用swagger-bootstrap-ui 不同  knife4j 需要使用静态资源处理器处理资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    public static void main(String[] args) {
        SpringApplication.run(YebApplication.class,args);
    }
}
