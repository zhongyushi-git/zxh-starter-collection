package com.zxh.boot.log.event;

import com.zxh.boot.log.entity.LogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件
 */
public class LogEvent extends ApplicationEvent {
    public LogEvent(LogDTO source) {
        super(source);
    }
}
