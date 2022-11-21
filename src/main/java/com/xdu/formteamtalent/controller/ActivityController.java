package com.xdu.formteamtalent.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.Activity;
import com.xdu.formteamtalent.entity.JoinRequest;
import com.xdu.formteamtalent.entity.Team;
import com.xdu.formteamtalent.entity.UAT;
import com.xdu.formteamtalent.global.RestfulResponse;
import com.xdu.formteamtalent.service.*;
import com.xdu.formteamtalent.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private final JoinRequestService joinRequestService;
    private final TeamService teamService;
    private final ActivityService activityService;
    private final UATService uatService;
    private final UserService userService;
    @Value("${staticPath}")
    private String staticPath;

    @Autowired
    public ActivityController(JoinRequestService joinRequestService,
                                 TeamService teamService,
                                 ActivityService activityService,
                                 UATService uatService,
                                 UserService userService) {
        this.joinRequestService = joinRequestService;
        this.teamService = teamService;
        this.activityService = activityService;
        this.uatService = uatService;
        this.userService = userService;
    }

    private String getQrcodePath(String a_id) {
        String a_real_qrcode_path = "/qrcode/" + a_id + ".jpg";
        if (!staticPath.equals("")) {
            return staticPath + a_real_qrcode_path; // 绝对路径
        } else {
            return "static" + a_real_qrcode_path; // 相对路径，默认相对于类路径
        }
    }

    @PostMapping("/add")
    @Transactional
    public RestfulResponse addActivity(HttpServletRequest request, @RequestBody Activity activity) {
        String a_id = IdUtil.simpleUUID();
        // 二维码指向的URL地址
        String a_qrcode_des_url = "/pages/page_activity/detail/detail?a_id=" + a_id;
        // 二维码图片在主机上的绝对路径地址
        String a_real_qrcode_path = getQrcodePath(a_id);
        // 访问二维码图片的URL地址
        String a_qrcode_visit_path = "/qrcode/" + a_id + ".jpg";
        String u_id = AuthUtil.getUserId(request);

        FileUtil.touch(a_real_qrcode_path);
        // FileUtil.file() 当地址为相对地址时默认相对与ClassPath!
        QrCodeUtil.generate(a_qrcode_des_url, 300, 300, FileUtil.file(a_real_qrcode_path));
        activity.setA_holder_id(u_id);
        activity.setA_id(a_id);
        activity.setA_qrcode_path(a_qrcode_visit_path);
        activity.setStatus(1);
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
            String qrcodePath = getQrcodePath(a_id);
            FileUtil.del(qrcodePath);
            uatService.remove(new QueryWrapper<UAT>().eq("a_id", a_id));
            joinRequestService.remove(new QueryWrapper<JoinRequest>().eq("a_id", a_id));
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
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("a_is_public", 1);
        wrapper.eq("status", 1);
        List<Activity> activities = activityService.list(wrapper);
        for (Activity activity : activities) {
            activity.setA_holder_id("");
        }
        return RestfulResponse.success(activities);
    }

    @GetMapping("/get/id")
    public RestfulResponse getActivityById(@RequestParam("a_id") String a_id, HttpServletRequest request) {
        Activity activity = activityService.getOne(new QueryWrapper<Activity>().eq("a_id", a_id));
        boolean owner = activity.getA_holder_id().equals(AuthUtil.getUserId(request));
        activity.setA_holder_id("");
        Map<Object, Object> map = MapUtil.builder()
                .put("activity", activity)
                .put("owner", owner)
                .build();
        return RestfulResponse.success(map);
    }

    /**
     * 获取我加入或创建的活动
     */
    @GetMapping("/get/my")
    public RestfulResponse getMyActivity(HttpServletRequest request) {
        // 获取我创建的活动
        String u_id = AuthUtil.getUserId(request);
        List<Activity> list = activityService.list(new QueryWrapper<Activity>().eq("a_holder_id", u_id));
        Set<Activity> set = new HashSet<>(list);
        // 获取我加入的活动
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
