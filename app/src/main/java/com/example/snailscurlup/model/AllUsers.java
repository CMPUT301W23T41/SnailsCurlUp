package com.example.snailscurlup.model;

import static com.example.snailscurlup.ui.scan.AbstractQR.getHash256Ins;

import android.app.Application;
import android.graphics.Bitmap;
import android.location.Address;

import androidx.annotation.Nullable;

import com.example.snailscurlup.ui.scan.QRCode;
//import com.example.snailscurlup.ui.scan.QrCode;
import com.example.snailscurlup.ui.scan.AbstractQR;
import com.example.snailscurlup.ui.scan.QRCodeInstanceNew;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// This class just holds the users list and active user as a global variable so that
// other activities can access it. Also, it is always up-to-date with firebase.
public class AllUsers extends Application {
    List<User> usersList = new ArrayList<User>();
    List<String> usernamesList = new ArrayList<String>();
    FirebaseFirestore db;
    Query allUsers;
    User activeUser;
    User wantedUser;

    // Get all users from firebase
    public void init() {
        db = FirebaseFirestore.getInstance();
        allUsers = db.collection("Users");
        allUsers.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                usersList.clear();
                usernamesList.clear();

                /**
                 * IMPORTANT NOTE: This needs to be updated once the new database stuff is in!!!
                 * TODO: FIX THIS!
                 */

                for (QueryDocumentSnapshot doc : value) {
                    String name = doc.getId();
                    String email = (String) doc.getData().get("Email");
                    String phone = (String)doc.getData().get("PhoneNumber");
                    String totalScore = doc.getData().get("Total Score").toString();
                    CollectionReference collection = db.collection("Users").document(doc.getId()).collection("QRInstancesList");
                    User user = new User(name,email,phone,totalScore);
                    collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (QueryDocumentSnapshot dc: value){
                                String name = dc.getId();
                                String QRHash = (String) dc.getData().get("data");
                                if (QRHash != null) {
                                    AbstractQR type = new AbstractQR(QRHash);
//                                    String location = (String) dc.getData().get("location");
//                                    String owner = (String) dc.getData().get("owner");
//                                    String points = (String) dc.getData().get("points");
                                    String timeStamp = (String) dc.getData().get("timestamp");
                                    try {
                                        QRCodeInstanceNew code = new QRCodeInstanceNew(type, user, Timestamp.valueOf(timeStamp));
                                        user.addScannedInstanceQrCodes(code);
                                    }catch (Exception e){
                                        QRCodeInstanceNew code = new QRCodeInstanceNew(type,user,null);
                                        user.addScannedInstanceQrCodes(code);
                                    }
                                }
                            }
                        }
                    });

                    usersList.add(user);
                    usernamesList.add(doc.getId());
                }
            }
        });
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public List<String> getUsernamesList() {
        return usernamesList;
    }

    // The following two functions are used to get a user by username
    public void userWanted(String username) {
        for(int i = 0; i < usersList.size(); i++) {
            if (usersList.get(i).getUsername() != null && usersList.get(i).getUsername().equals(username)) {
                wantedUser = usersList.get(i);
                break;
            }
        }
    }
    public User getWantedUser() {
        return wantedUser;
    }


    /********** NEW Code for abstract QR **********/
    public void addUserScanQRCode(String data, User activeUser, Bitmap scannedQRLogImage, Timestamp scanndQRLogTimeStamp, String adress) throws IOException {

        AbstractQR newabstractQRType = new AbstractQR(data);
        QRCodeInstanceNew newaQRInstance = new QRCodeInstanceNew(newabstractQRType,activeUser, scannedQRLogImage, scanndQRLogTimeStamp, adress);
        activeUser.addScannedInstanceQrCodes(newaQRInstance);


        }


        public boolean checkIfUserHasInstanceQrCode(String data,User activeUser){
            return activeUser.checkIfInstanceQrCodeExists(getHash256Ins(data));
        }
}

