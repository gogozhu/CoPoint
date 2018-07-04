package cn.copoint.coeditor.config;

import java.util.concurrent.TimeUnit;

import cn.copoint.coeditor.handler.AudioDistributeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

@Configuration
public class ThreadPoolTaskSchedulerConfig {

    @Bean(name="threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(20);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    public PeriodicTrigger periodicFixedRateTrigger() {
        PeriodicTrigger periodicTrigger = new PeriodicTrigger(500, TimeUnit.MICROSECONDS);
        periodicTrigger.setFixedRate(true);
        periodicTrigger.setInitialDelay(500);
        return periodicTrigger;
    }
}
