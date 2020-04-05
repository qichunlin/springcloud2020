package com.qcl.springcloud.controller;

import com.qcl.springcloud.commons.CommonResult;
import com.qcl.springcloud.entities.Payment;
import com.qcl.springcloud.service.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/2
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


    /**
     * 对外暴露的服务发现的一些信息
     */
    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping("/discovery")
    public Object discovery() {
        //获取EurekaServer上面所有的Application
        List<String> discoveryClientServices = discoveryClient.getServices();
        //SPRINGCLOUD-ORDER-SERVICE  SPRINGCLOUD-PAYMENT-SERVICE
        for (String application : discoveryClientServices) {
            log.info("******application******:{}", application);
        }

        //根据服务名称获取对应的实例信息
        List<ServiceInstance> discoveryClientInstances = discoveryClient.getInstances("SPRINGCLOUD-PAYMENT-SERVICE");
        // payment8002 , payment8001
        for (ServiceInstance serviceInstance : discoveryClientInstances) {
            log.info("******serviceInstance******:{}", serviceInstance);
            log.info("实例名称:{},主机名称:{},端口号:{},URI地址:{}",
                    serviceInstance.getServiceId(),
                    serviceInstance.getHost(),
                    serviceInstance.getPort(),
                    serviceInstance.getUri()
            );
        }

        return this.discoveryClient;
    }
}
