package com.qcl.springboot.interview.iddemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 大厂面试题---》一般通用方案：UUID案例(只满足唯一性其他都不满足)
 *
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/3
 */
public class IDDemo {

    public static void main(String[] args) {
        SnowFlakeTest();
    }


    public static void SnowFlakeTest() {
        System.out.println("11111111111111111111111111111111111111111".length());
        System.out.println("41位的1转换成十进制后的结果:2199023255551");

        long time = 2199023255551L;
        Date date = new Date();
        System.out.println("当前时间戳:"+date.getTime());
        date.setTime(time);
        //能使用到:2039-09-07   开始1970-01-01
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(date));
        //69
        System.out.println(2039 - 1970);

    }

    public void UuidTest() {
        System.out.println(UUID.randomUUID().toString());
    }
}
