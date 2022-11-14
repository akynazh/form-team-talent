package com.xdu.formteamtalent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdu.formteamtalent.entity.JoinRequest;
import com.xdu.formteamtalent.mapper.JoinRequestMapper;
import com.xdu.formteamtalent.service.JoinRequestService;
import org.springframework.stereotype.Service;

@Service
public class JoinRequestServiceImpl extends ServiceImpl<JoinRequestMapper, JoinRequest> implements JoinRequestService {
}
