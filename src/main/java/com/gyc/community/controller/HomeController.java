package com.gyc.community.controller;

import com.gyc.community.entity.DiscussPost;
import com.gyc.community.entity.Page;
import com.gyc.community.entity.User;
import com.gyc.community.service.DiscussPostService;
import com.gyc.community.service.UserService;
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
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

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
                discussPosts.add(map);

            }
        }

        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
}
