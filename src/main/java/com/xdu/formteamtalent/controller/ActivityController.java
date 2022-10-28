package com.xdu.formteamtalent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.Activity;
import com.xdu.formteamtalent.entity.RestfulResponse;
import com.xdu.formteamtalent.entity.Team;
import com.xdu.formteamtalent.service.ActivityService;
import com.xdu.formteamtalent.service.TeamService;
import com.xdu.formteamtalent.utils.WxUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private ActivityService activityService;
    private TeamService teamService;

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/add")
    @RequiresAuthentication
    public RestfulResponse addActivity(@RequestBody  Activity activity) {
        activity.setA_holder_id(WxUtil.getOpenId());
        activityService.save(activity);
        return RestfulResponse.success(activity);
    }

    @PostMapping("/remove")
    @RequiresAuthentication
    public RestfulResponse removeActivity(@RequestParam("a_id") String a_id) {
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("a_holder_id", WxUtil.getOpenId());
        wrapper.eq("a_id", a_id);
        if (activityService.getOne(wrapper) != null) {
            activityService.removeById(a_id);
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail(403, "无权限删除该活动");
        }
    }

    @PostMapping("/update")
    @RequiresAuthentication
    public RestfulResponse updateActivity(@RequestBody Activity activity) {
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("a_holder_id", WxUtil.getOpenId());
        wrapper.eq("a_id", activity.getA_id());
        if (activityService.getOne(wrapper) != null) {
            activityService.updateById(activity);
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail(403, "无权限修改该活动");
        }
    }

    @GetMapping("/get/pub")
    public RestfulResponse getPublicActivity() {
        List<Activity> list = activityService.list(new QueryWrapper<Activity>().eq("a_is_public", 1));
        // 获取还未结束的活动
        List<Activity> activities = list.stream()
                .filter(a -> {
                    String a_end_date = a.getA_end_date();
                    long end_timestamp = Timestamp.valueOf(a_end_date).getTime() / 1000;
                    long current_timestamp = System.currentTimeMillis() / 1000;
                    return end_timestamp <= current_timestamp;
                }).collect(Collectors.toList());
        return RestfulResponse.success(activities);
    }

    @GetMapping("/get/my")
    @RequiresAuthentication
    public RestfulResponse getMyActivity() {
        List<Activity> list = activityService.list(new QueryWrapper<Activity>().eq("a_holder_id", WxUtil.getOpenId()));
        return RestfulResponse.success(list);
    }

    /**
     * 根据活动id获取该活动对应的team
     * @param a_id 活动id
     */
    @GetMapping("/get/team")
    @RequiresAuthentication
    public RestfulResponse getActivityTeam(@RequestParam("a_id") Long a_id) {
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        wrapper.eq("a_id", a_id);
        List<Team> list = teamService.list(wrapper);
        return RestfulResponse.success(list);
    }
}
