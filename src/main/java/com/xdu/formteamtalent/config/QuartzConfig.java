package com.xdu.formteamtalent.config;

import com.xdu.formteamtalent.schedule.ActivityTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail activityJob() {
        return JobBuilder.newJob(ActivityTask.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger activityTrigger() {
        SimpleScheduleBuilder ssb = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(59)
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(activityJob())
                .withSchedule(ssb)
                .build();
    }
}
