package com.xdu.formteamtalent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdu.formteamtalent.entity.Team;
import com.xdu.formteamtalent.mapper.TeamMapper;
import com.xdu.formteamtalent.service.TeamService;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {
}
