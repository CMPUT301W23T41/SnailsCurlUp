package com.example.snailscurlup.ui.scan;


import java.sql.Timestamp;



/**
 * AbstractQRComment
 * <p>
 * This class is used to store the information about the comments that are added to the QR code.
 * It is used to store the username, message, and timestamp of the comment.
 *
 * @author AyanB123
 */

public class AbstractQRComment {


    private String message, username, userprofileimg;


    // Timestamp of comment added
    private Timestamp commentTimeStamp;


    public AbstractQRComment() {
    }

    public AbstractQRComment(String username, String content,Timestamp commentTime) {
        this.message = content;
        this.username = username;

        //implement user profile image once User has profile image implemented
        //this. userprofileimg = userprofileimg;

        this.commentTimeStamp = commentTime;

    }


    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserprofileimg() {
        return userprofileimg;
    }

    public void setUserprofileimg(String userprofileimg) {
        this.userprofileimg = userprofileimg;
    }


    public Timestamp getTimestamp() {
        return commentTimeStamp;
    }


}