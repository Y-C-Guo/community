package com.gyc.community.controller;

import com.gyc.community.annotation.LoginRequired;
import com.gyc.community.entity.User;
import com.gyc.community.service.FollowService;
import com.gyc.community.service.LikeService;
import com.gyc.community.service.UserService;
import com.gyc.community.util.CommunityConstant;
import com.gyc.community.util.CommunityUtil;
import com.gyc.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping(path = "/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;





    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }


    //上传文件的请求
    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImg, Model model){
        if(headerImg == null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }

        //上传文件
        //存储文件的后缀
        String fileName = headerImg.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }

        //生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        //确定文件存放的路径
        File dest = new File(uploadPath+"/"+fileName);
        try {
            //存储文件
            headerImg.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败："+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！",e);
        }

        //更新当前用户的头像的路径（web访问路径）
        User user = hostHolder.getUser();
        String headerUrl = domain+contextPath+"/user/header/"+fileName;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";




    }

    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName")String fileName, HttpServletResponse response){
        //服务器存放的路径
        fileName = uploadPath+"/"+fileName;
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("image/"+suffix);

        try(
                OutputStream os = response.getOutputStream();
                FileInputStream fis = new FileInputStream(fileName);
                ) {


            byte[] buffer = new byte[1024];
            int b = 0;
            while((b = fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败："+e.getMessage());
        }

    }

    //修改密码
    @RequestMapping(path = "/updatePassword",method = RequestMethod.POST)
    public String updatePassword(String oldPassword,String newPassword,String confirmPassword,Model model,@CookieValue("ticket") String ticket){
//        if(oldPassword == null || newPassword == null || confirmPassword == null){
//            model.addAttribute("error","请输入正确密码");
//            return "/site/setting";
//        }

        //新密码与确认密码不相同，修改密码失败
        if(!newPassword.equals(confirmPassword)){
            model.addAttribute("confirmPasswordMsg","两次输入的密码不一致");
            return "/site/setting";
        }

        //原始密码输入不正确，修改密码失败
        User user = hostHolder.getUser();
        if(!CommunityUtil.md5(oldPassword+user.getSalt()).equals(user.getPassword())){
            model.addAttribute("oldPasswordMsg","原始密码不正确");
            return "/site/setting";
        }
        //新密码与原始密码相同，修改密码失败
        if(CommunityUtil.md5(newPassword+user.getSalt()).equals(user.getPassword())){
            model.addAttribute("newPasswordMsg","新密码与原始密码不能相同");
            return "/site/setting";
        }
        //新密码加密
        newPassword = CommunityUtil.md5(newPassword+user.getSalt());
        //修改密码
        userService.updatePassword(user.getId(),newPassword);
        //修改完密码以后应该重新登录
        userService.logout(ticket);
        return "redirect:/login";

    }

    //个人主页
    @RequestMapping(path = "/profile/{userId}",method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId,Model model){
        User user = userService.findUserById(userId);
        if(user == null) throw new RuntimeException("该用户不存在");

        //用户的基本信息
        model.addAttribute("user",user);

        //点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);

        //关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);
        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount",followerCount);
        //是否已关注
        //先判断用户是否已经登录
        boolean hasFollowed = false;
        if(hostHolder.getUser()!=null){
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_USER,userId);

        }
        model.addAttribute("hasFollowed",hasFollowed);

        return "/site/profile";
    }


}
