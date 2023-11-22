package com.geekster.InstagramBackend.Controller;


import com.geekster.InstagramBackend.Model.Admin;
import com.geekster.InstagramBackend.Model.User;
import com.geekster.InstagramBackend.Model.dto.SignInAdmin;
import com.geekster.InstagramBackend.Model.dto.SignUpOutput;
import com.geekster.InstagramBackend.Service.AdminService;
import com.geekster.InstagramBackend.Service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {


    @Autowired
    AdminService adminService;

    @Autowired
    AuthenticationService authenticationService;


    //sign up, sign in , sign out a particular instagram admin
    @PostMapping("admin/signup")
    public SignUpOutput signUpInstaAdmin(@Valid @RequestBody Admin admin) {
        return adminService.signUpAdmin(admin);
    }


    @PostMapping("admin/signIn")
    public String signInInstaAdmin(@RequestBody @Valid SignInAdmin signInAdmin) {
        return adminService.signInInstaAdmin(signInAdmin);
    }

    @DeleteMapping("admin/signOut")
    public String sigOutInstaAdmin(String email, String token) {
        if(authenticationService.authenticateAdmin(email,token)) {
            return adminService.sigOutInstaAdmin(email);
        }
        else {
            return "Sign out not allowed for non authenticated user.";
        }

    }

    // API to get all users
    @GetMapping("Users")
    public List<User> getAllUsers(){
        return adminService.getAllUsers();
    }

}
