package com.gyc.community.dao;

import com.gyc.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    //查询什么评论，是对评论的评论，还是对帖子的评论
    List<Comment> selectCommentByEntity(int entityType,int entityId,int offset,int limit);

    //查询评论的数量
    int selectCountByEntity(int entityType,int entityId);

    //增加评论
    int insertComment(Comment comment);
}
