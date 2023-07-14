package com.qingge.springboot.service;

import com.qingge.springboot.entity.Comment;
import com.qingge.springboot.mapper.CommentMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<Comment> getAllComments(int parking_id) {
        List<Comment> comments = commentMapper.selectList(null);
        Map<Integer, Comment> commentMap = new HashMap<>();

        // 构建评论树
        for (Comment comment : comments)
        {
            comment.setChildComments(new ArrayList<>());
            commentMap.put(comment.getCid(), comment);

            int parentId = comment.getParentId();
            if (parentId != 0) {
                Comment parentComment = commentMap.get(parentId);
                if (parentComment != null) {
                    parentComment.getChildComments().add(comment);
                }
            }
        }

        List<Comment> rootComments = new ArrayList<>();
        //选出一级评论&&评论车位是当前车位
        for (Comment comment : comments) {
            if (comment.getParentId() == 0 && comment.getParkingid()==parking_id) {
                rootComments.add(comment);
            }
        }

        return rootComments;
    }

    public void saveComment(Comment comment) {
        commentMapper.insert(comment);
    }
}
