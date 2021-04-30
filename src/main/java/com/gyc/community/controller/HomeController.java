package com.gyc.community.controller;

import com.gyc.community.entity.DiscussPost;
import com.gyc.community.entity.Message;
import com.gyc.community.entity.Page;
import com.gyc.community.entity.User;
import com.gyc.community.service.DiscussPostService;
import com.gyc.community.service.LikeService;
import com.gyc.community.service.MessageService;
import com.gyc.community.service.UserService;
import com.gyc.community.util.CommunityConstant;
import com.gyc.community.util.HostHolder;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        //方法调用之前，springmvc会自动实例化Model和page，并将page注入Model
        //所以，我们在thymeleaf中可以直接访问page对象中的数据


        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");


        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> discussPosts = new ArrayList<>();

        if(list!=null){
            for (DiscussPost post : list) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount",likeCount);
                discussPosts.add(map);

            }
        }

        //未读的私信数量
        User user = hostHolder.getUser();
        if(user!=null){
            int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(),null);
            model.addAttribute("letterUnreadCount",letterUnreadCount);
        }


        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }


    @RequestMapping(path = "/error",method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }
}
