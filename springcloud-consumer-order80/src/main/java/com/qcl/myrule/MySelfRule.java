package com.qcl.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/8
 */
@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule() {
        //定义为随机(默认是轮询算法)
        return new RandomRule();
    }
}
