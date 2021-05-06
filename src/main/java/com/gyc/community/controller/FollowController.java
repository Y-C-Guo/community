package com.gyc.community.controller;

import com.gyc.community.entity.User;
import com.gyc.community.service.FollowService;
import com.gyc.community.util.CommunityUtil;
import com.gyc.community.util.HostHolder;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {


    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;

    //异步请求
    @RequestMapping(value = "/follow",method = RequestMethod.POST)
    @ResponseBody

    public String follow(int entityType,int entityId){
        //如果未登录，需要使用拦截器拦截处理
        User user = hostHolder.getUser();

        followService.follow(user.getId(),entityType,entityId);

        return CommunityUtil.getJSONString(0,"已关注");
    }

    //异步请求
    @RequestMapping(value = "/unfollow",method = RequestMethod.POST)
    @ResponseBody

    public String unfollow(int entityType,int entityId){
        //如果未登录，需要使用拦截器拦截处理
        User user = hostHolder.getUser();

        followService.unFollow(user.getId(),entityType,entityId);

        return CommunityUtil.getJSONString(0,"已取消关注");
    }
}
