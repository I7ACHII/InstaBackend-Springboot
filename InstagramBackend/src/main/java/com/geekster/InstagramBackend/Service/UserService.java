package com.geekster.InstagramBackend.Service;

import com.geekster.InstagramBackend.Model.*;
import com.geekster.InstagramBackend.Model.dto.SignInInput;
import com.geekster.InstagramBackend.Model.dto.SignUpOutput;
import com.geekster.InstagramBackend.Repository.IUserRepo;
import com.geekster.InstagramBackend.Service.emailUtility.EmailHandler;
import com.geekster.InstagramBackend.Service.hashingUtility.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    IUserRepo iUserRepo;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    public SignUpOutput signUpUser(User user) {

        boolean signUpStatus = true;
        String signUpStatusMessage = null;

        // checking if user has provided a null email ID
        String newEmail = user.getUserEmail();
        if(newEmail == null){
            signUpStatusMessage = "Invalid email!!";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }

        // check if this user email already exists??
        User existingUser = iUserRepo.findFirstByUserEmail(newEmail);
        if(existingUser != null){
            signUpStatusMessage = "Email already registered!!";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }


        //hash the password: encrypt the password
        try {
            String encryptedPassword = PasswordEncryptor.encryptPassword(user.getUserPassword());

            //save the user with the new encrypted password
            user.setUserPassword(encryptedPassword);
            iUserRepo.save(user);
            return new SignUpOutput(signUpStatus, "User registered successfully!!!");

        } catch (NoSuchAlgorithmException e) {
            signUpStatusMessage = "Internal error occurred during sign up";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
    }

    public String signInUser(SignInInput signInInput) {

        // If email provided by user for sign in is null, then it will return invalid email
        String signInEmail = signInInput.getEmail();
        if(signInEmail == null) {
            return "Invalid Email";
        }

        //check if this user email already exists ??
        User existingUser = iUserRepo.findFirstByUserEmail(signInEmail);

        if(existingUser == null) {
             return "Email not registered!!!";
        }

        //match passwords :
        //hash the password: encrypt the password

        try {
            String encryptPassword = PasswordEncryptor.encryptPassword(signInInput.getPassword());
            if(existingUser.getUserPassword().equals(encryptPassword)){

                AuthenticationToken authenticationToken = new AuthenticationToken(existingUser);
                authenticationService.saveAuthToken(authenticationToken);
                EmailHandler.sendEmail("mainakgh1@gmail.com","email testing", authenticationToken.getTokenValue());
                return "Token has been sent to your email Id!!";
            }
            else{
                return "Invalid credentials!!";
            }
        } catch (Exception e) {
            return "Internal error occurred during sign in";
        }
    }


    public String sigOutUser(String email) {
        User user = iUserRepo.findFirstByUserEmail(email);
        AuthenticationToken token = authenticationService.findFirstByUser(user);
        authenticationService.removeToken(token);
        return "User has signed out successfully";
    }

    public String createInstaPost(Post post, String email) {
        User postOwner = iUserRepo.findFirstByUserEmail(email);
        post.setPostOwner(postOwner);
        return postService.createInstaPost(post);
    }

    public String removeInstaPost(Integer postId, String email) {
        return postService.removeInstaPost(postId, email);
    }

    public String addPostComment(Comment comment, String commenterEmail) {
        if(postService.validatePost(comment.getInstaPost())){

            comment.setCommenter(iUserRepo.findFirstByUserEmail(commenterEmail));
            return commentService.addPostComment(comment);
        }
        else{
            return "Post not available!!";
        }
    }

    public String removeComment(Integer commentId, String deletersEmail) {
        if(validateUser(commentId, deletersEmail)){
            return commentService.removeComment(commentId);
        }
        else{
            return "Comment cannot be deleted!!";
        }
    }

    private boolean validateUser(Integer commentId, String deletersEmail) {
        Comment comment = commentService.getCommentById(commentId);
        if(comment == null){
            return false;
        }
        String commenterEmail = comment.getCommenter().getUserEmail();
        String comments_postOwnerEmail = comment.getInstaPost().getPostOwner().getUserEmail();

        if(commenterEmail.equals(deletersEmail) || comments_postOwnerEmail.equals(deletersEmail)){
            return true;
        }
        return false;
    }

    public String addLike(Like like, String likerEmail) {

        Post post = postService.findPostByPostId(like.getInstaPost().getPostId());
        if(post != null && postService.validatePost(post) ){

            User liker = iUserRepo.findFirstByUserEmail(likerEmail);
            if(likeService.postCanBeLikedOrNot(liker, post)){

                like.setLiker(liker);
                return likeService.addLike(like);
            }
            else{
                return "Already liked!!";
            }
        }
        else{
            return "Post does not exists!!";
        }
    }

    public String getTotalLikesOnAPost(Integer postId) {
        Post post = postService.findPostByPostId(postId);
        if(post != null && postService.validatePost(post)){
            return likeService.getTotalLikesOnAPost(post);
        }
        else{
            return "Not a valid postId!!";
        }
    }

    public String removeLike(Integer likeId, String likeRemoversEmail) {
        return likeService.removeLike(likeId, likeRemoversEmail);
    }

    public String followSomeone(Follow follow, String usersFollowerEmail) {
        User user = iUserRepo.findById(follow.getUser().getUserId()).orElse(null);
        if(user != null){

            User usersFollower = iUserRepo.findFirstByUserEmail(usersFollowerEmail);
            if(user.getUserId().equals(usersFollower.getUserId())){
                return "You cannot follow Yourself!!";
            }
            if( followService.followAllowedOrNot(user, usersFollower)){
                follow.setUsersFollower(usersFollower);
                followService.followSomeone(follow);
                return usersFollower.getUserHandle()  + " is now following " + user.getUserHandle();
            }
            else{
                return usersFollower.getUserHandle()  + " already follows " + user.getUserHandle();
            }
        }
        else{
            return "Not a valid user!!";
        }
    }

    public String unfollowSomeone(Integer followId, String usersFollowerEmail) {
        Follow followRecord = followService.findById(followId);
        if(followRecord != null){

            if( unfollowAllowedOrNot(followRecord, usersFollowerEmail)){
                return followService.unfollowSomeone(followRecord);
            }
            else{
                return "Unauthorized unfollow detected...Not allowed!!!!";
            }
        }
        else{
            return "Invalid follow mapping!!";
        }
    }

    private boolean unfollowAllowedOrNot(Follow followRecord, String usersFollowerEmail) {
            // taking out user from provided email Id
            User userFromFollowerEmail = iUserRepo.findFirstByUserEmail(usersFollowerEmail);

            // Taking out user from followRecord that we have get above
            User userFromFollowRecord = followRecord.getUsersFollower();

            return userFromFollowerEmail.equals(userFromFollowRecord) || usersFollowerEmail.equals(followRecord.getUser().getUserEmail());

    }

    public List<User> getAllUsers() {
        List<User> users = iUserRepo.findAll();
        return users;
    }

}
