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


    /**
     * Sentinel降级-RT
     *
     * @return
     */
    @GetMapping("/testD")
    public String testDRT() {
        log.info(Thread.currentThread().getName() + "\t" + "...testD RT");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "------testD RT";
    }

    /**
     * Sentinel降级-异常比例
     *
     * @return
     */
    @GetMapping("/testDException")
    public String testDException() {
        log.info(Thread.currentThread().getName() + "\t" + "...testDException DEGRADE_GRADE_EXCEPTION_RATIO");
        int age = 10 / 0;
        return "------testDException DEGRADE_GRADE_EXCEPTION_RATIO";
    }


    /**
     * Sentinel降级-异常数
     *
     * @return
     */
    @GetMapping("/testE")
    public String testE() {
        log.info("testE 测试异常数");
        int age = 10 / 0;
        return "------testE 测试异常数";
    }

    /**
     * 测试热点规则(何为热点？热点即经常访问的数据。很多时候我们希望统计某个热点数据中访问频次最高的 Top K 数据，并对其访问进行限制。)
     * <p>
     * blockHandler 如果跟我们热点配置的sentinel规则的就会条调到该方法类似于兜底的方法
     *
     * @param p1
     * @param p2
     * @return
     */
    @GetMapping("/testHotKey")
    //@SentinelResource(value = "testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey")
    public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
                             @RequestParam(value = "p2", required = false) String p2) {

        //Java的runtimeException异常不属于sentinel处理的兜底处理范围内,所以还是会在页面报错 (在@SentinelResource主借里面有个fallback方法)
        //int age = 10/0;
        return "------testHotKey";
    }

    /**
     * 热点规则匹配不到对应规则兜底处理方法
     *
     * @param p1
     * @param p2
     * @param exception
     * @return
     */
    public String deal_testHotKey(String p1, String p2, BlockException exception) {
        //sentinel系统默认的提示：Blocked by Sentinel (flow limiting)
        return "------deal_testHotKey,o(╥﹏╥)o";
    }

}
