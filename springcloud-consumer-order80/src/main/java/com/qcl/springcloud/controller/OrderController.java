package com.qcl.springcloud.controller;

import com.qcl.springcloud.commons.CommonResult;
import com.qcl.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/3
 */
@Slf4j
@RestController
@RequestMapping("/consumer")
public class OrderController {

    //public static final String PAYMENT_URL = "http://localhost:8001";
    public static final String PAYMENT_URL = "http://SPRINGCLOUD-PAYMENT-SERVICE";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/api/payment/create")
    public CommonResult<Payment> create(Payment payment) {
        log.info("******传输的对象信息******:{}", payment);
        return restTemplate.postForObject(PAYMENT_URL + "/api/payment/create", payment, CommonResult.class);
    }

    @GetMapping("/api/payment/query/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/api/payment/query/" + id, CommonResult.class);
    }
}
