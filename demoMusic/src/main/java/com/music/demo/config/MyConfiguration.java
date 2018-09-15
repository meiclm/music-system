package com.music.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

//扩展了springMVC，映射文件
@Configuration
//@EnableWebMvc SpringBoot 项目中不管是在哪个类上使用 @EnableWebMvc 注解，都将导致所有URL访问时返回 404 错误
public class MyConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        super.addResourceHandlers(registry);F:\myResource
        registry.addResourceHandler("/upload/**").addResourceLocations("file:F:/myResource/");
    }

}
