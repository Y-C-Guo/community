package com.gyc.community.util;

import com.gyc.community.entity.User;
import org.springframework.stereotype.Component;
//持有用户的信息，用于代替session对象
@Component
public class HostHolder {
    //ThreadLocal通过当前线程获取一个对应的Map，在进行get set
    private ThreadLocal<User> users  = new ThreadLocal<>();
    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
