package com.example.snailscurlup.model;


import com.example.snailscurlup.controllers.Database;
import com.example.snailscurlup.ui.scan.QRCode;
import com.example.snailscurlup.ui.scan.AbstractQR;
import com.example.snailscurlup.ui.scan.QRCodeInstanceNew;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User
 * <p>
 * Represents an account to be used with the application.
 * Stores QR codes and user information.
 *
 * @author AyanB123
 */


public class User implements Serializable {

    // need delete code of once done using it in the app
    private final ArrayList<QRCodeInstanceNew> scannedQRCodes;
    // Class properties key to user
    private String username;
    private String email;
    private String phoneNumber;
    private String profilePhotoPath;
    private String device_id;
    private String totalScore;
    /**** impmeent for new abstract Qrcode ***/
    private ArrayList<QRCodeInstanceNew> scannedInstanceQrCodes;

    // Default Constructor
    public User() {
        this.username = null;
        this.email = null;
        this.phoneNumber = null;
        this.profilePhotoPath = null;
        this.device_id = null;
        this.scannedQRCodes = new ArrayList<>();
        this.scannedInstanceQrCodes = new ArrayList<>();

        // fetchQRInstancesFromDB();
    }

    public User(String username, String email, String phoneNumber, String totalScore) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.totalScore = totalScore;
        this.profilePhotoPath = null;
        this.device_id = null;
        this.scannedQRCodes = new ArrayList<>();
        this.scannedInstanceQrCodes = new ArrayList<>();

        // fetchQRInstancesFromDB();
    }


    // Overloaded Constructor
    public User(String username, String email, String phoneNumber, String profilePhotoPath, String device_id) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePhotoPath = profilePhotoPath;
        this.device_id = device_id;
        this.scannedQRCodes = new ArrayList<>();

        // fetchQRInstancesFromDB();
    }

    /**
     * Checks the database and adds any QR code instances to the profile page.
     */
//    public void fetchQRInstancesFromDB() {
//        Database db = Database.getInstance();
//        ArrayList<QRCodeInstanceNew> codesToAdd = db.getUserQRCodeInstances(this);
//        for (QRCodeInstanceNew code : codesToAdd) {
//            this.scannedInstanceQrCodes.add(code);
//        }
//    }

    public String getTotalScore() {
        return totalScore;
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

    /***************** OLD CODE  for old Qr -> need to delet remove all dependecies of will not be needed*******************/
    /**
     * Returns a list of a user's scanned QR codes.
     *
     * @return The list of the user's scanned QR codes.
     */
    public ArrayList<QRCodeInstanceNew> getScannedQrCodes() {
        return scannedQRCodes;
    }

    /**
     * Adds a QR code to the user's library of scanned QR codes.
     *
     * @param scannedQrCodes - A QrCode object to be added to library.
     */
    public void addScannedQrCodes(QRCodeInstanceNew scannedQrCodes) {
        this.scannedQRCodes.add(scannedQrCodes);
    }

    /***************** End OLD QR code*******************/


    /***************** NEW CODE  for new Instance Qr -> want and need to use*******************/

    public void addScannedInstanceQrCodes(QRCodeInstanceNew scannedInstanceQrCodes) {
        this.scannedInstanceQrCodes.add(scannedInstanceQrCodes);
    }

    public ArrayList<QRCodeInstanceNew> getScannedInstanceQrCodes() {
        return scannedInstanceQrCodes;
    }

    public Boolean checkIfInstanceQrCodeExists(String NewQrCodeHash) {
        for (QRCodeInstanceNew qrCodeInstance : scannedInstanceQrCodes) {
            if (qrCodeInstance.AbstractQRHash().equals(NewQrCodeHash)) {
                return true;
            }
        }
        return false;
    }

    public AbstractQR getAbstractQrCode(String NewQrCodeHash) {
        for (QRCodeInstanceNew qrCodeInstance : scannedInstanceQrCodes) {
            if (qrCodeInstance.AbstractQRHash().equals(NewQrCodeHash)) {
                return qrCodeInstance.getAbstractQR();
            }
        }
        return null;
    }

    public Integer numberScannedQRCodeInstances() {
        int count = 0;
        for (QRCodeInstanceNew qrCodeInstance : scannedInstanceQrCodes) {
            count++;
        }

        return count;
    }

    public Integer totalPointsEarned() {
        int totalPoints = 0;
        for (QRCodeInstanceNew qrCodeInstance : scannedInstanceQrCodes) {

            totalPoints += qrCodeInstance.getPointsInt();

        }
        return totalPoints;
    }

    public void deletescannedQRCodeInstance(QRCodeInstanceNew qrCodeInstance) {
        for (QRCodeInstanceNew qrCodeInstance1 : scannedInstanceQrCodes) {
            if (qrCodeInstance1.getAbstractQR().getHash().equals(qrCodeInstance.getAbstractQR().getHash())) {
                scannedInstanceQrCodes.remove(qrCodeInstance1);
                break;
            }
        }
    }

    public void resetscanedQRCodeInstance(ArrayList<QRCodeInstanceNew> qrCodeInstanceresetList) {
        scannedInstanceQrCodes.clear();
        scannedInstanceQrCodes = qrCodeInstanceresetList;

    }

    public void clearScannedQRCodeInstance() {
        scannedInstanceQrCodes.clear();
    }

    /***************** End of NEW QR code Instance code *******************/

}


