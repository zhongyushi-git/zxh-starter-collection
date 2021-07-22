package com.zxh.boot.log.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 系统日志对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LogDTO{

    /**
     * 请求的ip
     */
    private String requestIp;

    /**
     * 操作类型,登录日志还是操作日志
     */
    private String type;

    /**
     * 请求的url
     */
    private String requestUrl;

    /**
     * 类路径
     */
    private String classPath;

    /**
     * 请求类型
     */
    private String httpMethod;

    /**
     * 操作状态，true成功，false失败（发生异常）
     */
    private Boolean handleStatus;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建人用户名
     */
    private String createByUser;

    /**
     * 创建人id
     */
    private String createById;

    /**
     * 异常原因
     */
    private String exMsg;

    /**
     * 异常详细信息
     */
    private String exDetail;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 所用时间
     */
    private Long consumingTime;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统名称
     */
    private String operatingSystem;

}
