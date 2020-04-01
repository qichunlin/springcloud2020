package com.qcl.springcloud.service;

import com.qcl.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/2
 */
@Mapper
public interface IPaymentService {

    public int create(Payment payment);

    public Payment getPaymentById(@Param("id") Long id);
}
