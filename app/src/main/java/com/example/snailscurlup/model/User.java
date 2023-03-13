package com.example.snailscurlup.model;


import com.example.snailscurlup.ui.scan.QrCode;
import com.example.snailscurlup.ui.scan.QrCodeInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String email;
    private String phoneNumber;
    private String profilePhotoPath;

    private String device_id;

    private ArrayList<QrCode> scannedQrCodes;

    // Default Constructor
    public User() {
        this.username = null;
        this.email = null;
        this.phoneNumber = null;
        this.profilePhotoPath = null;
        this.device_id = null;
        this.scannedQrCodes = new ArrayList<>();
    }

    // Overloaded Constructor
    public User(String username, String email, String phoneNumber, String profilePhotoPath, String device_id) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePhotoPath = profilePhotoPath;
        this.device_id = device_id;
        this.scannedQrCodes = new ArrayList<>();
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }
    public String getDevice_id() {
        return device_id;
    }
    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    // toString method to display user information
    @Override
    public String toString() {
        return "Username: " + username + ", Email: " + email + ", Phone Number: " + phoneNumber + ", Profile Photo Path: " + profilePhotoPath;
    }

    public ArrayList<QrCode> getScannedQrCodes() {
        return scannedQrCodes;
    }

   public void addScannedQrCodes(QrCode scannedQrCodes) {
        this.scannedQrCodes.add(scannedQrCodes);
   }

}


