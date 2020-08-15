package com.qcl.springcloud.alibaba.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/8/15
 */
@Configuration
public class ApplicationContextConfig {

    /**
     * 注意：如果没有 LoadBalanced 注解会报下面错
     * There was an unexpected error (type=Internal Server Error, status=500).
     * I/O error on GET request for "http://nacos-payment-provider/payment/nacos/999": nacos-payment-provider; nested exception is java.net.UnknownHostException: nacos-payment-provider
     * org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://nacos-payment-provider/payment/nacos/999": nacos-payment-provider; nested exception is java.net.UnknownHostException: nacos-payment-provider
     *
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
