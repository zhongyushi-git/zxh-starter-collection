package com.zxh.boot.log.listener;

import com.zxh.boot.log.entity.LogDTO;
import com.zxh.boot.log.event.LogEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;

/**
 * 日志事件监听
 */
@AllArgsConstructor
public class LogListener {

    private Consumer<LogDTO> consumer;

    @Async
    @Order
    @EventListener(LogEvent.class)
    public void saveLog(LogEvent event) {
        LogDTO logDTO = (LogDTO) event.getSource();
        consumer.accept(logDTO);
    }
}
