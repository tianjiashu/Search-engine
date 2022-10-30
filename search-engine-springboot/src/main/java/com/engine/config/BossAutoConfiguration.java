package com.engine.config;

import com.engine.common.SnowflakeManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BossAutoConfiguration {

    @Value("${snowflake.machine-id}")
    private long machineId;

    @Value("${snowflake.data-center-id}")
    private long dataCenterId;

    @Bean
    @ConditionalOnMissingBean
    public SnowflakeManager snowflakeManager() {
        return new SnowflakeManager(machineId, dataCenterId);
    }
}