package com.example.skydelivery.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.hutool.core.util.RandomUtil;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class HelperConfiguration{
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if(openApi.getTags()!=null){
                openApi.getTags().forEach(tag -> {
                    Map<String,Object> map =new HashMap<>();
                    map.put("x-order", RandomUtil.randomInt(0,100));
                    tag.setExtensions(map);
                });
            }
            if(openApi.getPaths()!=null){
                openApi.addExtension("x-test123","333");
                openApi.getPaths().addExtension("x-abb",RandomUtil.randomInt(1,100));
            }
        };
    }
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("XXX用户系统API")
                        .version("1.0")

                        .description( "Knife4j集成springdoc-openapi示例")
                        .termsOfService("http://doc.xiaominfo.com")
                        .license(new License().name("Apache 2.0")
                                .url("http://doc.xiaominfo.com")));
    }
    ////http://127.0.0.1:8080/swagger-ui/index.html   访问网址
}
















/*


import com.example.skydelivery.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
@Configuration
@Slf4j
public class MvcWebConfiguration extends WebMvcConfigurationSupport {
    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

//
//     * 注册自定义拦截器
//     * @return
//

import org.springframework.context.annotation.Bean;

protected void addInterceptors(InterceptorRegistry registry){
        log.info("开始注册自定义拦截器");
        registry.addInterceptor(jwtTokenAdminInterceptor)
        .addPathPatterns("/admin/**")
        .excludePathPatterns("/admin/employee/login");
        }
@Bean
public Docket docket(){
        ApiInfo apiInfo=new ApiInfoBuilder().title("接口文档").version("2.0").description("接口文档").build();
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo).select()
        .apis(RequestHandlerSelectors.basePackage("com.example.skydelivery.controller"))
        .paths(PathSelectors.any()).build();
        return docket;
        }
//
// * 静态资源映射
//
@Override
protected void addResourceHandlers(ResourceHandlerRegistry registry){
        log.info("开始设置静态资源映射");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars");
        }

        }
 */
