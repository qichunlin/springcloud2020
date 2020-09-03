package com.qcl.springboot.interview.service;

import com.qcl.springboot.interview.config.IdGeneratorSnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/3
 */
@Service
public class SnakeFlakeTestService {

    @Autowired
    private IdGeneratorSnowFlake idGeneratorSnowFlake;

    public String getSnowFlake() {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        for (int i = 0; i <= 20; i++) {
            threadPool.submit(()->{
                System.out.println(idGeneratorSnowFlake.snowflakeId());
            });
        }
        //关闭线程
        threadPool.shutdown();
        return "hello snowflake";
    }
}
