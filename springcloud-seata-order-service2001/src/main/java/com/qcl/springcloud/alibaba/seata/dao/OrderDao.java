package com.qcl.springcloud.alibaba.seata.dao;

import com.qcl.springcloud.alibaba.seata.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/4
 */
@Mapper
public interface OrderDao {

    /**
     * 1.新建订单
     *
     * @param order
     */
    void create(Order order);

    /**
     * 2.修改订单状态，从零改为1
     *
     * @param userId
     * @param status
     */
    void update(@Param("userId") Long userId, @Param("status") Integer status);
}