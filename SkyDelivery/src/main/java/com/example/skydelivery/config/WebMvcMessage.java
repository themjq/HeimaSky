//package com.example.skydelivery.config;
//
//import com.example.skydelivery.json.JacksonObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//import java.util.List;
//
//
//@Slf4j
//@Configuration
//public class WebMvcMessage extends WebMvcConfigurationSupport {
//
//    /**
//     * 消息转换，更改日期格式
//     */
////    @Override
////    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
////        log.info("已拓展消息转换器，功能:更改日期显示格式");
////        MappingJackson2CborHttpMessageConverter converter = new MappingJackson2CborHttpMessageConverter();
////        converter.setObjectMapper(new JacksonObjectMapper());
////        converters.add(0,converter);//0代表加载顺序
////    }
//    @Override
//    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//            log.info("已拓展消息转换器，功能:更改日期显示格式");
//            MappingJackson2HttpMessageConverter converter=new MappingJackson2HttpMessageConverter();
//            converter.setObjectMapper(new JacksonObjectMapper());
//            converters.add(0,converter);
//            }
//}
/**
 *
 * 在Spring Boot中，如果你有多个类继承了WebMvcConfigurationSupport，只有一个类会生效，因为WebMvcConfigurationSupport是一个配置类，而在Spring中，只有一个配置类会被加载。
 *
 * 如果你想让多个类都生效，你可以使用@Import注解来导入这些配置类，然后在其中一个配置类中重写extendMessageConverters方法，并在其中调用其他配置类的extendMessageConverters方法
 */