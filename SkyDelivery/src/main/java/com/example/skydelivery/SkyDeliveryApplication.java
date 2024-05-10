package com.example.skydelivery;

import com.example.skydelivery.config.WebMvcConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@Slf4j
@EnableTransactionManagement //开启注解方式的事务管理
@EnableWebMvc
@EnableCaching
@EnableScheduling   //开启任务调度
//@MapperScan("com.example.skydelivery.mapper")
public class SkyDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyDeliveryApplication.class, args);
        log.info("服务已启动");
    }

}
