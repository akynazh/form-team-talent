package com.xdu.formteamtalent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdu.formteamtalent.entity.UserTeam;
import com.xdu.formteamtalent.mapper.UserTeamMapper;
import com.xdu.formteamtalent.service.UserTeamService;
import org.springframework.stereotype.Service;

@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam> implements UserTeamService {
}
