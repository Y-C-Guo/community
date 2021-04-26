package com.gyc.community.service;

import com.gyc.community.dao.AlphaDao;
import com.gyc.community.dao.DiscussPostMapper;
import com.gyc.community.dao.UserMapper;
import com.gyc.community.entity.DiscussPost;
import com.gyc.community.entity.User;
import com.gyc.community.util.CommunityUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@Service
@Scope("singleton")//默认时单例模式，prototype每get一个就创建一个实例
public class AlphaService {

    //service 层调用dao
    @Autowired
    private AlphaDao alphaDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public AlphaService() {
        System.out.println("实例化AlphaService");
    }
    @PostConstruct
    public void init(){
        System.out.println("初始化AlphaService");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("销毁AlphaService");
    }


    public String find(){
        return alphaDao.select();
    }

    //REQUIRED:支持当前事务（外部事务），如果不存在则创建新事务
    //REQUIRES_NEW：创建一个新的事务，暂停当前事务（外部事务）
    //NESTED：如果当前存在外部事物，则嵌套在该事务中执行（独立的提交和回滚）
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)

    public Object save1(){
        //新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
        user.setEmail("alpha@.qq.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("hello");
        post.setContent("新人报到");
        post.setCreateTime(new Date());

        Integer.valueOf("abc");
        return "ok";
    }

    public Object save2(){
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionTemplate.execute(new TransactionCallback<Object>() {

            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
                user.setEmail("beta@.qq.com");
                user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                //新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好");
                post.setContent("新人报到");
                post.setCreateTime(new Date());

                Integer.valueOf("abc");
                return "ok";
            }
        });
    }
}
