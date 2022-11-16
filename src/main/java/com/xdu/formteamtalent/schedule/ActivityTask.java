package com.xdu.formteamtalent.schedule;

import com.xdu.formteamtalent.entity.Activity;
import com.xdu.formteamtalent.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ActivityTask extends QuartzJobBean {
    private final ActivityService activityService;

    @Autowired
    public ActivityTask(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        List<Activity> activities = activityService.list();
        for (Activity activity : activities) {
            if (Long.parseLong(activity.getA_end_date()) <= System.currentTimeMillis()) {
                activity.setStatus(0);
                activityService.updateById(activity);
            }
        }
    }
}
