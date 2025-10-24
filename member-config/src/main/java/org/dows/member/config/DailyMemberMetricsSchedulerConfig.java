package org.dows.member.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 生成会员每日统计调度器配置
 */
@Configuration
@Slf4j
public class DailyMemberMetricsSchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler dailyMemberMetricsTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("DailyMemberMetricsTaskScheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);

        // 初始化调度器
        try {
            scheduler.initialize();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize DailyMemberMetricsTaskScheduler", e);
        }
        return scheduler;
    }
}
