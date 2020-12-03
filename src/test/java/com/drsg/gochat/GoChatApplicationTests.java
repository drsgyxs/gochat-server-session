package com.drsg.gochat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class GoChatApplicationTests {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    GoChatApplicationTests(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("123456"));

    }

}
