package com.qcl.springcloud.alibaba.seata.service;


import com.qcl.springcloud.alibaba.seata.domain.Order;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/4
 */
public interface OrderService {
    /**
     * 创建订单
     *
     * @param order
     */
    void create(Order order);
}
