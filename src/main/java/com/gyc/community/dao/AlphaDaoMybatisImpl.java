package com.gyc.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository//操作数据库的注解
@Primary //相同bean的时候优先被装配
public class AlphaDaoMybatisImpl implements AlphaDao{

    @Override
    public String select() {
        return "mybatis";
    }
}
