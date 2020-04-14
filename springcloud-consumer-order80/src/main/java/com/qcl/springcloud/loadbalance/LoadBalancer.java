package com.qcl.springcloud.loadbalance;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/14
 */
public interface LoadBalancer {

    /**
     * 得到需要提供服务的ServerInstances
     * 负载均衡算法:
     *      rest接口第几次请求数 % 服务器集群总数量  = 实际调用服务器位置下标 (每次服务重启动后rest接口计数从1开始)
     *
     * @param serviceInstances
     * @return
     */
    ServiceInstance instances(List<ServiceInstance> serviceInstances);
}
