package com.gyc.community.controller.interceptor;

import com.gyc.community.annotation.LoginRequired;
import com.gyc.community.entity.User;
import com.gyc.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //先判断拦截的是不是方法
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired annotation = method.getAnnotation(LoginRequired.class);
            if(annotation!=null){
                //判断有没有登录
                User user = hostHolder.getUser();
                //用户未登录，重定向到登录页面
                if(user == null){
                    response.sendRedirect(request.getContextPath()+"/login");
                    return false;
                }
            }
        }


        return true;
    }
}
