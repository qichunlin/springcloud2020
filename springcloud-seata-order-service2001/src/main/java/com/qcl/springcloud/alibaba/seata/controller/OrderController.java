package com.qcl.springcloud.alibaba.seata.controller;

import com.qcl.springcloud.alibaba.seata.domain.CommonResult;
import com.qcl.springcloud.alibaba.seata.domain.Order;
import com.qcl.springcloud.alibaba.seata.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/4
 */
@RestController
public class OrderController {

    @Resource
    private OrderService orderService;


    /**
     * 订单创建
     *
     * @param order
     * @return
     */
    @GetMapping("/order/create")
    public CommonResult create(Order order) {
        orderService.create(order);
        return new CommonResult(200, "订单创建成功");
    }
}
