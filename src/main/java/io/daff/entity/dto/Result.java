package io.daff.entity.dto;

import lombok.Data;

/**
 * 响应模型
 *
 * @author daffupman
 * @since 2020/3/22
 */
@Data
public class Result<T> {

    // 本次请求的结果
    private Integer code;
    // 本次请求结果的详情
    private String msg;
    // 本次请求返回的结果集
    private T data;

    public Result() {
    }

    public Result(T data) {
        this.code = 200;
        this.msg = "success";
        this.data = data;
    }
}
