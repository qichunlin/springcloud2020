package com.qcl.springcloud.alibaba.service;

import com.qcl.springcloud.commons.CommonResult;
import com.qcl.springcloud.entities.Payment;
import org.springframework.stereotype.Component;

/**
 * OpenFeign服务降级类
 *
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/3
 */
@Component
public class PaymentFallbackService implements PaymentService {

    @Override
    public CommonResult<Payment> paymentSQL(Long id) {
        return new CommonResult<>(44444, "服务降级返回,---PaymentFallbackService", new Payment(id, "errorSerial"));
    }
}
