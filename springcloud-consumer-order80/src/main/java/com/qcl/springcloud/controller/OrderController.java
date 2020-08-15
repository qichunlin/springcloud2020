package com.qcl.springcloud.controller;

import com.qcl.springcloud.commons.CommonResult;
import com.qcl.springcloud.entities.Payment;
import com.qcl.springcloud.loadbalance.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

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


    @GetMapping("/api/payment/getForEntity/query/{id}")
    public CommonResult<Payment> getPayment2(@PathVariable("id") Long id) {
        //返回对象为ResponseEntity对象包含了响应的状态码\响应头
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/api/payment/query/" + id, CommonResult.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            return new CommonResult<>(444, "操作失败");
        }
    }


    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping("/discovery")
    public Object discovery() {
        return this.discoveryClient;
    }


    /**
     * 手写Load Balance 轮询算法
     */
    @Resource
    private LoadBalancer loadBalancer;

    @GetMapping(value = "/consumer/payment/lb")
    public String getPaymentLoadBalance() {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("SPRINGCLOUD-PAYMENT-SERVICE");

        if (CollectionUtils.isEmpty(serviceInstanceList)) {
            return null;
        }
        //获取提供服务的实例对象
        ServiceInstance serviceInstance = loadBalancer.instances(serviceInstanceList);
        URI uri = serviceInstance.getUri();
        return restTemplate.getForObject(uri + "/payment/lb", String.class);
    }


    /**
     * zipkin+sleuth
     *
     * @return
     */
    @GetMapping(value = "/payment/zipkin")
    public String getPaymentZipkin() {
        return restTemplate.getForObject(PAYMENT_URL+"/api/payment/sleuth/zipkin" , String.class);
    }
}
