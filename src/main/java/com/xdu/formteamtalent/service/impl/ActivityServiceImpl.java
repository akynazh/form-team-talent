package com.xdu.formteamtalent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdu.formteamtalent.entity.Activity;
import com.xdu.formteamtalent.mapper.ActivityMapper;
import com.xdu.formteamtalent.service.ActivityService;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
}
