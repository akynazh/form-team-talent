package com.xdu.formteamtalent.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.Activity;
import com.xdu.formteamtalent.entity.Team;
import com.xdu.formteamtalent.entity.UAT;
import com.xdu.formteamtalent.global.RestfulResponse;
import com.xdu.formteamtalent.service.ActivityService;
import com.xdu.formteamtalent.service.TeamService;
import com.xdu.formteamtalent.service.UATService;
import com.xdu.formteamtalent.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private ActivityService activityService;
    private UATService uatService;
    private TeamService teamService;

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @Autowired
    public void setUatService(UATService uatService) {
        this.uatService = uatService;
    }

    @Value("${server.baseUrl}")
    private String baseUrl;

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/add")
    @Transactional
    public RestfulResponse addActivity(HttpServletRequest request, @RequestBody Activity activity) {
        String a_id = IdUtil.simpleUUID();
        String a_qrcode_url = "/pages/page_activity/detail/detail?a_id=" + a_id;
        String a_real_qrcode_path = "static/qrcode/" + a_id + ".jpg";
        String a_qrcode_path = baseUrl + "/qrcode/" + a_id + ".jpg";
        String u_id = AuthUtil.getUserId(request);

        FileUtil.touch(a_real_qrcode_path);
        QrCodeUtil.generate(a_qrcode_url, 300, 300, FileUtil.file(a_real_qrcode_path));
        activity.setA_holder_id(u_id);
        activity.setA_id(a_id);
        activity.setA_qrcode_path(a_qrcode_path);
        activityService.save(activity);
        activity.setA_holder_id("");

        return RestfulResponse.success(activity);
    }

    @PostMapping("/remove")
    @Transactional
    public RestfulResponse removeActivity(HttpServletRequest request, @RequestParam("a_id") String a_id) {
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("a_holder_id", AuthUtil.getUserId(request));
        wrapper.eq("a_id", a_id);
        Activity activity = activityService.getOne(wrapper);
        if (activity != null) {
            FileUtil.del("static/qrcode/" + a_id + ".jpg");
            uatService.remove(new QueryWrapper<UAT>().eq("a_id", a_id));
            teamService.remove(new QueryWrapper<Team>().eq("a_id", a_id));
            activityService.removeById(a_id);
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail(403, "无权限删除该活动");
        }
    }

    @PostMapping("/update")
    public RestfulResponse updateActivity(HttpServletRequest request, @RequestBody Activity activity) {
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("a_holder_id", AuthUtil.getUserId(request));
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
                    return end_timestamp >= current_timestamp;
                }).collect(Collectors.toList());
        for (Activity activity : activities) {
            activity.setA_holder_id("");
        }
        return RestfulResponse.success(activities);
    }

    @GetMapping("/get/id")
    public RestfulResponse getActivityById(@RequestParam("a_id") String a_id) {
        Activity activity = activityService.getOne(new QueryWrapper<Activity>().eq("a_id", a_id));
        activity.setA_holder_id("");
        return RestfulResponse.success(activity);
    }

    @GetMapping("/get/my")
    public RestfulResponse getMyActivity(HttpServletRequest request) {
        String u_id = AuthUtil.getUserId(request);
        List<Activity> list = activityService.list(new QueryWrapper<Activity>().eq("a_holder_id", u_id));
        Set<Activity> set = new HashSet<>(list);
        List<UAT> list1 = uatService.list(new QueryWrapper<UAT>().eq("u_id", u_id));
        for (UAT uat : list1) {
            Activity activity = activityService.getOne(new QueryWrapper<Activity>().eq("a_id", uat.getA_id()));
            set.add(activity);
        }
        for (Activity activity: set) {
            activity.setA_holder_id("");
        }
        return RestfulResponse.success(set);
    }
}
