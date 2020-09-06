package com.qcl.springcloud.alibaba.seata.service.impl;


import com.qcl.springcloud.alibaba.seata.dao.OrderDao;
import com.qcl.springcloud.alibaba.seata.domain.Order;
import com.qcl.springcloud.alibaba.seata.service.AccountService;
import com.qcl.springcloud.alibaba.seata.service.OrderService;
import com.qcl.springcloud.alibaba.seata.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单Service
 *
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/4
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private StorageService storageService;
    @Resource
    private AccountService accountService;

    /**
     * 创建订单->调用库存服务扣减库存->调用账户服务扣减账户余额->修改订单状态
     * 简单说：下订单->扣库存->减余额->改状态
     */
    @Override
    @GlobalTransactional(name = "legend-create-order", rollbackFor = Exception.class)
    public void create(Order order) {
        log.info("----->开始新建订单");
        //1.新建订单
        orderDao.create(order);

        //2.扣减库存
        log.info("----->订单微服务开始调用库存，做扣减Count");
        storageService.decrease(order.getProductId(), order.getCount());
        log.info("----->订单微服务开始调用库存，做扣减end");

        //3.扣减账户
        log.info("----->订单微服务开始调用账户，做扣减Money");
        accountService.decrease(order.getUserId(), order.getMoney());
        log.info("----->订单微服务开始调用账户，做扣减end");

        //4.修改订单状态，从零到1,1代表已经完成
        log.info("----->修改订单状态开始");
        orderDao.update(order.getUserId(), 0);
        log.info("----->修改订单状态结束");

        log.info("----->下订单结束了，O(∩_∩)O哈哈~");

    }
}