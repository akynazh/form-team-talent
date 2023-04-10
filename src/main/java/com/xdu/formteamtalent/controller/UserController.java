package com.xdu.formteamtalent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xdu.formteamtalent.entity.*;
import com.xdu.formteamtalent.global.RestfulResponse;
import com.xdu.formteamtalent.service.*;
import com.xdu.formteamtalent.utils.JwtUtil;
import com.xdu.formteamtalent.utils.AuthUtil;
import com.xdu.formteamtalent.utils.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {
    private final JoinRequestService joinRequestService;
    private final TeamService teamService;
    private final ActivityService activityService;
    private final UATService uatService;
    private final UserService userService;
    private final RedisHelper redisHelper;

    @Autowired
    public UserController(JoinRequestService joinRequestService,
                          TeamService teamService,
                          ActivityService activityService,
                          UATService uatService,
                          UserService userService,
                          RedisHelper redisHelper) {
        this.joinRequestService = joinRequestService;
        this.teamService = teamService;
        this.activityService = activityService;
        this.uatService = uatService;
        this.userService = userService;
        this.redisHelper = redisHelper;
    }

    /**
     * 验证用户
     *
     * @param check 验证token是否合格
     */
    @PostMapping("/auth")
    public RestfulResponse auth(@RequestBody User user,
                                @RequestParam(value = "check", defaultValue = "") String check,
                                HttpServletRequest req,
                                HttpServletResponse resp) {
        if (check.equals("1")) {
            log.info("用户携带 token 进行访问");
            if (AuthUtil.checkToken(req)) return RestfulResponse.success();
            else return RestfulResponse.fail(401, "token验证错误");
        }
        log.info("用户携带用户 id 和密码进行访问");
        if (user.getUId() == null || user.getUPwd() == null) {
            return RestfulResponse.fail(404, "缺失字段值");
        }
        if (redisHelper.getUserByUId(user.getUId()) == null) {
            log.info("新注册用户: " + user);
            userService.save(user);
        }
        String token = JwtUtil.createToken(user.getUPwd());
        resp.setHeader("auth", token);
        resp.setHeader("Access-Control-Expose-Headers", "auth");
        return RestfulResponse.success();
    }

    /**
     * 用于微信小程序端验证
     *
     * @param code  微信授权码
     * @param check 验证token是否合格
     */
    @PostMapping("/authwx")
    public RestfulResponse authwx(@RequestParam(value = "code", defaultValue = "") String code,
                                  @RequestParam(value = "check", defaultValue = "") String check,
                                  HttpServletRequest req,
                                  HttpServletResponse resp) {
        if (check.equals("1")) {
            if (AuthUtil.checkToken(req)) return RestfulResponse.success();
            else return RestfulResponse.fail(401, "token验证错误");
        }
        String openId = AuthUtil.getOpenIdByCode(code);
        if (openId != null) {
            User user = redisHelper.getUserByUId(openId);
            if (user == null) {
                user = new User();
                user.setUName("wx-user");
                user.setUSex("other");
                user.setUId(openId);
                userService.save(user);
            }
            String token = JwtUtil.createToken(openId);
            resp.setHeader("auth", token);
            resp.setHeader("Access-Control-Expose-Headers", "auth");
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail_500();
        }
    }

    @PostMapping("/update")
    public RestfulResponse update(HttpServletRequest request, @RequestBody User user) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        String userId = AuthUtil.getUserId(request);
        wrapper.eq("u_id", userId);
        userService.update(user, wrapper);
        redisHelper.removeUserCacheByUId(userId);
        return RestfulResponse.success();
    }

    /**
     * 退出某个小组（活动）
     *
     * @param tId 小组 id
     */
    @PostMapping("/leave/team")
    public RestfulResponse leaveTeam(HttpServletRequest request, @RequestParam("tId") String tId) {
        String uId = AuthUtil.getUserId(request);
        QueryWrapper<UAT> wrapper = new QueryWrapper<>();
        wrapper.eq("t_id", tId);
        wrapper.eq("u_id", uId);
        uatService.remove(wrapper);
        return RestfulResponse.success();
    }

    @GetMapping("/get/info")
    public RestfulResponse getUserInfo(HttpServletRequest request) {
        return RestfulResponse.success(redisHelper.getUserByUId(AuthUtil.getUserId(request)));
    }
}
