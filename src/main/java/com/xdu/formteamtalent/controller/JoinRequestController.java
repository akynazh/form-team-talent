package com.xdu.formteamtalent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.JoinRequest;
import com.xdu.formteamtalent.entity.Team;
import com.xdu.formteamtalent.entity.UAT;
import com.xdu.formteamtalent.entity.User;
import com.xdu.formteamtalent.global.RestfulResponse;
import com.xdu.formteamtalent.mapper.JoinRequestMapper;
import com.xdu.formteamtalent.service.*;
import com.xdu.formteamtalent.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequestMapping("/api/req")
@RestController
public class JoinRequestController {
    private final JoinRequestService joinRequestService;
    private final TeamService teamService;
    private final ActivityService activityService;
    private final UATService uatService;
    private final UserService userService;
    private final JoinRequestMapper joinRequestMapper;

    @Autowired
    public JoinRequestController(JoinRequestService joinRequestService,
                                 TeamService teamService,
                                 ActivityService activityService,
                                 UATService uatService,
                                 UserService userService,
                                 JoinRequestMapper joinRequestMapper) {
        this.joinRequestService = joinRequestService;
        this.teamService = teamService;
        this.activityService = activityService;
        this.uatService = uatService;
        this.userService = userService;
        this.joinRequestMapper = joinRequestMapper;
    }

    @PostMapping("/send")
    public RestfulResponse sendRequest(@RequestBody JoinRequest joinRequest, HttpServletRequest request) {
        String userId = AuthUtil.getUserId(request);
        // ???????????????????????????
        QueryWrapper<UAT> wrapper = new QueryWrapper<>();
        wrapper.eq("a_id", joinRequest.getAId());
        wrapper.eq("t_id", joinRequest.getTId());
        wrapper.eq("u_id", userId);
        if (uatService.getOne(wrapper) != null) {
            return RestfulResponse.fail(403, "?????????????????????");
        }
        // ?????????????????????????????????
        User user = userService.getOne(new QueryWrapper<User>().eq("u_id", userId));
        Team team = teamService.getOne(new QueryWrapper<Team>().eq("t_id", joinRequest.getTId()));
        // ????????????????????????
        if (team.getTCount().equals(team.getTTotal())) {
            return RestfulResponse.fail(403, "????????????=_=");
        }

        joinRequest.setAgree(0);
        joinRequest.setStatus(1);
        joinRequest.setSendDate(String.valueOf(System.currentTimeMillis()));
        joinRequest.setUName(user.getUName());
        joinRequest.setTName(team.getTName());
        joinRequest.setFromId(userId);
        joinRequest.setToId(team.getTLeaderId());

        JoinRequest checkRequest = joinRequestMapper.checkRequestExist(joinRequest.getFromId(), joinRequest.getToId(),
                joinRequest.getAId(), joinRequest.getTId());
        if (checkRequest != null && checkRequest.getStatus() == 1) {
            return RestfulResponse.fail(403, "?????????????????????");
        }
        joinRequestService.save(joinRequest);
        return RestfulResponse.success();
    }

    @PostMapping("/handle")
    @Transactional
    public RestfulResponse handleRequest(@RequestParam Long id, @RequestParam Integer agree, HttpServletRequest request) {
        JoinRequest one = joinRequestService.getOne(new QueryWrapper<JoinRequest>().eq("id", id));
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
                    return RestfulResponse.fail(404, "??????????????????");
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
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail(403, "????????????");
        }
    }

    @PostMapping("/remove/{id}")
    public RestfulResponse removeRequest(@PathVariable String id, HttpServletRequest request) {
        JoinRequest one = joinRequestService.getOne(new QueryWrapper<JoinRequest>().eq("id", id));
        if (one != null) {
            if (one.getFromId().equals(AuthUtil.getUserId(request))) {
                joinRequestService.removeById(id);
                return RestfulResponse.success();
            } else {
                return RestfulResponse.fail(403, "????????????");
            }
        } else {
            return RestfulResponse.fail(404, "??????????????????");
        }
    }

    private List<JoinRequest> removeOpenId(List<JoinRequest> list) {
        for (JoinRequest req : list) {
            req.setFromId("");
            req.setToId("");
        }
        return list;
    }

    /**
     * ???????????????????????????
     *
     * @param type    ????????????0????????????????????? 1????????????????????? 2???????????????????????????
     * @param status  ????????????0????????????????????? 1????????????????????? 2???????????????????????????
     * @param request ??????
     * @return json
     */
    @GetMapping("/get")
    public RestfulResponse getMyRequest(@RequestParam(defaultValue = "2") Integer type,
                                        @RequestParam(defaultValue = "2") Integer status,
                                        HttpServletRequest request) {
        if ((type != 0 && type != 1 && type != 2) || (status != 0 && status != 1 && status != 2)) {
            return RestfulResponse.fail(404, "????????????");
        }
        String uId = AuthUtil.getUserId(request);
        List<JoinRequest> requests = joinRequestMapper.getMyRequest(uId);
        if (status == 2 && type == 2) {
            return RestfulResponse.success(removeOpenId(requests));
        }

        // ???????????????type???
        Predicate<JoinRequest> pType = req -> type == 0 ? req.getFromId().equals(uId) : req.getToId().equals(uId);
        // ???????????????status???
        Predicate<JoinRequest> pStatus = req -> req.getStatus().equals(status);

        if (status == 2)
            return RestfulResponse.success(
                    removeOpenId(requests.stream().filter(pType).collect(Collectors.toList()))
            );
        if (type == 2)
            return RestfulResponse.success(
                    removeOpenId(requests.stream().filter(pStatus).collect(Collectors.toList()))
            );
        return RestfulResponse.success(
                removeOpenId(requests.stream().filter(pStatus).filter(pType).collect(Collectors.toList()))
        );
    }
}