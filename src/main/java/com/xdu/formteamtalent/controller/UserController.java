package com.xdu.formteamtalent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xdu.formteamtalent.entity.RestResult;
import com.xdu.formteamtalent.entity.User;
import com.xdu.formteamtalent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/user/auth")
    public RestResult auth(@RequestParam("code") String code) {

        return RestResult.success();
    }

    @PostMapping("/api/user/update")
    public RestResult update(User user) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("u_open_id", user.getU_open_id());
        if (userService.update(user, wrapper)) {
            return RestResult.success();
        } else {
            return RestResult.fail_502();
        }
    }
}
