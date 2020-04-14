package com.qcl.springcloud.loadbalance;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/14
 */
@Component
public class MyLoadBalancer implements LoadBalancer {

    /**
     * 原子整型类
     */
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 获取并递增
     *
     * @return
     */
    public final int getAndIncrement() {
        int current;
        int next;
        //自旋锁
        do {
            current = this.atomicInteger.get();
            // Integer的最大值2147.... 之后从0开始
            next = current >= 2147483647 ? 0 : current + 1;
        } while (!this.atomicInteger.compareAndSet(current, next));
        System.out.println("*************第几次访问,次数next:" + next);
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        //rest接口第几次请求数 % 服务器集群总数量  = 实际调用服务器位置下标 (每次服务重启动后rest接口计数从1开始)
        int index = getAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }
}
