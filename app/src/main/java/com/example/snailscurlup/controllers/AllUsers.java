package com.example.snailscurlup.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.snailscurlup.model.User;

import java.util.List;

public class AllUsers {
    private final List<User> users;
    private User activeUser;
    private static AllUsers instance;
    private final SharedPreferences sharedPreferences;
    private final Context context;

    // constructor
    private AllUsers(Context context) {
        this.context = context.getApplicationContext();
        users = SharedPreferencesUtils.getAllUsers(context);
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String activeUsername = sharedPreferences.getString("activeUsername", null);
        if (activeUsername != null) {
            activeUser = SharedPreferencesUtils.getUserByUsername(context, activeUsername);
        }
    }

    // singleton ensure 1 instance
    public static AllUsers getInstance(Context context) {
        if (instance == null) {
            instance = new AllUsers(context);
        }
        return instance;
    }

    // Method to add a new user
    public void addUser(String username, String email, String phoneNumber, String profilePhotoPath, String device_id) {
        User newUser = new User(username, email, phoneNumber, profilePhotoPath, device_id);
        SharedPreferencesUtils.addUser(context, newUser);
        users.add(newUser);
    }

    // Method to remove an existing user
    public void removeUser(User user) {
        users.remove(user);
        SharedPreferencesUtils.deleteUser(context, user);
    }

    //check if a username already exists
    public boolean isUsernameTaken(String username) {
        return SharedPreferencesUtils.isUsernameTaken(context, username);
    }

    // a user by their username
    public User getUserByUsername(String username) {
        return SharedPreferencesUtils.getUserByUsername(context, username);
    }

    //  check if a user is valid
    public boolean isUserValid(String username) {
        return SharedPreferencesUtils.getUserByUsername(context, username) != null;
    }

    // Getter for all users
    public AllUsers getUsers() {
        return this;
    }

    // et the active user
    public void setActiveUser(User user) {
        activeUser = user;
        SharedPreferencesUtils.setActiveUser(context, user);
    }

    //  get the active user
    public User getActiveUser() {
        return activeUser;
    }

    // Method to check if there is an active user
    public boolean hasActiveUser() {
        return activeUser != null;
    }

    // emove the active user
    public void removeActiveUser() {
        activeUser = null;
        SharedPreferencesUtils.clearActiveUser(context);
    }

    //  update a user
    public void updateUser(User editeduser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(editeduser.getUsername())) {
                users.set(i, editeduser);
                SharedPreferencesUtils.updateUser(context, editeduser);
                break;
            }
        }
    }
}
