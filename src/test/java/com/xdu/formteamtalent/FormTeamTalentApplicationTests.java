package com.xdu.formteamtalent;

import com.xdu.formteamtalent.utils.JwtUtil;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class FormTeamTalentApplicationTests {
    @Test
    public void getTokenForTest() {
        System.out.println(JwtUtil.createToken("test1"));
    }

    @Test
    public void contextLoads() throws IOException {
        CreateIndexRequest req = new CreateIndexRequest("test");

    }
}
