package com.xdu.formteamtalent.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.*;
import com.xdu.formteamtalent.entity.RestfulResponse;
import com.xdu.formteamtalent.mapper.ActivityMapper;
import com.xdu.formteamtalent.service.*;
import com.xdu.formteamtalent.utils.QrcodeUtil;
import com.xdu.formteamtalent.utils.AuthUtil;
import com.xdu.formteamtalent.utils.RedisHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/activity")
@Api(tags = "活动操作接口")
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


    /**
     * 添加活动
     *
     * @param request
     * @param activity 活动
     * @return RestfulResponse 数据对象为单个活动对象
     */
    @PostMapping("/add")
    @Transactional
    @ApiOperation(value = "添加活动")
    public RestfulResponse addActivity(HttpServletRequest request,
                                       @RequestBody @ApiParam(value = "活动", required = true) Activity activity) {
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

    /**
     * 删除活动
     *
     * @param request
     * @param aId     活动编号
     * @return RestfulResponse
     */
    @PostMapping("/remove")
    @Transactional
    @ApiOperation(value = "删除活动")
    public RestfulResponse removeActivity(HttpServletRequest request,
                                          @RequestParam("aId") @ApiParam(value = "活动编号", required = true) String aId) {
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

    /**
     * 更新活动
     *
     * @param request
     * @param activity 活动
     * @return RestfulResponse
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新活动")
    public RestfulResponse updateActivity(HttpServletRequest request,
                                          @RequestBody @ApiParam(value = "活动", required = true) Activity activity) {
        Activity activity1 = redisHelper.getActivityByAId(activity.getAId());
        if (activity1 != null && Objects.equals(activity1.getAHolderId(), AuthUtil.getUserId(request))) {
            activityService.updateById(activity);
            redisHelper.removeActivityCacheByAId(activity.getAId());
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail(403, "无权限修改该活动");
        }
    }

    /**
     * 获取公共活动
     *
     * @return RestfulResponse 数据对象为活动对象列表
     */
    @GetMapping("/get/pub")
    @ApiOperation(value = "获取公共活动")
    public RestfulResponse getPublicActivity() {
        List<Activity> activities = activityMapper.getPublicActivity(MAX_PUBLIC_ACTIVITY_COUNT);
        return RestfulResponse.success(activities);
    }

    /**
     * 获取指定活动
     *
     * @param aId     活动编号
     * @param request
     * @return RestfulResponse 数据对象为单个活动对象，活动拥有者组成的 map
     */
    @GetMapping("/get/id")
    @ApiOperation(value = "获取指定活动")
    public RestfulResponse getActivityById(@RequestParam("aId") @ApiParam(value = "活动编号", required = true) String aId,
                                           HttpServletRequest request) {
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
     *
     * @param request
     * @return RestfulResponse
     */
    @GetMapping("/get/my")
    @ApiOperation(value = "获取我加入或创建的活动")
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
