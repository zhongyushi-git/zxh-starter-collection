package com.zxh.boot.log;

import com.zxh.boot.log.aspect.LogAspect;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * sys.log.enabled:是否启用日志收集，true启用.false不启用。不配置时也生效。
 */


@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "sys.log.enabled", havingValue = "true", matchIfMissing = true)
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
