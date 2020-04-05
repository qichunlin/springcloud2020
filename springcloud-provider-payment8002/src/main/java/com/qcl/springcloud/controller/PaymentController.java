package com.qcl.springcloud.controller;

import com.qcl.springcloud.commons.CommonResult;
import com.qcl.springcloud.entities.Payment;
import com.qcl.springcloud.service.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/5
 */
@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    /**
     * 因为做了支付服务的集群,applicationName是一样的,但是可以通过端口号来区别是哪个服务
     */
    @Value("${server.port}")
    private String serverPort;

    @PostMapping("/create")
    public CommonResult create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info("*****插入结果result:{}", result);

        if (result > 0) {
            return new CommonResult(200, "插入数据库成功,serverPort:" + serverPort, result);
        } else {
            return new CommonResult(444, "插入数据库失败,serverPort:" + serverPort, null);
        }
    }


    @GetMapping("/query/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment result = paymentService.getPaymentById(id);
        log.info("*****查询结果result:{}", result);

        if (result != null) {
            return new CommonResult(200, "查询成功,serverPort:" + serverPort, result);
        } else {
            return new CommonResult(444, "查询失败,没有对应id:{}记录,serverPort:" + serverPort, null);
        }
    }

    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping("/discovery")
    public Object discovery(){
        return this.discoveryClient;
    }
}
