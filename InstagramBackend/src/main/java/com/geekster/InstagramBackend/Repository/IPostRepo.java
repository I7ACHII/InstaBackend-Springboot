package com.geekster.InstagramBackend.Repository;

import com.geekster.InstagramBackend.Model.Comment;
import com.geekster.InstagramBackend.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IPostRepo extends JpaRepository<Post, Integer> {

//    Post findFirstByPostId(Integer postId);

}
