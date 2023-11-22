package com.geekster.InstagramBackend.Service;

import com.geekster.InstagramBackend.Model.Like;
import com.geekster.InstagramBackend.Model.Post;
import com.geekster.InstagramBackend.Model.User;
import com.geekster.InstagramBackend.Repository.ILikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

    @Autowired
    ILikeRepo iLikeRepo;

    public String addLike(Like like) {
        iLikeRepo.save(like);
        return "Post is liked successfully!!";
    }

    public boolean postCanBeLikedOrNot(User liker, Post post) {
        List<Like> listOfSameRecord = iLikeRepo.findByInstaPostAndLiker(post, liker);
        return listOfSameRecord != null && listOfSameRecord.isEmpty();
    }

    public String getTotalLikesOnAPost(Post post) {
        return String.valueOf(iLikeRepo.findByInstaPost(post).size());
    }

    public String removeLike(Integer likeId, String likeRemoversEmail) {
        Like like = iLikeRepo.findById(likeId).orElse(null);
        if(like != null){
            String likersEmail = like.getLiker().getUserEmail();
            if(likersEmail.equals(likeRemoversEmail)){
                iLikeRepo.deleteById(likeId);
                return "Post is unliked";
            }
            else {
                return "like can't be removed!!";
            }
        }
        else{
            return "Like by this likeId does not exist!!";
        }
    }
}
