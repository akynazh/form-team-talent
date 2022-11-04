package com.xdu.formteamtalent.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.global.RestfulResponse;
import com.xdu.formteamtalent.entity.Team;
import com.xdu.formteamtalent.service.TeamService;
import com.xdu.formteamtalent.utils.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    public RestfulResponse addTeam(HttpServletRequest request, @RequestBody Team team) {
        team.setT_leader_id(WxUtil.getOpenId(request));
        team.setT_id(IdUtil.simpleUUID());
        teamService.save(team);
        return RestfulResponse.success(team);
    }

    @PostMapping("/remove")

    public RestfulResponse removeTeam(@RequestParam("t_id") String t_id) {
        teamService.removeById(t_id);
        return RestfulResponse.success();
    }

    @PostMapping("/update")

    public RestfulResponse updateTeam(@RequestBody Team team) {
        teamService.updateById(team);
        return RestfulResponse.success();
    }

    @GetMapping("/get/by_a_id")
    public RestfulResponse getTeamByA_id(@RequestParam("a_id") String a_id) {
        List<Team> list = teamService.list(new QueryWrapper<Team>().eq("a_id", a_id));
        return RestfulResponse.success(list);
    }


    @GetMapping("/get/my")
    public RestfulResponse getMyTeam(HttpServletRequest request) {
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        wrapper.eq("t_leader_id", WxUtil.getOpenId(request));
        List<Team> list = teamService.list(wrapper);
        return RestfulResponse.success(list);
    }
}
