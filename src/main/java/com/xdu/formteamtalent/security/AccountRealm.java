package com.xdu.formteamtalent.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdu.formteamtalent.entity.User;
import com.xdu.formteamtalent.service.UserService;
import com.xdu.formteamtalent.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {
    private final UserService userService;

    @Autowired
    public AccountRealm(UserService userService) {
        this.userService = userService;
    }

    /**
     * 必须添加，设置支持于自己指定的JwtToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 认证校验
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        Claims claims = JwtUtil.getClaimsByToken((String) jwtToken.getPrincipal());

        assert claims != null;
        String u_open_id = claims.getSubject();
        User user = userService.getOne(new QueryWrapper<User>().eq("u_open_id", u_open_id));
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        // 所有对象统一为AccountProfile内置属性rid作为统一redis缓存id
        AccountProfile profile = new AccountProfile();
        profile.setU_open_id(user.getU_open_id());
        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), this.getClass().getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}
