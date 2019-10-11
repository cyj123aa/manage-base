package com.hoolink.manage.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author default
 */
@EnableDiscoveryClient
@EnableSwagger2
@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients(basePackages = {"com.hoolink.manage.base.consumer"})
@MapperScan(basePackages = {"com.hoolink.manage.base.dao.mapper"})
@ComponentScan(basePackages = {"com.hoolink.manage.base","com.hoolink.sdk.aspect"})
public class ManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }

}
