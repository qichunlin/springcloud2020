package com.qcl.springcloud.controller;

import com.qcl.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/6
 */
@Slf4j
@RestController
public class OrderZKController {

    public static final String PAYMENT_URL = "http://springcloud-provider-payment";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/zk")
    public String create(Payment payment) {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/zk", String.class);
    }
}
