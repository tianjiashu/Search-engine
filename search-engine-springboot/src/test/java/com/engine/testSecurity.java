package com.engine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
@SpringBootTest
public class testSecurity {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testPasswordEncoder(){
        String password = "1234";
        String encode = passwordEncoder.encode(password);
        System.out.println(encode);
    }
}
