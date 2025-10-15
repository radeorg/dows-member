package org.dows.member.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@RequiredArgsConstructor
@Configuration
@EnableScheduling
@Slf4j
public class DailyMemberMetricsThreadPoolConfig {
    /**
     * 生成会员每日度量任务线程池,同时最大5个并发处理
     */
    @Bean
    public ThreadPoolTaskExecutor dailyMemberMetricsTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("dailyMemberMetrics-worker-");
        executor.initialize();
        return executor;
    }
}
