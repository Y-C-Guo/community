package com.gyc.community;

import com.gyc.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TransactionTests {
    @Autowired
    private AlphaService alphaService;
    @Test
    public void alphaTest(){
        System.out.println(alphaService.save1());
    }

    @Test
    public void betaTest(){
        System.out.println(alphaService.save2());
    }
}
