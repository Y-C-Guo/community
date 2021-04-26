package com.gyc.community.dao;

import com.gyc.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    //offset 每一页起始行号，limit每页显示多少数据
    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);

    //@Param 取别名
    //如果只有一个参数，并且在if里使用，则必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);

    //增加帖子的方法
    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);
    //修改帖子评论数量
    int updateCommentCount(int id,int commentCount);







}

