package org.zerock.j2.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Objects;

@SpringBootTest
@Log4j2
public class JWTTests {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void testCreate(){

        Map<String, Object> claim = Map.of("email", "user00@aaa.com");

        String jwtStr = jwtUtil.generate(claim, 10);

        System.out.println(jwtStr);
    }

    @Test
    public void testToken(){

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InVzZXIwMEBhYWEuY29tIiwiaWF0IjoxNjg5NzQ0Mzk2LCJleHAiOjE2ODk3NDQ5OTZ9.U6NOBVFa_3bxbmWmI7bXHk8IO4dnurH-uNsokoHa123456nI";

        try {

            jwtUtil.validateToken(token);
        } catch (Exception e){
            System.out.println(e);
        }
    }

}
