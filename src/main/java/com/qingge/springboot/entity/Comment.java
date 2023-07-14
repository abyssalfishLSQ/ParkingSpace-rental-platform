package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@TableName("comment")
public class Comment {
    private static final long serialVersionUID = 1L;
    //为了避免在类的结构发生变化时导致反序列化失败，我们可以显式地声明一个serialVersionUID，并将其设为固定值。
    @TableId(value = "cid",type = IdType.AUTO)
    private Integer cid;
    private Integer parkingid;//评论的是哪个车位
    private String content;
    private LocalDateTime createdAt;//评论创造的时间
    private int parentId;//存储父级评论
    private int topid;//top最原始的id
    private String nickname;//评论人的昵称
    private String commentAvatar;//评论人的头像URL
    private String targetNickname;//@了谁，他的昵称是啥
    @TableField(exist = false)
    private List<Comment> childComments;

}
