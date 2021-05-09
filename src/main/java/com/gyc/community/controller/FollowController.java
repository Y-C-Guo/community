package com.gyc.community.controller;

import com.gyc.community.entity.Page;
import com.gyc.community.entity.User;
import com.gyc.community.service.FollowService;
import com.gyc.community.service.UserService;
import com.gyc.community.util.CommunityConstant;
import com.gyc.community.util.CommunityUtil;
import com.gyc.community.util.HostHolder;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements CommunityConstant {


    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

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

    //关注列表请求
    @RequestMapping(path = "/followees/{userId}",method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if(user == null) {
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user",user);
        page.setLimit(5);
        page.setPath("/followees/"+userId);
        page.setRows((int)followService.findFolloweeCount(userId,CommunityConstant.ENTITY_TYPE_USER));

        List<Map<String,Object>> userList = followService.findFollowees(userId,page.getOffset(),page.getLimit());

        if(userList!=null){
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed",hasFolllowed(u.getId()));

            }
        }

        model.addAttribute("users",userList);
        return "/site/followee";
    }

    //关注粉丝请求
    @RequestMapping(path = "/followers/{userId}",method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if(user == null) {
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user",user);
        page.setLimit(5);
        page.setPath("/followers/"+userId);
        page.setRows((int)followService.findFollowerCount(CommunityConstant.ENTITY_TYPE_USER,userId));

        List<Map<String,Object>> userList = followService.findFollowers(userId,page.getOffset(),page.getLimit());

        if(userList!=null){
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed",hasFolllowed(u.getId()));

            }
        }

        model.addAttribute("users",userList);
        return "/site/follower";
    }

    private boolean hasFolllowed(int userId){
        if(hostHolder.getUser() == null) return false;
        return followService.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_USER,userId);
    }
}
