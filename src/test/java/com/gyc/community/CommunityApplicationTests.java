package com.gyc.community;

import com.gyc.community.dao.AlphaDao;
import com.gyc.community.service.AlphaService;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {


    void contextLoads() {
    }

    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @Test
    public void testApplicationContext(){
        System.out.println(applicationContext);
        AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
        System.out.println(alphaDao.select());

        //通过名字获取bean
        AlphaDao alphaHibernate = applicationContext.getBean("alphaHibernate", AlphaDao.class);
        System.out.println(alphaHibernate.select());

    }

    //容器中的bean时单例的
    @Test
    public void testBeanManagement(){
        AlphaService bean = applicationContext.getBean(AlphaService.class);
        System.out.println(bean);
    }


    @Test
    public void testBeanConfig(){
        SimpleDateFormat bean = applicationContext.getBean(SimpleDateFormat.class);
        System.out.println(bean.format(new Date()));
    }

    @Autowired
    @Qualifier("alphaHibernate")//会按bean的名字注入
    private AlphaDao alphaDao;

    @Autowired
    private AlphaService alphaService;
    @Autowired
    private SimpleDateFormat simpleDateFormat;
    @Test
    public void testDI(){
        System.out.println(alphaDao);
        System.out.println(alphaService);
        System.out.println(simpleDateFormat);
    }
}
