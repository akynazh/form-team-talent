package com.xdu.formteamtalent.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.JoinRequest;
import com.xdu.formteamtalent.entity.UAT;
import com.xdu.formteamtalent.entity.User;
import com.xdu.formteamtalent.global.RestfulResponse;
import com.xdu.formteamtalent.entity.Team;
import com.xdu.formteamtalent.service.*;
import com.xdu.formteamtalent.utils.AuthUtil;
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
    private final JoinRequestService joinRequestService;
    private final TeamService teamService;
    private final ActivityService activityService;
    private final UATService uatService;
    private final UserService userService;

    @Autowired
    public TeamController(JoinRequestService joinRequestService,
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
    public RestfulResponse addTeam(HttpServletRequest request, @RequestBody Team team) {
        String uId = AuthUtil.getUserId(request);
        String tId = IdUtil.simpleUUID();
        team.setTLeaderId(uId);
        team.setTId(tId);
        team.setTCount(1);
        teamService.save(team);

        UAT uat = new UAT();
        uat.setAId(team.getAId());
        uat.setUId(uId);
        uat.setTId(tId);
        uatService.save(uat);

        team.setTLeaderId("");
        return RestfulResponse.success(team);
    }

    @PostMapping("/remove")
    @Transactional
    public RestfulResponse removeTeam(HttpServletRequest request, @RequestParam("tId") String tId) {
        Team team = teamService.getOne(new QueryWrapper<Team>().eq("t_id", tId));
        if (team.getTLeaderId().equals(AuthUtil.getUserId(request))) {
            uatService.remove(new QueryWrapper<UAT>().eq("t_id", tId));
            joinRequestService.remove(new QueryWrapper<JoinRequest>().eq("t_id", tId));
            teamService.removeById(tId);
        } else {
            return RestfulResponse.fail(403, "无权删除");
        }
        return RestfulResponse.success();
    }

    @PostMapping("/update")

    public RestfulResponse updateTeam(HttpServletRequest request, @RequestBody Team team) {
        Team team1 = teamService.getOne(new QueryWrapper<Team>().eq("t_id", team.getTId()));
        if (team1.getTLeaderId().equals(AuthUtil.getUserId(request))) {
            if (team1.getAId().equals(team.getAId())) {
                teamService.updateById(team);
            } else {
                return RestfulResponse.fail(404, "不能修改所属活动");
            }
        } else {
            return RestfulResponse.fail(403, "无权更新");
        }
        return RestfulResponse.success();
    }

    @GetMapping("/get/by_a_id")
    public RestfulResponse getTeamByAId(@RequestParam("a_id") String a_id) {
        List<Team> list = teamService.list(new QueryWrapper<Team>().eq("a_id", a_id));
        for (Team team : list) {
            team.setTLeaderId("");
        }
        return RestfulResponse.success(list);
    }

    @GetMapping("/get/id")
    public RestfulResponse getTeamById(@RequestParam("tId") String tId, HttpServletRequest request) {
        Team team = teamService.getOne(new QueryWrapper<Team>().eq("t_id", tId));
        User leader = userService.getOne(new QueryWrapper<User>().eq("u_id", team.getTLeaderId()));
        String leaderName = leader.getUName();
        if (!StringUtils.hasText(leaderName)) {
            leaderName = "无名氏";
        }
        // get members
        List<UAT> uats = uatService.list(new QueryWrapper<UAT>().eq("t_id", tId));
        List<User> members = new ArrayList<>();
        for (UAT uat : uats) {
            String uId = uat.getUId();
            User user1 = userService.getOne(new QueryWrapper<User>().eq("u_id", uId));
            user1.setUId("");
            members.add(user1);
        }

        boolean owner = leader.getUId().equals(AuthUtil.getUserId(request));
        team.setTLeaderId("");
        Map<Object, Object> map = MapUtil.builder()
                .put("team", team)
                .put("teamLeaderName", leaderName)
                .put("members", members)
                .put("owner", owner)
                .build();
        return RestfulResponse.success(map);
    }

    @GetMapping("/get/my")
    public RestfulResponse getMyTeam(HttpServletRequest request) {
        QueryWrapper<UAT> wrapper = new QueryWrapper<>();
        wrapper.eq("u_id", AuthUtil.getUserId(request));
        List<UAT> list = uatService.list(wrapper);
        List<Team> teams = new ArrayList<>();
        for (UAT uat : list) {
            Team team = teamService.getOne(new QueryWrapper<Team>().eq("t_id", uat.getTId()));
            team.setTLeaderId("");
            teams.add(team);
        }
        return RestfulResponse.success(teams);
    }
}
