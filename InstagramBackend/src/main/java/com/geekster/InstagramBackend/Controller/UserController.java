package com.geekster.InstagramBackend.Controller;

import com.geekster.InstagramBackend.Model.*;
import com.geekster.InstagramBackend.Model.dto.SignInInput;
import com.geekster.InstagramBackend.Model.dto.SignUpOutput;
import com.geekster.InstagramBackend.Service.AuthenticationService;
import com.geekster.InstagramBackend.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

    //sign up, sign in , sign out a particular instagram user
    @PostMapping("user/signup")
    public SignUpOutput signUpInstaUser(@Valid @RequestBody User user) {
        return userService.signUpUser(user);
    }

    @PostMapping("user/signIn")
    public String sigInInstaUser(@RequestBody @Valid SignInInput signInInput) {
        return userService.signInUser(signInInput);
    }

    @DeleteMapping("user/signOut")
    public String sigOutInstaUser(String email, String token) {
        if(authenticationService.authenticate(email,token)) {
            return userService.sigOutUser(email);
        }
        else {
            return "Sign out not allowed for non authenticated user.";
        }

    }

    // content on instagram
    @PostMapping("post")
    public String createInstaPost (@RequestBody Post post, @RequestParam String postOwnerEmail, @RequestParam String token){
        if(authenticationService.authenticate(postOwnerEmail,token)) {
            return userService.createInstaPost(post,postOwnerEmail);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }

    @DeleteMapping("post")
    public String removeInstaPost(@RequestParam Integer postId, @RequestParam String email, @RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return userService.removeInstaPost(postId,email);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }

    // commenting functionalities on instagram
    @PostMapping("comment")
    public String addPostComment(@RequestBody Comment comment, @RequestParam String commenterEmail, @RequestParam String commenterAuthToken){
        if(authenticationService.authenticate(commenterEmail, commenterAuthToken)){
            return userService.addPostComment(comment, commenterEmail);
        }
        else{
            return "Non authenticated user!!";
        }
    }
    /*    comment deleter has to pass commentId foe comment to be deleted, deleters email, so it can be verified if he has the authority
          to delete the comment and his authentication Token to know if he is a signed-in user      */
    @DeleteMapping("comment")
    public String removeComment (@RequestParam Integer commentId, @RequestParam String deletersEmail, @RequestParam String userAuthToken){
        if(authenticationService.authenticate(deletersEmail, userAuthToken)){
            return userService.removeComment(commentId, deletersEmail);
        }
        else{
            return "Not authenticated user!!";
        }
    }

     // Like functionalities in instagram
    @PostMapping("like")
    public String addLike(@RequestBody Like like, @RequestParam String likersEmail, @RequestParam String likersAuthToken){
        if(authenticationService.authenticate(likersEmail, likersAuthToken)){
            return userService.addLike(like, likersEmail);
        }
        else{
            return "Not an authenticated user activity!!";
        }
    }

    // Get total number of likes on a post
    @GetMapping("like/count/postId/{postId}")
    public String getTotalLikesOnAPost(@PathVariable Integer postId, @RequestParam String userEmail, @RequestParam String userAuthToken){
        if(authenticationService.authenticate(userEmail, userAuthToken)){
            return userService.getTotalLikesOnAPost(postId);
        }
        else{
            return "Non Authenticated activity!!";
        }
    }

    @DeleteMapping("like")
    public String removeLike(@RequestParam Integer likeId, @RequestParam String likeRemoversEmail, @RequestParam String likeRemoversAuthToken){
        if(authenticationService.authenticate(likeRemoversEmail, likeRemoversAuthToken)){
            return userService.removeLike(likeId, likeRemoversEmail);
        }
        else{
            return "Not a authenticated user activity!!";
        }
    }

    //   follow functionality in instagram
    @PostMapping("follow")
    public String followSomeone (@RequestBody Follow follow, @RequestParam String usersFollowerEmail, @RequestParam String usersFollowerAuthToken){
        if(authenticationService.authenticate(usersFollowerEmail, usersFollowerAuthToken)){
            return userService.followSomeone(follow, usersFollowerEmail);
        }
        else{
            return "Not an Authenticated user activity!!";
        }
    }

    @DeleteMapping("unfollow/target/{followId}")
    public String unfollowSomeone(@PathVariable Integer followId, @RequestParam String usersFollowerEmail, @RequestParam String usersFollowerAuthToken) {
        if(authenticationService.authenticate(usersFollowerEmail, usersFollowerAuthToken)){
            return userService.unfollowSomeone(followId, usersFollowerEmail);
        }
        else{
            return "Not a Authenticated User activity!!";
        }
    }

}




















