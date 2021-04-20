package com.gyc.community.dao;

import org.springframework.stereotype.Repository;

@Repository("alphaHibernate")//可以自定义bean的名字
public class AlphaDaoImpl implements AlphaDao{

    @Override
    public String select() {
        return "DAO";
    }
}
