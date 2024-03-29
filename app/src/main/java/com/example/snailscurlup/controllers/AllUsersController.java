package com.example.snailscurlup.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.snailscurlup.model.User;
import com.example.snailscurlup.ui.scan.QRCode;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class AllUsersController {
    private final List<User> users;
    private User activeUser;
    private static AllUsersController instance;
    private final SharedPreferences sharedPreferences;
    private final Context context;
    FirebaseFirestore db;


    // constructor
    private AllUsersController(Context context) {
        this.context = context.getApplicationContext();
        db = FirebaseFirestore.getInstance();

        users = SharedPreferencesUtils.getAllUsers(context);
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String activeUsername = sharedPreferences.getString("activeUsername", null);
        if (activeUsername != null) {
            activeUser = SharedPreferencesUtils.getUserByUsername(context, activeUsername);
        }
    }

    // singleton ensure 1 instance
    public static AllUsersController getInstance(Context context) {
        if (instance == null) {
            instance = new AllUsersController(context);
        }
        return instance;
    }

    // Method to add a new user
    public void addUser(String username, String email, String phoneNumber, String profilePhotoPath, String device_id) {
        //local database
        User newUser = new User(username, email, phoneNumber, profilePhotoPath, device_id);
        SharedPreferencesUtils.addUser(context, newUser);
        users.add(newUser);

        //Firebase add user
        final String TAG = "Sample";
        HashMap<String, String> data = new HashMap<>();
        if (username.length() > 0) {  //if (userName.length() > 0 && email.length() > 0 && phoneNumber.length() > 0)
            data.put("Email", email);
            data.put("PhoneNumber", phoneNumber);
            data.put("Total Score", "0");
            data.put("Codes Scanned","0");
            db.collection("Users")
                    .document(username)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Data has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.d(TAG, "Data not be added!" + e.toString());
                        }
                    });
        }

        //instantiates a QR Wallet for the User
        HashMap<String, String> empty = new HashMap<>();
        db.collection("Users").document(username).collection("QRInstancesList")
                .document("dummy")
                .set(empty)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "Data not be added!" + e.toString());
                    }
                });
    }

    // Method to remove an existing user
    public void removeUser(User user) {
        users.remove(user);
        SharedPreferencesUtils.deleteUser(context, user);

        //remove user from firebase
        final String TAG = "Sample";
        db.collection("Users").document(user.getUsername())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Deleted successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document"+e.toString());
                    }
                });
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
    public AllUsersController getUsers() {
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

    // remove the active user
    public void removeActiveUser() {
        activeUser = null;
        SharedPreferencesUtils.clearActiveUser(context);
    }
    public void addQRCode(QRCode qrCode) {
        String userID = activeUser.getUsername(); // get user ID
        SharedPreferencesUtils.addQRCode(context, userID, qrCode); // store QR code with user ID in shared preferences
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