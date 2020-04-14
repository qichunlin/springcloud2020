package com.qcl.springcloud.service;

import com.qcl.springcloud.commons.CommonResult;
import com.qcl.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/14
 */
@Component
@FeignClient(value = "SPRINGCLOUD-PAYMENT-SERVICE")
public interface PaymentFeignService {

    /**
     * 远程调用通过id获取payment对象
     *
     * @param id
     * @return
     */
    @GetMapping("/api/payment/query/{id}")
    CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

    /**
     * Feign超时控制
     *
     * @return
     */
    @GetMapping(value = "/api/payment/feign/timeout")
    String paymentFeignTimeout();
}
