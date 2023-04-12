package com.xdu.formteamtalent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.*;
import com.xdu.formteamtalent.entity.RestfulResponse;
import com.xdu.formteamtalent.mapper.JoinRequestMapper;
import com.xdu.formteamtalent.service.*;
import com.xdu.formteamtalent.utils.AuthUtil;
import com.xdu.formteamtalent.utils.RedisHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequestMapping("/api/req")
@RestController
@Api(tags = "请求操作接口")
public class JoinRequestController {
    private final JoinRequestService joinRequestService;
    private final TeamService teamService;
    private final ActivityService activityService;
    private final UATService uatService;
    private final UserService userService;
    private final JoinRequestMapper joinRequestMapper;
    private final RedisHelper redisHelper;

    @Autowired
    public JoinRequestController(JoinRequestService joinRequestService,
                                 TeamService teamService,
                                 ActivityService activityService,
                                 UATService uatService,
                                 UserService userService,
                                 JoinRequestMapper joinRequestMapper,
                                 RedisHelper redisHelper) {
        this.joinRequestService = joinRequestService;
        this.teamService = teamService;
        this.activityService = activityService;
        this.uatService = uatService;
        this.userService = userService;
        this.joinRequestMapper = joinRequestMapper;
        this.redisHelper = redisHelper;
    }

    /**
     * 发送请求
     *
     * @param joinRequest 请求
     * @param request
     * @return RestfulResponse
     */
    @PostMapping("/send")
    @ApiOperation(value = "发送请求")
    public RestfulResponse sendRequest(@RequestBody @ApiParam(value = "请求", required = true) JoinRequest joinRequest, HttpServletRequest request) {
        String requestId = joinRequest.getId(); // 请求 id 需要在前端根据 from_id to_id a_id t_id 生成，保证唯一性
        if (redisHelper.checkReqExistsByReqId(requestId)) {
            return RestfulResponse.fail(403, "请勿重复操作");
        }
        String userId = AuthUtil.getUserId(request);
        // 查看是否已经加入了
        QueryWrapper<UAT> wrapper = new QueryWrapper<>();
        wrapper.eq("a_id", joinRequest.getAId());
        wrapper.eq("t_id", joinRequest.getTId());
        wrapper.eq("u_id", userId);
        if (uatService.getOne(wrapper) != null) {
            return RestfulResponse.fail(403, "不可重复加入哦");
        }
        // 获取用户和小组详细信息
        User user = redisHelper.getUserByUId(userId);
        Team team = redisHelper.getTeamByTId(joinRequest.getTId());
        // 查看人数是否已满
        if (team.getTCount().equals(team.getTTotal())) {
            return RestfulResponse.fail(403, "人数已满=_=");
        }
        joinRequest.setAgree(0);
        joinRequest.setStatus(1);
        joinRequest.setSendDate(String.valueOf(System.currentTimeMillis()));
        joinRequest.setUName(user.getUName());
        joinRequest.setTName(team.getTName());
        joinRequest.setFromId(userId);
        joinRequest.setToId(team.getTLeaderId());
//        JoinRequest checkRequest = joinRequestMapper.checkRequestExist(joinRequest.getFromId(), joinRequest.getToId(),
//                joinRequest.getAId(), joinRequest.getTId());
        joinRequestService.save(joinRequest);
        return RestfulResponse.success();
    }

    /**
     * 处理请求
     *
     * @param id      请求编号
     * @param agree   是否同意 1 是 | 0 否
     * @param request
     * @return RestfulResponse
     */
    @PostMapping("/handle")
    @Transactional
    @ApiOperation(value = "处理请求")
    public RestfulResponse handleRequest(@RequestParam @ApiParam(value = "请求编号", required = true) String id,
                                         @RequestParam @ApiParam(value = "是否同意 1 是 | 0 否", required = true) Integer agree,
                                         HttpServletRequest request) {
        JoinRequest one = redisHelper.getJoinReqByReqId(id);
        if (one.getToId().equals(AuthUtil.getUserId(request))) {
            one.setStatus(0);
            one.setAgree(agree);
            joinRequestService.updateById(one);
            if (agree == 1) {
                QueryWrapper<UAT> wrapper = new QueryWrapper<>();
                wrapper.eq("u_id", one.getFromId());
                wrapper.eq("t_id", one.getTId());
                wrapper.eq("a_id", one.getAId());
                if (uatService.getOne(wrapper) != null) {
                    return RestfulResponse.fail(404, "不可重复加入");
                }
                UAT uat = new UAT();
                uat.setUId(one.getFromId());
                uat.setAId(one.getAId());
                uat.setTId(one.getTId());
                uatService.save(uat);

                Team team = teamService.getOne(new QueryWrapper<Team>().eq("t_id", one.getTId()));
                team.setTCount(team.getTCount() + 1);
                teamService.updateById(team);
            }
            redisHelper.removeReqCacheByReqId(one.getId());
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail(403, "没有权限");
        }
    }

    /**
     * 撤销请求
     *
     * @param id      请求编号
     * @param request
     * @return RestfulResponse
     */
    @ApiOperation(value = "撤销请求")
    @PostMapping("/remove/{id}")
    public RestfulResponse removeRequest(@PathVariable @ApiParam(value = "请求编号", required = true) String id, HttpServletRequest request) {
        JoinRequest one = redisHelper.getJoinReqByReqId(id);
        if (one != null) {
            if (one.getFromId().equals(AuthUtil.getUserId(request))) {
                joinRequestService.removeById(id);
                redisHelper.removeReqCacheByReqId(one.getId());
                return RestfulResponse.success();
            } else {
                return RestfulResponse.fail(403, "没有权限");
            }
        } else {
            return RestfulResponse.fail(404, "不存在该记录");
        }
    }

    /**
     * 获得与我相关的请求
     *
     * @param type    请求类型, 非必要，0：我发出的请求 1：发给我的请求 2：所有请求（默认）
     * @param status  请求状态, 非必要，0：已处理的请求 1：未处理的请求 2：所有请求（默认）
     * @param request 请求
     * @return RestfulResponse 数据对象为请求对象列表
     */
    @GetMapping("/get")
    @ApiOperation(value = "获得与我相关的请求")
    public RestfulResponse getMyRequest(@RequestParam(defaultValue = "2") @ApiParam(value = "请求类型, 0：我发出的请求 1：发给我的请求 2：所有请求（默认）") Integer type,
                                        @RequestParam(defaultValue = "2") @ApiParam(value = "请求状态, 0：已处理的请求 1：未处理的请求 2：所有请求（默认）") Integer status,
                                        HttpServletRequest request) {
        if ((type != 0 && type != 1 && type != 2) || (status != 0 && status != 1 && status != 2)) {
            return RestfulResponse.fail(404, "参数错误");
        }
        String uId = AuthUtil.getUserId(request);

        List<JoinRequest> requests = joinRequestMapper.getMyRequest(uId);
        if (status == 2 && type == 2) {
            return RestfulResponse.success(requests);
        }

        // 过滤出符合type的
        Predicate<JoinRequest> pType = req -> type == 0 ? req.getFromId().equals(uId) : req.getToId().equals(uId);
        // 过滤出符合status的
        Predicate<JoinRequest> pStatus = req -> req.getStatus().equals(status);

        if (status == 2)
            return RestfulResponse.success(
                    requests.stream().filter(pType).collect(Collectors.toList())
            );
        if (type == 2)
            return RestfulResponse.success(
                    requests.stream().filter(pStatus).collect(Collectors.toList())
            );
        return RestfulResponse.success(
                requests.stream().filter(pStatus).filter(pType).collect(Collectors.toList())
        );
    }
}