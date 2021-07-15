package com.zxh.boot.config;

import com.zxh.boot.filter.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Xss配置类
 */
public class XssConfiguration {

    //配置跨站攻击过滤器
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        //过滤器祖册器，注册自定义的过滤器
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new XssFilter());
        //拦截所有的地址
        filterRegistrationBean.addUrlPatterns("/*");
        //设置过滤器的先后顺序
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}