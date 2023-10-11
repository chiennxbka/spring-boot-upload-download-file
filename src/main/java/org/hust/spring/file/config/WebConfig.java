package org.hust.spring.file.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /**
     * pathPattern dai dien cho resource locations voi tham so tuong ung
     * Vi du, truy cap url http://localhost:8080/spring-file/static/js/jquery-3.7.1.min.js se show file js,
     * giong nhu viec doc file
     *
     * Vi du khac, truy cap url http://localhost:8080/spring-file/public/yt3-4.png
     * se tim va doc file anh yt3-4.png trong duong dan /opt/avatar/images neu file yt3-4.png da ton tai trong thu muc
     *
     *
     * */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**", "/public/**")
                .addResourceLocations("classpath:/static/", "file:///opt/avatar/images/").setCachePeriod(5);
    }
}
