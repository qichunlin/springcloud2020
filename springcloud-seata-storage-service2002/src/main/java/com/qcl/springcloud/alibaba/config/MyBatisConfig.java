package com.qcl.springcloud.alibaba.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis扫描包配置
 *
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/6
 */
@Configuration
@MapperScan({"com.qcl.springcloud.alibaba.dao.StorageDao"})
public class MyBatisConfig {
}
