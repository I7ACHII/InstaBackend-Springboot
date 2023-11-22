package com.geekster.InstagramBackend.Repository;

import com.geekster.InstagramBackend.Model.Admin;
import com.geekster.InstagramBackend.Model.AuthenticationToken;
import com.geekster.InstagramBackend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IAuthenticationRepo extends JpaRepository<AuthenticationToken, Long> {

    AuthenticationToken findFirstByTokenValue(String token);

    AuthenticationToken findFirstByUser(User user);

    AuthenticationToken findFirstByAdmin(Admin admin);
}
