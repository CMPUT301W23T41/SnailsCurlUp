package com.example.snailscurlup.model;

import android.app.Application;

import androidx.annotation.Nullable;

import com.example.snailscurlup.ui.scan.QrCode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllUsers extends Application{
    List<User> usersList = new ArrayList<User>();
    List<String> usernamesList = new ArrayList<String>();
    FirebaseFirestore db;
    Query allUsers;
    User activeUser;

    // Get all users from firebase
    public void init() {
        db = FirebaseFirestore.getInstance();
        allUsers = db.collection("Users");
        allUsers.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                usersList.clear();
                usernamesList.clear();

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
                                String qr = dc.getId();
                                QrCode code = new QrCode(qr);
                                user.addScannedQrCodes(code);
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
}