package com.xdu.formteamtalent.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.UAT;
import com.xdu.formteamtalent.entity.User;
import com.xdu.formteamtalent.global.RestfulResponse;
import com.xdu.formteamtalent.entity.Team;
import com.xdu.formteamtalent.service.TeamService;
import com.xdu.formteamtalent.service.UATService;
import com.xdu.formteamtalent.service.UserService;
import com.xdu.formteamtalent.utils.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/team")
public class TeamController {
    private TeamService teamService;
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

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/add")
    @Transactional
    public RestfulResponse addTeam(HttpServletRequest request, @RequestBody Team team) {
        String u_id = WxUtil.getOpenId(request);
        String t_id = IdUtil.simpleUUID();
        team.setT_leader_id(u_id);
        team.setT_id(t_id);
        team.setT_count(1);
        teamService.save(team);

        UAT uat = new UAT();
        uat.setA_id(team.getA_id());
        uat.setU_id(u_id);
        uat.setT_id(t_id);
        uatService.save(uat);

        team.setT_leader_id("");
        return RestfulResponse.success(team);
    }

    @PostMapping("/remove")
    public RestfulResponse removeTeam(HttpServletRequest request, @RequestParam("t_id") String t_id) {
        Team team = teamService.getOne(new QueryWrapper<Team>().eq("t_id", t_id));
        if (team.getT_leader_id().equals(WxUtil.getOpenId(request))) {
            uatService.remove(new QueryWrapper<UAT>().eq("t_id", t_id));
            teamService.removeById(t_id);
        } else {
            return RestfulResponse.fail(403, "无权删除");
        }
        return RestfulResponse.success();
    }

    @PostMapping("/update")

    public RestfulResponse updateTeam(HttpServletRequest request, @RequestBody Team team) {
        Team team1 = teamService.getOne(new QueryWrapper<Team>().eq("t_id", team.getT_id()));
        if (team1.getT_leader_id().equals(WxUtil.getOpenId(request))) {
            teamService.updateById(team);
        } else {
            return RestfulResponse.fail(403, "无权更新");
        }
        return RestfulResponse.success();
    }

    @GetMapping("/get/by_a_id")
    public RestfulResponse getTeamByA_id(@RequestParam("a_id") String a_id) {
        List<Team> list = teamService.list(new QueryWrapper<Team>().eq("a_id", a_id));
        for (Team team : list) {
            team.setT_leader_id("");
        }
        return RestfulResponse.success(list);
    }

    @GetMapping("/get/id")
    public RestfulResponse getTeamById(@RequestParam("t_id") String t_id) {
        // get team by t_id
        Team team = teamService.getOne(new QueryWrapper<Team>().eq("t_id", t_id));
        // get leader by t_leader_id
        User user = userService.getOne(new QueryWrapper<User>().eq("u_id", team.getT_leader_id()));
        String username = user.getU_name();
        if (!StringUtils.hasText(username)) {
            username = "未设置名称";
        }
        user.setU_id("");
        // get members
        List<UAT> uats = uatService.list(new QueryWrapper<UAT>().eq("t_id", t_id));
        List<User> members = new ArrayList<>();
        for (UAT uat : uats) {
            String u_id = uat.getU_id();
            User user1 = userService.getOne(new QueryWrapper<User>().eq("u_id", u_id));
            user1.setU_id("");
            members.add(user1);
        }

        Map<Object, Object> map = MapUtil.builder()
                .put("team", team)
                .put("team_leader_name", username)
                .put("members", members)
                .build();
        return RestfulResponse.success(map);
    }


    @GetMapping("/get/my")
    public RestfulResponse getMyTeam(HttpServletRequest request) {
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        wrapper.eq("t_leader_id", WxUtil.getOpenId(request));
        List<Team> list = teamService.list(wrapper);
        for (Team team : list) {
            team.setT_leader_id("");
        }
        return RestfulResponse.success(list);
    }
}
