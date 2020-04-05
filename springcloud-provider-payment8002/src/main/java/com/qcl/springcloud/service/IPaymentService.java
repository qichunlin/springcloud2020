package com.qcl.springcloud.service;

import com.qcl.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/5
 */
public interface IPaymentService {

    /**
     * 创建
     *
     * @param payment
     * @return
     */
    public int create(Payment payment);

    /**
     * 通过id获取支付对象
     *
     * @param id
     * @return
     */
    public Payment getPaymentById(@Param("id") Long id);
}
