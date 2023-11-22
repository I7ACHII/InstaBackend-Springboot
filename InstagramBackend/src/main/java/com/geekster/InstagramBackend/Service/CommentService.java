package com.geekster.InstagramBackend.Service;

import com.geekster.InstagramBackend.Model.Comment;
import com.geekster.InstagramBackend.Repository.ICommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    ICommentRepo iCommentRepo;

    public String addPostComment(Comment comment) {
        comment.setCommentCreationTimeStamp(LocalDateTime.now());
        iCommentRepo.save(comment);
        return "comment has been added!!";
    }

    public String removeComment(Integer commentId) {
        iCommentRepo.deleteById(commentId);
        return "comment has been deleted!!";
    }

    public Comment getCommentById(Integer commentId) {
        return iCommentRepo.findById(commentId).orElse(null);
    }
}
