package com.example.snailscurlup.model;

import static com.example.snailscurlup.ui.scan.AbstractQR.getHash256Ins;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.snailscurlup.controllers.Database;
import com.example.snailscurlup.ui.scan.QRCode;
//import com.example.snailscurlup.ui.scan.QrCode;
import com.example.snailscurlup.ui.scan.AbstractQR;
import com.example.snailscurlup.ui.scan.QRCodeInstanceNew;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.concurrent.atomic.AtomicBoolean;

// This class just holds the users list and active user as a global variable so that
// other activities can access it. Also, it is always up-to-date with firebase.
public class AllUsers extends Application {
    List<User> usersList = new ArrayList<User>();
    List<String> usernamesList = new ArrayList<String>();

    private Database database;
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
                    CollectionReference collection = db.collection("Users").document(doc.getId()).collection("QRList");
                    User user = new User(name,email,phone,totalScore);
                    collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (QueryDocumentSnapshot dc: value){
                                // String qr = dc.getId();
                                // QRCode code = new QRCode(qr);
                                // user.addScannedQrCodes(code);
                                String QR = dc.getId();
                                String QRHash = (String) dc.get("data");
                                if (QRHash != null) {
                                    AbstractQR type = new AbstractQR(QRHash);
                                    // QRCode code = new QRCode(QR);
                                    // QRCodeInstanceNew code = new QRCodeInstanceNew(type, user, null, null, null);
                                    QRCodeInstanceNew code = new QRCodeInstanceNew(QRHash, user);
                                    user.addScannedQrCodes(code);
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
   /* public void addUserScanQRCode( AbstractQR newAbstractQR,  User activeUser,  Bitmap testLogPhotoBitmap, testLogTimeStamp, testaddress) throws IOException {

        QRCodeInstanceNew code = new QRCodeInstanceNew(newAbstractQR, activeUser, testLogPhotoBitmap, testLogTimeStamp, testaddress);
        AbstractQR newabstractQRType = new AbstractQR(data);
        QRCodeInstanceNew newaQRInstance = new QRCodeInstanceNew(newabstractQRType,activeUser, scannedQRLogImage, scanndQRLogTimeStamp, adress);
        activeUser.addScannedInstanceQrCodes(newaQRInstance);


        } */


    public void addUserScanQRCodewithInstance( QRCodeInstanceNew newQRInstance,  User activeUser) throws IOException {
        activeUser.addScannedInstanceQrCodes(newQRInstance);

    }

    public void deleteUserScanQRCode(User activeUser,QRCodeInstanceNew QRInstanceToDel){
        activeUser.deletescannedQRCodeInstance(QRInstanceToDel);
        database = Database.getInstance();
        database.deleteQRCodeFromUser(activeUser,QRInstanceToDel,getApplicationContext());

    }

    public ArrayList<QRCodeInstanceNew> loadUserScanQRCodefromDatabase(User activeUser){
        database = Database.getInstance();
        ArrayList<QRCodeInstanceNew> qrCodeInstancedbList = database.getUserQRCodeInstancesNew(activeUser,getApplicationContext());
        activeUser.resetscanedQRCodeInstance(qrCodeInstancedbList);
        return activeUser.getScannedInstanceQrCodes();


    }




    public boolean checkIfUserHasInstanceQrCode(String data,User activeUser){
            return activeUser.checkIfInstanceQrCodeExists(getHash256Ins(data));
        }





}


