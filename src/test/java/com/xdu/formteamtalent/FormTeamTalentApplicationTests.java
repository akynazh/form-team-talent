package com.xdu.formteamtalent;

import cn.hutool.core.util.IdUtil;
import com.xdu.formteamtalent.entity.User;
import com.xdu.formteamtalent.service.UserService;
import com.xdu.formteamtalent.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FormTeamTalentApplicationTests {
    /*
	@Autowired
    UserService userService;
    @Test
    public void getTokenForTest() {
        String uuid = IdUtil.simpleUUID();
        String token = JwtUtil.createToken(uuid);
        User user = new User();
        user.setU_id(uuid);
        user.setU_name("mike");
        user.setU_school("xdu");
        user.setU_stu_num("2000913sdaaf");
        userService.save(user);
        System.out.println(token);
    }
	*/
}
