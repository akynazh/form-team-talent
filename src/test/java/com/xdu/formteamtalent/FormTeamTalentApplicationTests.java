package com.xdu.formteamtalent;

import com.xdu.formteamtalent.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FormTeamTalentApplicationTests {
    @Test
    public void getTokenForTest() {
        System.out.println(JwtUtil.createToken("test1"));
    }
}
