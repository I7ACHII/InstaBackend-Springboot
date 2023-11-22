package com.geekster.InstagramBackend.Service;

import com.geekster.InstagramBackend.Model.Admin;
import com.geekster.InstagramBackend.Model.AuthenticationToken;
import com.geekster.InstagramBackend.Model.User;
import com.geekster.InstagramBackend.Model.dto.SignInAdmin;
import com.geekster.InstagramBackend.Model.dto.SignUpOutput;
import com.geekster.InstagramBackend.Repository.IAdminRepo;
import com.geekster.InstagramBackend.Service.emailUtility.EmailHandler;
import com.geekster.InstagramBackend.Service.hashingUtility.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class AdminService {


    @Autowired
    IAdminRepo iAdminRepo;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;
    public SignUpOutput signUpAdmin(Admin admin) {

        boolean signUpStatus = true;
        String signUpStatusMessage = null;

        // checking if admin has provided a null email ID
        String newEmail = admin.getAdminEmail();
        if(newEmail == null){
            signUpStatusMessage = "Invalid email!!";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }

        // check if this admin email already exists??
        Admin existingAdmin = iAdminRepo.findFirstByAdminEmail(newEmail);
        if(existingAdmin != null){
            signUpStatusMessage = "Email already registered!!";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }


        //hash the password: encrypt the password
        try {
            String encryptedPassword = PasswordEncryptor.encryptPassword(admin.getAdminPassword());

            //save the admin with the new encrypted password
            admin.setAdminPassword(encryptedPassword);
            iAdminRepo.save(admin);
            return new SignUpOutput(signUpStatus, "Admin registered successfully!!!");

        } catch (NoSuchAlgorithmException e) {
            signUpStatusMessage = "Internal error occurred during sign up";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }

    }

    public String signInInstaAdmin(SignInAdmin signInAdmin) {

        // If email provided by admin for sign in is null, then it will return invalid email
        String signInEmail = signInAdmin.getEmail();
        if(signInEmail == null) {
            return "Invalid Email";
        }

        //check if this admin email already exists ??
        Admin existingAdmin = iAdminRepo.findFirstByAdminEmail(signInEmail);

        if(existingAdmin == null) {
            return "Email not registered!!!";
        }

        //match passwords :
        //hash the password: encrypt the password

        try {
            String encryptPassword = PasswordEncryptor.encryptPassword(signInAdmin.getPassword());
            if(existingAdmin.getAdminPassword().equals(encryptPassword)){

                AuthenticationToken authenticationToken = new AuthenticationToken(existingAdmin);
                authenticationService.saveAuthToken(authenticationToken);
                EmailHandler.sendEmail("pulkitmittal194@gmail.com","email testing", authenticationToken.getTokenValue());
                return "Sign In Successful! Token has been sent to your email Id!!";
            }
            else{
                return "Invalid credentials!!";
            }
        } catch (Exception e) {
            return "Internal error occurred during sign in";
        }
    }

    public String sigOutInstaAdmin(String email) {
        Admin admin = iAdminRepo.findFirstByAdminEmail(email);
        AuthenticationToken token = authenticationService.findFirstByAdmin(admin);
        authenticationService.removeToken(token);
        return "Admin has signed out successfully";
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


}
