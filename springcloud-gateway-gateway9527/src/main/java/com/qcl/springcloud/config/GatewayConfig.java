package com.qcl.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/19
 */
@Component
public class GatewayConfig {


    /**
     * 第一种是通过yml配置的方式
     * 第二种通过硬编码配置路由的方式
     *
     * @param routeLocatorBuilder
     * @return
     */
    @Bean
    public RouteLocator customerRouteLocatorGuoNei(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        //http://localhost:9527/guonei 转发到  --->  http://news.baidu.com/guonei
        routes.route("path_route_qcl:",
                r -> r.path("/guonei")
                        //转发到的外网地址
                        .uri("http://news.baidu.com/guonei"))
                .build();
        return routes.build();
    }


    @Bean
    public RouteLocator customerRouteLocatorGuoJi(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        //http://localhost:9527/guoji 转发到  --->  http://news.baidu.com/guoji
        routes.route("path_route_qcl:",
                r -> r.path("/guoji")
                        //转发到的外网地址
                        .uri("http://news.baidu.com/guoji"))
                .build();
        return routes.build();
    }
}
