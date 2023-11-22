package com.geekster.InstagramBackend.Repository;

import com.geekster.InstagramBackend.Model.Comment;
import com.geekster.InstagramBackend.Model.Follow;
import com.geekster.InstagramBackend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IFollowRepo extends JpaRepository<Follow, Integer> {

    List<Follow> findByUserAndUsersFollower(User user, User usersFollower);
}
