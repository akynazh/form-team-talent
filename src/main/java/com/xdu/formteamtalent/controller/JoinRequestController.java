package com.xdu.formteamtalent.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.JoinRequest;
import com.xdu.formteamtalent.entity.UAT;
import com.xdu.formteamtalent.global.RestfulResponse;
import com.xdu.formteamtalent.mapper.JoinRequestMapper;
import com.xdu.formteamtalent.service.JoinRequestService;
import com.xdu.formteamtalent.service.TeamService;
import com.xdu.formteamtalent.service.UATService;
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
    private JoinRequestService joinRequestService;
    private TeamService teamService;
    private UATService uatService;

    private JoinRequestMapper joinRequestMapper;

    @Autowired
    public void setJoinRequestMapper(JoinRequestMapper joinRequestMapper) {
        this.joinRequestMapper = joinRequestMapper;
    }

    @Autowired
    public void setUatService(UATService uatService) {
        this.uatService = uatService;
    }

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @Autowired
    public void setJoinRequestService(JoinRequestService joinRequestService) {
        this.joinRequestService = joinRequestService;
    }

    @PostMapping("/send")
    public RestfulResponse sendRequest(@RequestBody JoinRequest joinRequest, HttpServletRequest request) {
        QueryWrapper<UAT> wrapper = new QueryWrapper<>();
        wrapper.eq("a_id", joinRequest.getA_id());
        wrapper.eq("t_id", joinRequest.getT_id());
        UAT uat = uatService.getOne(wrapper);

        if (uat == null) {
            return RestfulResponse.fail(404, "数据库错误，请联系后台");
        }

        joinRequest.setAgree(0);
        joinRequest.setStatus(1);
        joinRequest.setFrom_id(AuthUtil.getUserId(request));
        joinRequest.setTo_id(uat.getU_id());

        joinRequestService.save(joinRequest);
        return RestfulResponse.success();
    }

    @PostMapping("/handle")
    @Transactional
    public RestfulResponse handleRequest(@RequestParam Long id, @RequestParam Integer agree, HttpServletRequest request) {
        JoinRequest one = joinRequestService.getOne(new QueryWrapper<JoinRequest>().eq("id", id));
        if (one.getTo_id().equals(AuthUtil.getUserId(request))) {
            one.setStatus(0);
            one.setAgree(agree);
            joinRequestService.updateById(one);
            if (agree == 1) {
                QueryWrapper<UAT> wrapper = new QueryWrapper<>();
                wrapper.eq("u_id", one.getFrom_id());
                wrapper.eq("t_id", one.getT_id());
                wrapper.eq("a_id", one.getA_id());
                if (uatService.getOne(wrapper) != null) {
                    return RestfulResponse.fail(404, "不可重复加入");
                }
                UAT uat = new UAT();
                uat.setU_id(one.getFrom_id());
                uat.setA_id(one.getA_id());
                uat.setT_id(one.getT_id());
                uatService.save(uat);
            }
            return RestfulResponse.success();
        } else {
            return RestfulResponse.fail(403, "没有权限");
        }
    }

    @PostMapping("/remove/{id}")
    public RestfulResponse removeRequest(@PathVariable String id, HttpServletRequest request) {
        JoinRequest one = joinRequestService.getOne(new QueryWrapper<JoinRequest>().eq("id", id));
        if (one != null) {
            if (one.getFrom_id().equals(AuthUtil.getUserId(request))) {
                joinRequestService.removeById(id);
                return RestfulResponse.success();
            } else {
                return RestfulResponse.fail(403, "没有权限");
            }
        } else {
            return RestfulResponse.fail(404, "不存在该记录");
        }
    }

    @PostMapping("/update")
    public RestfulResponse updateRequest(@RequestBody JoinRequest joinRequest, HttpServletRequest request) {
        JoinRequest one = joinRequestService.getOne(new QueryWrapper<JoinRequest>().eq("id", joinRequest.getId()));
        if (one != null) {
            if (one.getFrom_id().equals(AuthUtil.getUserId(request))) {
                joinRequestService.updateById(joinRequest);
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
     * @param type 非必要，0：我发出的请求 1：发给我的请求 2：所有请求（默认）
     * @param status 非必要，0：已处理的请求 1：未处理的请求 2：所有请求（默认）
     * @param request 请求
     * @return json
     */
    @GetMapping("/get")
    public RestfulResponse getMyRequest(@RequestParam(defaultValue = "2") Integer type, @RequestParam(defaultValue = "2") Integer status, HttpServletRequest request) {
        if ((type != 0 && type != 1 && type != 2) || (status != 0 && status != 1 && status != 2)) {
            return RestfulResponse.fail(404, "参数错误");
        }
        String u_id = AuthUtil.getUserId(request);
        List<JoinRequest> requests = joinRequestMapper.getMyRequest(u_id);
        if (status == 2 && type == 2) {
            return RestfulResponse.success(requests);
        }

        // 过滤出符合type的
        Predicate<JoinRequest> p_type = req -> type == 0 ? req.getFrom_id().equals(u_id) : req.getTo_id().equals(u_id);
        // 过滤出符合status的
        Predicate<JoinRequest> p_status = req -> req.getStatus().equals(status);

        if (status == 2) return RestfulResponse.success(requests.stream().filter(p_type).collect(Collectors.toList()));
        if (type == 2) return RestfulResponse.success(requests.stream().filter(p_status).collect(Collectors.toList()));
        return RestfulResponse.success(requests.stream().filter(p_status).filter(p_type).collect(Collectors.toList()));
    }
}