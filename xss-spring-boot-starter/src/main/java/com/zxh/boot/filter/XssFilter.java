package com.zxh.boot.filter;


import com.zxh.boot.config.XssRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Xss过滤器
 */
public class XssFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //对request进行封装
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //在放行过程中对request进行过滤后
        filterChain.doFilter(new XssRequestWrapper(request), servletResponse);
    }
}