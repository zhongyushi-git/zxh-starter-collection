package com.zxh.boot.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSONObject;
import com.zxh.boot.log.annotation.Log;
import com.zxh.boot.log.entity.LogDTO;
import com.zxh.boot.log.event.LogEvent;
import com.zxh.boot.log.util.HttpUtil;
import com.zxh.boot.log.util.SessionCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Aspect
public class LogAspect {

    //成功的标志
    private final static boolean SUCCESS = true;
    //请求头的用户信息
    private final static String HEADER_USER_ID = "userId";
    private final static String HEADER_USER_NAME = "userName";

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 配置切点，拦截Controller中添加@Log注解的方法
     */
    @Pointcut("@annotation(com.zxh.boot.log.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 前置通知
     *
     * @param joinPoint
     */
    @Before("logPointCut()")
    public void doBeforeAdvice(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        LogDTO logDTO = SessionCache.getCache();
        logDTO.setRequestIp(HttpUtil.getIpAddr(request)).setBrowser(HttpUtil.getBrowser(request));
        logDTO.setOperatingSystem(HttpUtil.getOperatingSystem(request));
        logDTO.setClassPath(joinPoint.getTarget().getClass().getName());
        logDTO.setRequestUrl(URLUtil.getPath(request.getRequestURI()));
        logDTO.setHttpMethod(request.getMethod()).setHandleStatus(SUCCESS);
        Log annotation = signature.getMethod().getAnnotation(Log.class);
        logDTO.setDescription(annotation.value()).setType(annotation.type());

        Object[] args = joinPoint.getArgs();//请求参数
        String strArgs = "";
        try {
            if (!request.getContentType().contains("multipart/form-data")) {
                strArgs = JSONObject.toJSONString(args);
            }
        } catch (Exception e) {
            try {
                strArgs = Arrays.toString(args);
            } catch (Exception ex) {
                log.warn("解析参数异常", ex);
            }
        }
        logDTO.setParams(getText(strArgs));
        logDTO.setStartTime(LocalDateTime.now());
        logDTO.setCreateByUser(request.getHeader(HEADER_USER_NAME));
        logDTO.setCreateById(request.getHeader(HEADER_USER_ID));
        SessionCache.putCache(logDTO);
    }

    /**
     * 后置通知
     *
     * @param obj
     * @throws IOException
     */
    @AfterReturning(returning = "obj", pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint, Object obj) {
        handleLog(obj, null);
    }

    /**
     * 后置异常通知
     *
     * @param e
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(null, e);
    }

    /**
     * 发布事件通知
     *
     * @param logDTO
     */
    private void publishEvent(LogDTO logDTO) {
        LocalDateTime nowTime = LocalDateTime.now();
        logDTO.setFinishTime(nowTime);
        logDTO.setConsumingTime(logDTO.getStartTime().until(nowTime, ChronoUnit.MILLIS));
        applicationContext.publishEvent(new LogEvent(logDTO));
        SessionCache.removeCache();
    }

    /**
     * 日志处理
     *
     * @param result
     * @param e
     */
    private void handleLog(Object result, Exception e) {
        LogDTO logDTO = SessionCache.getCache();
        logDTO.setResult(result == null ? null : result.toString());
        if (null != e) {
            log.error("发生异常：", e);
            logDTO.setHandleStatus(!SUCCESS);
            logDTO.setExMsg(e.getMessage());
            logDTO.setExDetail(e.toString());
        }
        this.publishEvent(logDTO);
    }

    /**
     * 截取字符串
     *
     * @param val
     * @return
     */
    private String getText(String val) {
        return StrUtil.sub(val, 0, 65535);
    }

}
