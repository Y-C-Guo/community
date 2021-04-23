package com.gyc.community;

import com.gyc.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.Target;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void sensitiveTest(){
        String s = ".开。票？会赌&博吗";
        String filter = sensitiveFilter.filter(s);
        System.out.println(filter);
    }
}
