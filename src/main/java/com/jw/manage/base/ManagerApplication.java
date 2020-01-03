package com.jw.manage.base;


import com.jw.sdk.annotation.EnableUserInfoTransmitter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * @author chenyuejun
 */

/*
 *  @EnableEurekaClient只适用于Eureka作为注册中心，@EnableDiscoveryClient 可以是其他注册中心
 */
@EnableDiscoveryClient
/*
 *SPringboot 启动注解
 */
@SpringBootApplication
/*
 * 开启数据库事务，在方法上@Transactional 便可
 */
@EnableTransactionManagement

/*
 * 启用feign客户端 扫描对应包
 */
@EnableFeignClients(basePackages = {"com.jw.manage.base.consumer"})
/*
 * 开启数据库扫描
 */
@MapperScan(basePackages = {"com.jw.manage.base.dao.mapper"})
/**
 * @ComponentScan告诉Spring 哪个packages 的用注解标识的类 会被spring自动扫描并且装入bean容器。
 */
@ComponentScan(basePackages = {"com.jw.manage.base","com.jw.sdk.aspect"})
/**
 * 用户信息微服务传递实现解析
 */
@EnableUserInfoTransmitter
public class ManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
