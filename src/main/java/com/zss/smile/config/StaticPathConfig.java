package com.zss.smile.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author ZSS
 * @date 2021/9/9 16:23
 * @desc 静态资源配置
 */
@Slf4j
@Configuration
public class StaticPathConfig extends WebMvcConfigurationSupport {

    @Value("${resources.static.pattern}")
    private String staticPattern;
    @Value("${resources.static.location}")
    private String staticLocation;

    @Value("${resources.files.pattern}")
    private String filesPattern;
    @Value("${resources.files.location}")
    private String fileLocation;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern(staticPattern)){
            registry.addResourceHandler(staticPattern)
                    .addResourceLocations(staticLocation);
        }
        if (!registry.hasMappingForPattern(filesPattern)){
            registry.addResourceHandler(filesPattern)
                    .addResourceLocations("file:" + fileLocation);
        }
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
}
