package com.xdu.formteamtalent.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.*;
import com.xdu.formteamtalent.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis 相关操作
 *
 * @author akyna
 * @date 04/09 009 9:38 PM
 */
@Component
@Slf4j
public class RedisHelper {
    public static final String ACT_PRE = "act:";
    public static final String USER_PRE = "user:";
    public static final String TEAM_PRE = "team:";
    public static final String REQ_PRE = "req:";

    public static final String UAT_PRE = "uat:";

    private final JoinRequestService joinRequestService;
    private final TeamService teamService;
    private final ActivityService activityService;
    private final UATService uatService;
    private final UserService userService;

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisHelper(JoinRequestService joinRequestService,
                       TeamService teamService,
                       ActivityService activityService,
                       UATService uatService,
                       UserService userService,
                       StringRedisTemplate redisTemplate) {
        this.joinRequestService = joinRequestService;
        this.teamService = teamService;
        this.activityService = activityService;
        this.uatService = uatService;
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    public Activity getActivityByAId(String aId) {
        String activityStr = redisTemplate.opsForValue().get(ACT_PRE + aId);
        Activity activity;
        if (activityStr != null) {
            activity = (Activity) JSON.parseObject(activityStr, Activity.class);
        } else {
            activity = activityService.getOne(new QueryWrapper<Activity>().eq("a_id", aId));
            if (activity != null) {
                redisTemplate.opsForValue().set(ACT_PRE + aId, JSON.toJSONString(activity));
            }
        }
        return activity;
    }

    public void removeActivityCacheByAId(String aId) {
        redisTemplate.delete(ACT_PRE + aId);
    }

    public Team getTeamByTId(String tId) {
        String teamStr = redisTemplate.opsForValue().get(TEAM_PRE + tId);
        Team team;
        if (teamStr != null) {
            team = (Team) JSON.parseObject(teamStr, Team.class);
        } else {
            team = teamService.getOne(new QueryWrapper<Team>().eq("t_id", tId));
            if (team != null) {
                redisTemplate.opsForValue().set(TEAM_PRE + tId, JSON.toJSONString(team));
            }
        }
        return team;
    }

    public void removeTeamCacheByTId(String tId) {
        redisTemplate.delete(TEAM_PRE + tId);
    }

    public User getUserByUId(String uId) {
        String userStr = redisTemplate.opsForValue().get(USER_PRE + uId);
        User user;
        if (userStr != null) {
            user = (User) JSON.parseObject(userStr, User.class);
            log.info("从缓存中得到序列化的用户信息: " + user);
        } else {
            user = userService.getOne(new QueryWrapper<User>().eq("u_id", uId));
            if (user != null) {
                redisTemplate.opsForValue().set(USER_PRE + uId, JSON.toJSONString(user));
                log.info("成功缓存用户信息: " + user);
            }
        }
        return user;
    }

    public void removeUserCacheByUId(String uId) {
        redisTemplate.delete(USER_PRE + uId);
    }

    public JoinRequest getJoinReqByReqId(String reqId) {
        String reqStr = redisTemplate.opsForValue().get(REQ_PRE + reqId);
        JoinRequest joinRequest;
        if (reqStr != null) {
            joinRequest = (JoinRequest) JSON.parseObject(reqStr, JoinRequest.class);
        } else {
            joinRequest = joinRequestService.getOne(new QueryWrapper<JoinRequest>().eq("id", reqId));
            if (joinRequest != null) {
                redisTemplate.opsForValue().set(REQ_PRE + reqId, JSON.toJSONString(joinRequest));
            }
        }
        return joinRequest;
    }

    public void removeReqCacheByReqId(String reqId) {
        redisTemplate.delete(REQ_PRE + reqId);
    }

    public UAT getUATByIds(String aId, String tId, String uId) {
        String uatId = UAT_PRE + aId + tId + uId;
        String uatStr = redisTemplate.opsForValue().get(uatId);
        UAT uat;
        if (uatStr != null) {
            uat = (UAT) JSON.parseObject(uatStr, UAT.class);
        } else {
            QueryWrapper<UAT> wrapper = new QueryWrapper<>();
            wrapper.eq("a_id", aId);
            wrapper.eq("t_id", tId);
            wrapper.eq("u_id", uId);
            uat = uatService.getOne(wrapper);
            if (uat != null) {
                redisTemplate.opsForValue().set(uatId, JSON.toJSONString(uat));
            }
        }
        return uat;
    }

    public void removeUATCacheByIds(String aId, String tId, String uId) {
        redisTemplate.delete(UAT_PRE + aId + tId + uId);
    }
}
