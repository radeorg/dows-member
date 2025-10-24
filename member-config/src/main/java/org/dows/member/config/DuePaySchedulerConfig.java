package org.dows.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 到期未付款充值任务调度器配置
 */
@Configuration
public class DuePaySchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler duePayTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("DuePayTaskScheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);

        // 初始化调度器
        try {
            scheduler.initialize();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize duePayTaskScheduler", e);
        }
        return scheduler;
    }
}
