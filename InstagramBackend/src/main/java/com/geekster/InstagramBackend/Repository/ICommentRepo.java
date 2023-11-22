package com.geekster.InstagramBackend.Repository;

import com.geekster.InstagramBackend.Model.Admin;
import com.geekster.InstagramBackend.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ICommentRepo extends JpaRepository<Comment, Integer> {

}
