package com.gyc.community.config;

import com.gyc.community.util.CommunityConstant;
import com.gyc.community.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {
    //忽略对静态资源的拦截
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }


//    //认证
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//    }
    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //授权
        http.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "discuss/add",
                        "/comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"
                ).hasAnyAuthority(
                    AUTHORITY_USER,
                    AUTHORITY_ADMIN,
                    AUTHORITY_MODERATOR
                )
                .anyRequest().permitAll()//其他请求都允许
        .and().csrf().disable();//不弃用csrf

        //权限不够时的处理
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    //没有登录时的处理
                    @Override
                    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        String xRequestedWith = httpServletRequest.getHeader("x-requested_with");
                        if("XMLHttpRequest".equals(xRequestedWith)){
                            //异步请求
                            httpServletResponse.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = httpServletResponse.getWriter();
                            writer.write(CommunityUtil.getJSONString(403,"你还没有登录！"));
                        }else{
                            //同步请求
                            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    //权限不足的处理
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestedWith = httpServletRequest.getHeader("x-requested_with");
                        if("XMLHttpRequest".equals(xRequestedWith)){
                            //异步请求
                            httpServletResponse.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = httpServletResponse.getWriter();
                            writer.write(CommunityUtil.getJSONString(403,"你还没有访问此功能的权限！"));
                        }else{
                            //同步请求
                            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/denied");
                        }
                    }
                });

        //Security底层默认会拦截logout请求，进行退出处理
        //覆盖它默认的逻辑，才能执行我们自己的退出代码
        http.logout().logoutUrl("/securitylogout");
    }
}
