package com.qcl.springcloud.alibaba.myhandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.qcl.springcloud.commons.CommonResult;

/**
 * 限流处理逻辑
 *
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/9/2
 */
public class CustomerBlockHandler {

    /**
     * 限流处理逻辑异常1
     *
     * @param exception
     * @return
     */
    public static CommonResult handlerException(BlockException exception) {
        return new CommonResult(6666, "按客戶自定义,global handlerException----1");
    }

    /**
     * 限流处理逻辑异常2
     *
     * @param exception
     * @return
     */
    public static CommonResult handlerException2(BlockException exception) {
        return new CommonResult(6666, "按客戶自定义,global handlerException----2");
    }
}
