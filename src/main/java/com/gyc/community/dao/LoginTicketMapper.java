package com.gyc.community.dao;

import com.gyc.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
@Deprecated
public interface LoginTicketMapper {



    //插入一个凭证,每个字符串后面加个空格
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })//把多个字符串拼成一个sql
    @Options(useGeneratedKeys = true,keyProperty = "id")//自动生成主键
    int insertLoginTicket(LoginTicket loginTicket);

    //查询
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);
    //修改状态
    @Update({
            "update login_ticket set status=${status} where ticket=#{ticket}"
    })
    int updateStatus(String ticket,int status);

}
