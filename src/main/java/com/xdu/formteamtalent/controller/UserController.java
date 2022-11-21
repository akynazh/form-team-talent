package com.xdu.formteamtalent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xdu.formteamtalent.entity.*;
import com.xdu.formteamtalent.global.RestfulResponse;
import com.xdu.formteamtalent.service.*;
import com.xdu.formteamtalent.utils.JwtUtil;
import com.xdu.formteamtalent.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final JoinRequestService joinRequestService;
    private final TeamService teamService;
    private final ActivityService activityService;
    private final UATService uatService;
    private final UserService userService;

    @Autowired
    public UserController(JoinRequestService joinRequestService,
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

    /**
     *
     * @param code 微信授权码
     * @param check 验证token是否合格
     */
    @PostMapping("/auth")
    public RestfulResponse auth(@RequestParam(value = "code", defaultValue = "") String code,
                                @RequestParam(value = "check", defaultValue = "") String check,
                                HttpServletRequest req,
                                HttpServletResponse resp){
        if (check.equals("1")) {
            if (AuthUtil.checkToken(req)) return RestfulResponse.success();
            else return RestfulResponse.fail(401, "token验证错误");
        }
        String openId = AuthUtil.getOpenIdByCode(code);
        if (openId != null) {
            User user = userService.getOne(new QueryWrapper<User>().eq("u_id", openId));
            if (user == null) {
                user = new User();
                user.setU_name("wx-user");
                user.setU_sex("other");
                user.setU_id(openId);
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
    public RestfulResponse update(HttpServletRequest request, @RequestBody  User user) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("u_id", AuthUtil.getUserId(request));
        userService.update(user, wrapper);
        return RestfulResponse.success();
    }

    /**
     * 退出某个小组（活动）
     * @param t_id 小组id
     */
    @PostMapping("/leave/team")
    public RestfulResponse leaveTeam(HttpServletRequest request, @RequestParam("t_id") String t_id) {
        String u_id = AuthUtil.getUserId(request);
        QueryWrapper<UAT> wrapper = new QueryWrapper<>();
        wrapper.eq("t_id", t_id);
        wrapper.eq("u_id", u_id);
        uatService.remove(wrapper);
        return RestfulResponse.success();
    }

    @GetMapping("/get/info")
    public RestfulResponse getUserInfo(HttpServletRequest request) {
        User user = userService.getOne(new QueryWrapper<User>().eq("u_id", AuthUtil.getUserId(request)));
        user.setU_id("");
        return RestfulResponse.success(user);
    }
}
