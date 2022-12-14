package com.xdu.formteamtalent.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.*;
import com.xdu.formteamtalent.global.RestfulResponse;
import com.xdu.formteamtalent.service.*;
import com.xdu.formteamtalent.utils.QrcodeUtil;
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

    @Value("${enableCloud}")
    private Integer enableCloud;

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
            return RestfulResponse.fail(502, "???????????????????????????");
        }
        String uId = AuthUtil.getUserId(request);

        activity.setAHolderId(uId);
        activity.setAId(aId);
        activity.setAQrcodePath(visitPath);
        activity.setStatus(1);
        activityService.save(activity);
        activity.setAHolderId("");

        return RestfulResponse.success(activity);
    }

    @PostMapping("/remove")
    @Transactional
    public RestfulResponse removeActivity(HttpServletRequest request, @RequestParam("aId") String aId) {
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("a_holder_id", AuthUtil.getUserId(request));
        wrapper.eq("a_id", aId);
        Activity activity = activityService.getOne(wrapper);
        if (activity != null) {
            String qrcodePath = QrcodeUtil.getQrcodeRealPathByAId(aId);
            if (FileUtil.exist(qrcodePath)) {
                FileUtil.del(qrcodePath);
            }
            uatService.remove(new QueryWrapper<UAT>().eq("a_id", aId));
            joinRequestService.remove(new QueryWrapper<JoinRequest>().eq("a_id", aId));
            teamService.remove(new QueryWrapper<Team>().eq("a_id", aId));
            activityService.removeById(aId);
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail(403, "????????????????????????");
        }
    }

    @PostMapping("/update")
    public RestfulResponse updateActivity(HttpServletRequest request, @RequestBody Activity activity) {
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("a_holder_id", AuthUtil.getUserId(request));
        wrapper.eq("a_id", activity.getAId());
        if (activityService.getOne(wrapper) != null) {
            activityService.updateById(activity);
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail(403, "????????????????????????");
        }
    }

    @GetMapping("/get/pub")
    public RestfulResponse getPublicActivity() {
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("a_is_public", 1);
        wrapper.eq("status", 1);
        List<Activity> activities = activityService.list(wrapper);
        for (Activity activity : activities) {
            activity.setAHolderId("");
        }
        return RestfulResponse.success(activities);
    }

    @GetMapping("/get/id")
    public RestfulResponse getActivityById(@RequestParam("aId") String aId, HttpServletRequest request) {
        Activity activity = activityService.getOne(new QueryWrapper<Activity>().eq("a_id", aId));
        boolean owner = activity.getAHolderId().equals(AuthUtil.getUserId(request));
        activity.setAHolderId("");
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
     * ?????????????????????????????????
     */
    @GetMapping("/get/my")
    public RestfulResponse getMyActivity(HttpServletRequest request) {
        // ????????????????????????
        String uId = AuthUtil.getUserId(request);
        List<Activity> list = activityService.list(new QueryWrapper<Activity>().eq("a_holder_id", uId));
        Set<Activity> set = new HashSet<>(list);
        // ????????????????????????
        List<UAT> list1 = uatService.list(new QueryWrapper<UAT>().eq("u_id", uId));
        for (UAT uat : list1) {
            Activity activity = activityService.getOne(new QueryWrapper<Activity>().eq("a_id", uat.getAId()));
            set.add(activity);
        }
        for (Activity activity: set) {
            activity.setAHolderId("");
        }
        return RestfulResponse.success(set);
    }
}
