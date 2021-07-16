package com.zxh.boot.log.util;

import com.zxh.boot.log.entity.LogDTO;

/**
 * 单线程缓存
 */
public class SessionCache {
    private static ThreadLocal<LogDTO> threadLocal = new ThreadLocal<>();

    public static <T extends LogDTO> void putCache(T t) {
        threadLocal.set(t);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getCache() {
        if (null == (T) threadLocal.get()) {
            return (T) new LogDTO();
        }
        return (T) threadLocal.get();
    }

    public static void removeCache() {
        threadLocal.remove();
    }
}