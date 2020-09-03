package com.qcl.springboot.interview.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 雪花算法核心代码
 *
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/3
 */
@Slf4j
@Component
public class IdGeneratorSnowFlake {

    private long workerId = 0;
    private long datacenterId = 1;

    private Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);

    /**
     * post完成之后开始执行
     */
    @PostConstruct
    public void init() {
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的workerId:{}", workerId);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("当前机器的workerId获取失败:{}", e);
            workerId = NetUtil.getLocalhostStr().hashCode();
        }
    }

    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    /**
     * 方法重载以便进行重写
     *
     * @param workerId     0
     * @param datacenterId 31
     * @return
     */
    public synchronized long snowflakeId(long workerId, long datacenterId) {
        Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

    /**
     * 生成的是不的字符，类似子：73a64edf935d49520287739a66f96e06
     *
     * @return
     */
    public String simpleUUID() {
        return IdUtil.simpleUUID();
    }

    /**
     * 生成的UID是著的字符，类似子：b12b6401-6f9c-4351-b2b6-d8afc9ab9272
     *
     * @return
     */
    public String randomUUID() {
        return IdUtil.randomUUID();
    }

    public static void main(String[] args) {
        System.out.println(new IdGeneratorSnowFlake().randomUUID());
        System.out.println(new IdGeneratorSnowFlake().simpleUUID());
    }
}
