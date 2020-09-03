package com.qcl.springboot.interview.controller;

import com.qcl.springboot.interview.service.SnakeFlakeTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://localhost:7777/snowflake  访问即可生成20个id
 *
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/3
 */
@RestController
public class SnakeFlakeTestController {

    @Autowired
    private SnakeFlakeTestService snakeFlakeTestService;

    @GetMapping("/snowflake")
    public String getSnowFlake(){
        return snakeFlakeTestService.getSnowFlake();
    }
}
