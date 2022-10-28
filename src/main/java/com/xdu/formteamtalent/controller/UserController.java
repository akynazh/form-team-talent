package com.xdu.formteamtalent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xdu.formteamtalent.entity.*;
import com.xdu.formteamtalent.service.UserService;
import com.xdu.formteamtalent.service.UATService;
import com.xdu.formteamtalent.utils.JwtUtil;
import com.xdu.formteamtalent.utils.WxUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;
    private UATService uatService;

    @Autowired
    public void setUatService(UATService uatService) {
        this.uatService = uatService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth")
    public RestfulResponse auth(@RequestParam("code") String code, HttpServletResponse resp) {
        String openId = WxUtil.getOpenIdByCode(code);
        if (openId != null) {
            User user = userService.getOne(new QueryWrapper<User>().eq("u_open_id", openId));
            if (user == null) {
                userService.save(new User(openId));
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
    @RequiresAuthentication
    public RestfulResponse update(@RequestBody  User user) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("u_open_id", WxUtil.getOpenId());
        userService.update(user, wrapper);
        return RestfulResponse.success();
    }

    /**
     * 加入某个小组
     * @param t_id 小组id
     */
    @PostMapping("/join/team")
    @RequiresAuthentication
    public RestfulResponse joinTeam(@RequestParam("t_id") String t_id, @RequestParam("a_id") String a_id) {
        UAT uat = new UAT();
        uat.setU_id(WxUtil.getOpenId());
        uat.setA_id(a_id);
        uat.setT_id(t_id);
        uatService.save(uat);
        return RestfulResponse.success();
    }

    /**
     * 退出某个小组
     * @param t_id 小组id
     */
    @PostMapping("/leave/team")
    @RequiresAuthentication
    public RestfulResponse leaveTeam(@RequestParam("t_id") String t_id) {
        QueryWrapper<UAT> wrapper = new QueryWrapper<>();
        wrapper.eq("t_id", t_id);
        wrapper.eq("u_id", WxUtil.getOpenId());
        uatService.remove(wrapper);
        return RestfulResponse.success();
    }
}
