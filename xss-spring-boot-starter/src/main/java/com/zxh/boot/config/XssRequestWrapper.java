package com.zxh.boot.config;


import org.owasp.validator.html.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.InputStream;

/**
 * 对请求参数进行过滤处理
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

    private static final String ANTISAMY_FILE_NAME = "antisamy-config.xml";

    //获取策略对象
    public static Policy policy = null;

    static {
        try {
            InputStream resourceAsStream = XssRequestWrapper.class.getClassLoader().getResourceAsStream(ANTISAMY_FILE_NAME);
            policy = Policy.getInstance(resourceAsStream);
        } catch (PolicyException e) {
            e.printStackTrace();
        }
    }

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 获取所有的参数，进行过滤
     *
     * @param name 参数key,super.getParameterValues(name)可以获取对应的值
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues == null) {
            return null;
        }
        //遍历参数的值，过滤后放到新数组中
        String[] newValues = new String[parameterValues.length];
        for (int i = 0; i < parameterValues.length; i++) {
            newValues[i] = filterText(parameterValues[i]);
        }
        return newValues;
    }

    /**
     * 根据策略过滤字符信息，非法字符直接删掉
     *
     * @param text
     * @return
     */
    public String filterText(String text) {
        AntiSamy antiSamy = new AntiSamy();
        CleanResults scan = null;
        try {
            //扫描字符并按策略过滤
            scan = antiSamy.scan(text, policy);
            //得到过滤后的字符
            text = scan.getCleanHTML();
        } catch (ScanException e) {
            e.printStackTrace();
        } catch (PolicyException e) {
            e.printStackTrace();
        }
        return text;
    }
}