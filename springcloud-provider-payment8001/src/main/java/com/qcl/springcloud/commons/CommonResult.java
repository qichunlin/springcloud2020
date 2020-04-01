package com.qcl.springcloud.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用响应结果类 T对应的是具体的实体类
 *
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @description
 * @date 2020/4/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    /**
     * 编码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据对象
     */
    private T data;

    /**
     * 如果data为空的话就调用这个
     *
     * @param code
     * @param message
     */
    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }
}
