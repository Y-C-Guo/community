package com.gyc.community;

import com.gyc.community.dao.DiscussPostMapper;
import com.gyc.community.dao.LoginTicketMapper;
import com.gyc.community.dao.UserMapper;
import com.gyc.community.entity.DiscussPost;
import com.gyc.community.entity.LoginTicket;
import com.gyc.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("gyc");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("12345@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());
        int i = userMapper.insertUser(user);
        System.out.println(i);
        System.out.println(user.getId());

    }

    @Test
    public void updateUser(){
        int i = userMapper.updateStatus(150, 1);
        System.out.println(i);

        int i1 = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(i1);

        int i2 = userMapper.updatePassword(150, "0000");
        System.out.println(i2);

    }


    @Test
    public void testSelectPosts(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }

        int i = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(i);


    }

    @Test
    public void testInsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
        System.out.println(loginTicketMapper.insertLoginTicket(loginTicket));

    }

    @Test
    public void testQueryLoginTicket(){

        System.out.println(loginTicketMapper.selectByTicket("abc"));

    }

    @Test
    public void testUpdateLoginTicket(){
        System.out.println(loginTicketMapper.selectByTicket("abc"));
        loginTicketMapper.updateStatus("abc",1);
        System.out.println(loginTicketMapper.selectByTicket("abc"));

    }



}
