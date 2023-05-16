package com.xdu.formteamtalent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xdu.formteamtalent.entity.*;
import com.xdu.formteamtalent.entity.RestfulResponse;
import com.xdu.formteamtalent.service.*;
import com.xdu.formteamtalent.utils.JwtUtil;
import com.xdu.formteamtalent.utils.AuthUtil;
import com.xdu.formteamtalent.utils.RedisHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping("/api/user")
@Api(tags = "用户操作接口")
/**
 * 用户操作接口
 */
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
     * web 端验证用户
     *
     * @param user  用户
     * @param check 若值为 1 则验证 token 是否合格
     * @param req
     * @param resp
     * @return RestfulResponse
     */
    @PostMapping("/auth")
    @ApiOperation(value = "web 端验证用户", notes = "验证用户, 如果携带了 token 则验证 token, 否则验证请求体中的用户 id 和密码")
    public RestfulResponse auth(@RequestBody @ApiParam(value = "用户") User user,
                                @RequestParam(value = "check", defaultValue = "0") @ApiParam(value = "是否携带 token, 1 是 | 0 否, 默认为否", required = true) String check,
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
        User dbUser = redisHelper.getUserByUId(user.getUId());
        if (dbUser == null) {
            log.info("新注册用户: " + user);
            userService.save(user);
        } else {
            if (!dbUser.getUPwd().equals(user.getUPwd())) {
                log.info("密码错误");
                return RestfulResponse.fail(403, "密码错误");
            }
        }
        String token = JwtUtil.createToken(user.getUId());
        resp.setHeader("auth", token);
        resp.setHeader("Access-Control-Expose-Headers", "auth");
        return RestfulResponse.success();
    }

    /**
     * 用于微信小程序端验证
     *
     * @param code  微信授权码
     * @param check 若值为 1 则验证 token 是否合格
     * @param req
     * @param resp
     * @return RestfulResponse
     */
    @PostMapping("/authwx")
    @ApiOperation(value = "用于微信小程序端验证", notes = "验证用户, 如果携带了 token 则验证 token, 否则验证微信授权码")
    public RestfulResponse authwx(@RequestParam(value = "code", defaultValue = "") @ApiParam(value = "微信授权码") String code,
                                  @RequestParam(value = "check", defaultValue = "") @ApiParam(value = "是否携带 token, 1 是 | 0 否", required = true) String check,
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

    /**
     * 更新用户信息
     *
     * @param request
     * @param user
     * @return RestfulResponse
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新用户信息")
    public RestfulResponse update(HttpServletRequest request, @RequestBody @ApiParam(value = "用户", required = true) User user) {
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
     * @param request
     * @param tId     小组 id
     * @return RestfulResponse
     */
    @PostMapping("/leave/team")
    @ApiOperation(value = "退出某个小组（活动）")
    public RestfulResponse leaveTeam(HttpServletRequest request, @RequestParam("tId") @ApiParam(value = "小组编号", required = true) String tId) {
        String uId = AuthUtil.getUserId(request);
        QueryWrapper<UAT> wrapper = new QueryWrapper<>();
        wrapper.eq("t_id", tId);
        wrapper.eq("u_id", uId);
        uatService.remove(wrapper);
        return RestfulResponse.success();
    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return RestfulResponse 数据对象为单个用户对象
     */
    @GetMapping("/get/info")
    @ApiOperation(value = "获取用户信息")
    public RestfulResponse getUserInfo(HttpServletRequest request) {
        System.out.println(request.toString());
        return RestfulResponse.success(redisHelper.getUserByUId(AuthUtil.getUserId(request)));
    }
}
