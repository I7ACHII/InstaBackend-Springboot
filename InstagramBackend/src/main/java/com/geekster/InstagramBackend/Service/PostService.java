package com.geekster.InstagramBackend.Service;

import com.geekster.InstagramBackend.Model.Comment;
import com.geekster.InstagramBackend.Model.Post;
import com.geekster.InstagramBackend.Model.User;
import com.geekster.InstagramBackend.Repository.IPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    @Autowired
    IPostRepo iPostRepo;

    public String createInstaPost(Post post) {
        post.setPostCreatedTimeStamp(LocalDateTime.now());
        iPostRepo.save(post);
        return "Post has been added!!!";
    }

    public String removeInstaPost(Integer postId, String email) {
        Post post = iPostRepo.findById(postId).orElse(null);
        if(post == null){
            return "Post to be deleted does not exists!!";
        }
        else if (post.getPostOwner().getUserEmail().equals(email)){
            iPostRepo.deleteById(postId);
            return "Post has been deleted!!!";
        }
        else{
            return "You are not authorize to delete this post!! ";
        }
    }


    public boolean validatePost(Post post) {
        boolean validPost = iPostRepo.existsById(post.getPostId());
        return validPost;
    }

    public Post findPostByPostId(Integer postId) {
        return iPostRepo.findById(postId).orElse(null);
    }
}
