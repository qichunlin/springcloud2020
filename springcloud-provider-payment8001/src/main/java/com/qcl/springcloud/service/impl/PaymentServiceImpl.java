package com.qcl.springcloud.service.impl;

import com.qcl.springcloud.entities.Payment;
import com.qcl.springcloud.mapper.PaymentMapper;
import com.qcl.springcloud.service.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/2
 */
@Slf4j
@Service
public class PaymentServiceImpl implements IPaymentService {

    @Resource
    private PaymentMapper paymentMapper;


    @Override
    public int create(Payment payment) {
        return paymentMapper.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentMapper.getPaymentById(id);
    }
}
