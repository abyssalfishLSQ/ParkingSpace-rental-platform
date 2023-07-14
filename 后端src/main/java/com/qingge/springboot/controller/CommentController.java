package com.qingge.springboot.controller;
import com.qingge.springboot.entity.Comment;
import com.qingge.springboot.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 * @author 李思琪
 * @since 2023-06-14
 */
@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{parkingid}")
    public List<Comment> getAllComments(@PathVariable Integer parkingid) {
        return commentService.getAllComments(parkingid);
    }

    @PostMapping
    public void saveComment(@RequestBody Comment comment) {
        commentService.saveComment(comment);
    }
}