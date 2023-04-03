package com.example.snailscurlup.ui.leaderboard;

/**
 * Leader class represents a user on the leaderboard. This class contains
 * information about the user such as their username and score
 */
public class Leader {
    private String username;
    private String score;


    /**
     * Constructs a new Leader with the specified username and score.
     * @param username The user's username.
     * @param score The user's score.
     */
    public Leader(String username, String score){
        this.username=username;
        this.score = score;
    }

    /**
     * @return The user's username.
     */
    public String getUsername(){return this.username;}

    /**
     * @return The user's phone number.
     */
    public String getScore(){
        return this.score;
    }

}
