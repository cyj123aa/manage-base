package com.hoolink.manage.base;

import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * @author default
 */
@EnableServiceComb
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.hoolink.manage.base.dao.mapper"})
@ComponentScan(basePackages = {"com.hoolink.manage.base","com.hoolink.sdk.aspect"})
public class ManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return RestTemplateBuilder.create();
    }

}
