package org.nmfw.foodietree.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.TaskScheduler;

@Configuration
public class TaskSchedulerConfig {

    @Bean(name = "notificationTaskScheduler")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("task-scheduler-");
        scheduler.initialize();
        return scheduler;
    }
    @Bean(name = "approvalTaskScheduler")
    public TaskScheduler approvalTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5); // 승인 요청 처리 전용 스레드 풀
        scheduler.setThreadNamePrefix("approval-task-scheduler-");
        scheduler.initialize();
        return scheduler;
    }
}
