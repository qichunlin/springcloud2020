package com.qcl.springcloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * config配置类
 * <p>
 * Bean注解等价于
 * applicationContext.xml  <bean id="" class=""></bean>
 *
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/3
 */
@Configuration
public class ApplicationContextConfig {

    /**
     * LoadBalanced注解赋予 RestTemplate负载均衡能力
     *
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
