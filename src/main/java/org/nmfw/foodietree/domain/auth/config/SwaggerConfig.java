//package org.nmfw.foodietree.domain.auth.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.List;
//
//// swagger 내용 확인하기
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//    @Bean
//    public Docket api() {
//        springfox.documentation.service.Parameter refreshTokenHeader = new ParameterBuilder()
//                .name("Refresh-Token")
//                .description("Refresh token header")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build();
//
//        List<Parameter> globalParameters = new ArrayList<>();
//        globalParameters.add(refreshTokenHeader);
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("org.nmfw.foodietree"))
//                .paths(PathSelectors.any())
//                .build()
//                .globalOperationParameters(globalParameters);
//    }
//}
