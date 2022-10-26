package com.xdu.formteamtalent.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xdu.formteamtalent.entity.RestfulResponse;
import com.xdu.formteamtalent.entity.User;
import com.xdu.formteamtalent.security.AccountProfile;
import com.xdu.formteamtalent.service.UserService;
import com.xdu.formteamtalent.utils.JwtUtil;
import com.xdu.formteamtalent.utils.WxUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {
    private UserService userService;
    private JwtUtil jwtUtil;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/test")
    public RestfulResponse test() {
        User one = userService.getOne(new QueryWrapper<User>().eq("u_open_id", "sadjfklasdjfkasjd"));
        return RestfulResponse.success(one);
    }

    @PostMapping("/api/user/auth")
    public RestfulResponse auth(@RequestParam("code") String code, HttpServletResponse resp) {
        String openId = WxUtil.getOpenIdByCode(code);
        if (openId != null) {
            User user = userService.getOne(new QueryWrapper<User>().eq("u_open_id", openId));
            if (user == null) {
                userService.save(new User(openId));
            }
            String token = jwtUtil.createToken(openId);
            resp.setHeader("auth", token);
            resp.setHeader("Access-Control-Expose-Headers", "auth");
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail_502();
        }
    }

    @PostMapping("/api/user/update")
    @RequiresAuthentication
    public RestfulResponse update(User user) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("u_open_id", WxUtil.getOpenId());
        if (userService.update(user, wrapper)) {
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail_502();
        }
    }
}
