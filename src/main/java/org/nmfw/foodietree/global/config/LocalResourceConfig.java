package org.nmfw.foodietree.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 로컬 서버에 저장된 이미지를 웹 브라우저에서 불러올 수 있도록
// 로컬 서버 파일 경로를 웹서버 url을 변경하는 설정
@Configuration
public class LocalResourceConfig implements WebMvcConfigurer {
    @Value("${env.upload.path}")
    private String rootPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
        ResourceLocations: 로컬에 있는 경로
        ResourceHandler: 해당 로컬 경로를 web url로 변환

        ex)
            User/jeongin/dog.jpg

            local 접근 - file: User/jeongin/dog.jpg
            web 접근 - http://localhoslt:8383/local/dog.jpg
         */
        registry
                .addResourceHandler("/local/**")
                .addResourceLocations("file:" + rootPath);
    }
}
