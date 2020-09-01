package com.qcl.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/1
 */
@Slf4j
@RestController
public class FlowLimitController {

    /**
     * 由于sentinel是属于懒加载所以需要先访问  http://localhost:8401/testA  http://localhost:8401/testB 接口才能加载监控服务
     *
     * @return
     */
    @GetMapping("/testA")
    public String testA() {
        return "------testA";
    }

    /**
     * 由于sentinel是属于懒加载所以需要先访问  http://localhost:8401/testA  http://localhost:8401/testB 接口才能加载监控服务
     *
     * @return
     */
    @GetMapping("/testB")
    public String testB() {
        //测试线程数限流
        //暂停毫秒
        try {
            TimeUnit.MILLISECONDS.sleep(800);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "------testB";
    }

    /**
     * 测试关联资源 C惹事了 A挂了
     * 比如：支付的接口资源不够用了,就通过限制下订单的接口不让请求进入
     *
     * @return
     */
    @GetMapping("/testC")
    public String testC() {
        log.info(Thread.currentThread().getName() + "\t" + "...testC");
        return "------testC";
    }


    @GetMapping("/testD")
    public String testD() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("testD 测试RT");

        log.info("testD 异常比例");
        int age = 10 / 0;
        return "------testD";
    }

    @GetMapping("/testE")
    public String testE() {
        log.info("testE 测试异常数");
        int age = 10 / 0;
        return "------testE 测试异常数";
    }

    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey")
    public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
                             @RequestParam(value = "p2", required = false) String p2) {
        //int age = 10/0;
        return "------testHotKey";
    }

    public String deal_testHotKey(String p1, String p2, BlockException exception) {
        //sentinel系统默认的提示：Blocked by Sentinel (flow limiting)
        return "------deal_testHotKey,o(╥﹏╥)o";
    }

}
