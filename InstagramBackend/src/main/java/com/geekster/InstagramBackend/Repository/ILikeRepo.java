package com.geekster.InstagramBackend.Repository;

import com.geekster.InstagramBackend.Model.Comment;
import com.geekster.InstagramBackend.Model.Like;
import com.geekster.InstagramBackend.Model.Post;
import com.geekster.InstagramBackend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface ILikeRepo extends JpaRepository<Like, Integer> {

    List<Like> findByInstaPostAndLiker(Post post, User liker);


    List<Like> findByInstaPost(Post post);
}
