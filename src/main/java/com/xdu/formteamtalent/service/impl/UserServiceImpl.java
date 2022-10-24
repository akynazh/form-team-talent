package com.xdu.formteamtalent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdu.formteamtalent.entity.User;
import com.xdu.formteamtalent.mapper.UserMapper;
import com.xdu.formteamtalent.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
