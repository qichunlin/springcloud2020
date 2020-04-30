package com.qcl.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端读取配置文件需要解决分布式配置文件动态刷新的问题
 * 如果github仓库上面修改配置文件3355服务每次都需要重启才能获取
 *
 * @RefreshScope 提供刷新的功能-->动态刷新文件
 *
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/30
 */
@RestController
@RefreshScope
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    /**
     * 客户端访问地址:
     * http://localhost:3355/configInfo
     * <p>
     *
     *     服务端访问地址
     * http://config-3344.com:3344/springcloudconfig/dev/master
     * <p>
     * http://config-3344.com:3344/master/springcloudconfig-dev.yml
     *
     * @return
     */
    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return configInfo;
    }
}
