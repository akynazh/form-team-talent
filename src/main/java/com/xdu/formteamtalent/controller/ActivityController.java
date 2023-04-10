package com.xdu.formteamtalent.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.*;
import com.xdu.formteamtalent.global.RestfulResponse;
import com.xdu.formteamtalent.mapper.ActivityMapper;
import com.xdu.formteamtalent.service.*;
import com.xdu.formteamtalent.utils.QrcodeUtil;
import com.xdu.formteamtalent.utils.AuthUtil;
import com.xdu.formteamtalent.utils.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private final Integer MAX_PUBLIC_ACTIVITY_COUNT = 100;
    private final JoinRequestService joinRequestService;
    private final TeamService teamService;
    private final ActivityService activityService;
    private final UATService uatService;
    private final UserService userService;
    private final ActivityMapper activityMapper;

    private final RedisHelper redisHelper;


    @Value("${enableCloud}")
    private Integer enableCloud;

    @Autowired
    public ActivityController(JoinRequestService joinRequestService,
                              TeamService teamService,
                              ActivityService activityService,
                              UATService uatService,
                              UserService userService,
                              ActivityMapper activityMapper,
                              RedisHelper redisHelper) {
        this.joinRequestService = joinRequestService;
        this.teamService = teamService;
        this.activityService = activityService;
        this.uatService = uatService;
        this.userService = userService;
        this.activityMapper = activityMapper;
        this.redisHelper = redisHelper;
    }


    @PostMapping("/add")
    @Transactional
    public RestfulResponse addActivity(HttpServletRequest request, @RequestBody Activity activity) {
        String aId = IdUtil.simpleUUID();
        QrcodeUtil.generateQrcodeByAId(aId);
        String visitPath = QrcodeUtil.getQrcodeVisitPathByAId(aId);
        if (enableCloud == 1) {
            visitPath = QrcodeUtil.uploadToAliByAId(aId);
        }
        if (enableCloud == 2) {
            visitPath = QrcodeUtil.uploadToTCByAId(aId);
        }
        if (visitPath.equals("")) {
            return RestfulResponse.fail(502, "服务器异常，请重试");
        }
        String uId = AuthUtil.getUserId(request);

        activity.setAHolderId(uId);
        activity.setAId(aId);
        activity.setAQrcodePath(visitPath);
        activity.setStatus(1);
        activityService.save(activity);
        return RestfulResponse.success(activity);
    }

    @PostMapping("/remove")
    @Transactional
    public RestfulResponse removeActivity(HttpServletRequest request, @RequestParam("aId") String aId) {
        Activity activity = redisHelper.getActivityByAId(aId);
        if (activity != null && Objects.equals(activity.getAHolderId(), AuthUtil.getUserId(request))) {
            String qrcodePath = QrcodeUtil.getQrcodeRealPathByAId(aId);
            if (FileUtil.exist(qrcodePath)) {
                FileUtil.del(qrcodePath);
            }
            uatService.remove(new QueryWrapper<UAT>().eq("a_id", aId));
            joinRequestService.remove(new QueryWrapper<JoinRequest>().eq("a_id", aId));
            teamService.remove(new QueryWrapper<Team>().eq("a_id", aId));
            activityService.removeById(aId);
            redisHelper.removeActivityCacheByAId(aId);
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail(403, "无权限删除该活动");
        }
    }

    @PostMapping("/update")
    public RestfulResponse updateActivity(HttpServletRequest request, @RequestBody Activity activity) {
        Activity activity1 = redisHelper.getActivityByAId(activity.getAId());
        if (activity1 != null && Objects.equals(activity1.getAHolderId(), AuthUtil.getUserId(request))) {
            activityService.updateById(activity);
            redisHelper.removeActivityCacheByAId(activity.getAId());
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail(403, "无权限修改该活动");
        }
    }

    @GetMapping("/get/pub")
    public RestfulResponse getPublicActivity() {
        List<Activity> activities = activityMapper.getPublicActivity(MAX_PUBLIC_ACTIVITY_COUNT);
        return RestfulResponse.success(activities);
    }

    @GetMapping("/get/id")
    public RestfulResponse getActivityById(@RequestParam("aId") String aId, HttpServletRequest request) {
        Activity activity = redisHelper.getActivityByAId(aId);
        if (activity == null) {
            return RestfulResponse.fail(404, "不存在该活动");
        }
        boolean owner = activity.getAHolderId().equals(AuthUtil.getUserId(request));
        String qrcodePath = QrcodeUtil.getQrcodeRealPathByAId(aId);
        if (!FileUtil.exist(qrcodePath)) {
            QrcodeUtil.generateQrcodeByAId(aId);
        }
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
        String uId = AuthUtil.getUserId(request);
        List<Activity> list = activityService.list(new QueryWrapper<Activity>().eq("a_holder_id", uId));
        Set<Activity> set = new HashSet<>(list);
        // 获取我加入的活动
        List<UAT> list1 = uatService.list(new QueryWrapper<UAT>().eq("u_id", uId));
        for (UAT uat : list1) {
            Activity activity = redisHelper.getActivityByAId(uat.getAId());
            set.add(activity);
        }
        return RestfulResponse.success(set);
    }
}
