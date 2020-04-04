package com.qcl.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/4
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaMain7002 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaMain7002.class);
    }
}
