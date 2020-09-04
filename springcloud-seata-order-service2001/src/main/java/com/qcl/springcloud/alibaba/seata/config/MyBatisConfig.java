package com.qcl.springcloud.alibaba.seata.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/4
 */
@Configuration
@MapperScan({"com.qcl.springcloud.alibaba.seata.dao"})
public class MyBatisConfig {
}
