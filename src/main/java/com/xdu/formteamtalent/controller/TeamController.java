package com.xdu.formteamtalent.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.*;
import com.xdu.formteamtalent.entity.RestfulResponse;
import com.xdu.formteamtalent.service.*;
import com.xdu.formteamtalent.utils.AuthUtil;
import com.xdu.formteamtalent.utils.RedisHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "小组操作接口")
/**
 * 小组操作接口
 */
public class TeamController {
    private final JoinRequestService joinRequestService;
    private final TeamService teamService;
    private final ActivityService activityService;
    private final UATService uatService;
    private final UserService userService;

    private final RedisHelper redisHelper;

    @Autowired
    public TeamController(JoinRequestService joinRequestService,
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
     * 添加小组
     *
     * @param request
     * @param team    小组
     * @return RestfulResponse 数据对象为单个小组对象
     */
    @PostMapping("/add")
    @Transactional
    @ApiOperation(value = "添加小组")
    public RestfulResponse addTeam(HttpServletRequest request, @RequestBody @ApiParam(value = "小组", required = true) Team team) {
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

        return RestfulResponse.success(team);
    }

    /**
     * 删除小组
     *
     * @param request
     * @param tId     小组编号
     * @return RestfulResponse
     */
    @PostMapping("/remove")
    @Transactional
    @ApiOperation(value = "删除小组")
    public RestfulResponse removeTeam(HttpServletRequest request, @RequestParam("tId") @ApiParam(value = "小组编号", required = true) String tId) {
        Team team = redisHelper.getTeamByTId(tId);
        if (team.getTLeaderId().equals(AuthUtil.getUserId(request))) {
            uatService.remove(new QueryWrapper<UAT>().eq("t_id", tId));
            joinRequestService.remove(new QueryWrapper<JoinRequest>().eq("t_id", tId));
            teamService.removeById(tId);
            redisHelper.removeTeamCacheByTId(tId);
        } else {
            return RestfulResponse.fail(403, "无权删除");
        }
        return RestfulResponse.success();
    }

    /**
     * 更新小组信息
     *
     * @param request
     * @param team    小组
     * @return RestfulResponse
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新小组信息")
    public RestfulResponse updateTeam(HttpServletRequest request, @RequestBody @ApiParam(value = "小组", required = true) Team team) {
        Team team1 = redisHelper.getTeamByTId(team.getTId());
        if (team1.getTLeaderId().equals(AuthUtil.getUserId(request))) {
            if (team1.getAId().equals(team.getAId())) {
                teamService.updateById(team);
                redisHelper.removeTeamCacheByTId(team.getTId());
            } else {
                return RestfulResponse.fail(404, "不能修改所属活动");
            }
        } else {
            return RestfulResponse.fail(403, "无权更新");
        }
        return RestfulResponse.success();
    }

    /**
     * 根据活动编号获取小组
     *
     * @param aId 活动编号
     * @return RestfulResponse 数据对象为小组对象组成的列表
     */
    @ApiOperation(value = "根据活动编号获取小组")
    @GetMapping("/get/byAId")
    public RestfulResponse getTeamsByAId(@RequestParam("aId") @ApiParam(value = "活动编号", required = true) String aId) {
        List<Team> list = teamService.list(new QueryWrapper<Team>().eq("a_id", aId));
        return RestfulResponse.success(list);
    }

    /**
     * 获取指定小组
     *
     * @param tId 小组编号
     * @param request
     * @return RestfulResponse 数据对象为小组, 组长名称, 小组成员对象组成的列表, 小组拥有者对象这四个对象组成的 map
     */
    @GetMapping("/get/id")
    @ApiOperation(value = "获取小组")
    public RestfulResponse getTeamById(@RequestParam("tId") @ApiParam(value = "小组编号", required = true) String tId, HttpServletRequest request) {
        Team team = redisHelper.getTeamByTId(tId);
        User leader = redisHelper.getUserByUId(team.getTLeaderId());
        String leaderName = leader.getUName();
        if (!StringUtils.hasText(leaderName)) {
            leaderName = "无名氏";
        }
        // get members
        List<UAT> uats = uatService.list(new QueryWrapper<UAT>().eq("t_id", tId));
        List<User> members = new ArrayList<>();
        for (UAT uat : uats) {
            members.add(redisHelper.getUserByUId(uat.getUId()));
        }

        boolean owner = leader.getUId().equals(AuthUtil.getUserId(request));
        Map<Object, Object> map = MapUtil.builder()
                .put("team", team)
                .put("teamLeaderName", leaderName)
                .put("members", members)
                .put("owner", owner)
                .build();
        return RestfulResponse.success(map);
    }

    /**
     * 获取我的小组
     *
     * @param request
     * @return RestfulResponse
     */
    @ApiOperation(value = "获取我的小组")
    @GetMapping("/get/my")
    public RestfulResponse getMyTeam(HttpServletRequest request) {
        QueryWrapper<UAT> wrapper = new QueryWrapper<>();
        wrapper.eq("u_id", AuthUtil.getUserId(request));
        List<UAT> list = uatService.list(wrapper);
        List<Team> teams = new ArrayList<>();
        for (UAT uat : list) {
            teams.add(redisHelper.getTeamByTId(uat.getTId()));
        }
        return RestfulResponse.success(teams);
    }
}
