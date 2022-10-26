package com.xdu.formteamtalent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.RestfulResponse;
import com.xdu.formteamtalent.entity.Team;
import com.xdu.formteamtalent.entity.UserTeam;
import com.xdu.formteamtalent.service.TeamService;
import com.xdu.formteamtalent.service.UserTeamService;
import com.xdu.formteamtalent.utils.WxUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
public class TeamController {
    private TeamService teamService;

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/add")
    @RequiresAuthentication
    public RestfulResponse addTeam(@RequestBody Team team) {
        team.setT_leader_id(WxUtil.getOpenId());
        teamService.save(team);
        return RestfulResponse.success(team);
    }

    @PostMapping("/remove")
    @RequiresAuthentication
    public RestfulResponse removeTeam(@RequestParam("t_id") Long t_id) {
        teamService.removeById(t_id);
        return RestfulResponse.success();
    }

    @PostMapping("/update")
    @RequiresAuthentication
    public RestfulResponse updateTeam(@RequestBody Team team) {
        teamService.updateById(team);
        return RestfulResponse.success();
    }

    @RequiresAuthentication
    @GetMapping("/get/my")
    public RestfulResponse getMyTeam() {
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        wrapper.eq("t_leader_id", WxUtil.getOpenId());
        List<Team> list = teamService.list(wrapper);
        return RestfulResponse.success(list);
    }
}
