package com.example.snailscurlup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snailscurlup.ui.scan.AbstractQR;
import com.example.snailscurlup.ui.scan.QRCodeInstanceNew;
import com.example.snailscurlup.ui.scan.QRGalleryAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.snailscurlup.R;
import com.example.snailscurlup.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;

public class Searched_User_Profile extends AppCompatActivity {

    private Intent intent;
    private ImageButton backPageButton;
    private final int key = -1;
    private final String userKey = "User Object";
    private final String localID = "local Host's id";
    private final String timeStamp = "timeStamp";
    private String activeUserID;
    private User selectedUser;
    private final String selectedUserString = "selectedUser";
    private TextView username_fieldprof;
    private TextView totalscore_fieldprof;
    private TextView codesscanned_fieldprof;
    private RecyclerView QrRecylerView;
    private QRGalleryAdapter qrGalleryAdapter;
    private String[] timeStampArray;
    private FirebaseFirestore db;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_user_profile);
        backPageButton = findViewById(R.id.backPage);
        username_fieldprof = findViewById(R.id.username_fieldprof);
        totalscore_fieldprof = findViewById(R.id.totalscore_fieldprof);
        codesscanned_fieldprof = findViewById(R.id.codesscanned_fieldprof);
        QrRecylerView = findViewById(R.id.QRGalleryRecyclerView);


        intent = getIntent();
        timeStampArray = intent.getStringArrayExtra(timeStamp);
        activeUserID = intent.getStringExtra(localID);
        String selectedUserName = intent.getStringExtra(selectedUserString);
        getSelectedUser(selectedUserName);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            selectedUser = intent.getSerializableExtra(userKey,User.class);
//        }










        backPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getSelectedUser(String name ) {
        User u = new User();
        db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("Users");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: value)
                {
                    if(doc.getId().equals(name)){
                        u.setUsername(name);
                        u.setEmail((String)doc.getData().get("Email"));
                        u.setPhoneNumber((String)doc.getData().get("PhoneNumber"));
                        u.setTotalScore((String)doc.getData().get("Total Score"));
                        CollectionReference collection = db.collection("Users").document(doc.getId()).collection("QRInstancesList");
                        collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for (QueryDocumentSnapshot dc: value){
                                    String name = dc.getId();
                                    String QRHash = (String) dc.getData().get("data");
                                    if (QRHash != null) {
                                        AbstractQR type = new AbstractQR(QRHash);
                                        String location = (String) dc.getData().get("location");
                                        String owner = (String) dc.getData().get("owner");
                                        String points = (String) dc.getData().get("points");
                                        String timeStamp = (String) dc.getData().get("timestamp");

                                        QRCodeInstanceNew code = new QRCodeInstanceNew(type, u, Timestamp.valueOf(timeStamp));
                                        u.addScannedInstanceQrCodes(code);
                                    }
                                }
                                selectedUser = u;
                                username_fieldprof.setText(selectedUser.getUsername());
                                totalscore_fieldprof.setText(selectedUser.getTotalScore());
                                String size = String.valueOf(selectedUser.getScannedInstanceQrCodes().size());
                                codesscanned_fieldprof.setText(size);

                                int a = selectedUser.getScannedQrCodes().size();

                                for (int i=0;i<selectedUser.getScannedInstanceQrCodes().size();i++){
                                    selectedUser.getScannedInstanceQrCodes().get(i).setScanQRLogTimeStamp(Timestamp.valueOf(timeStampArray[i]));
                                }

                                QrRecylerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
                                qrGalleryAdapter = new QRGalleryAdapter(context, selectedUser.getScannedInstanceQrCodes(),getSupportFragmentManager(),selectedUser);
                                QrRecylerView.setAdapter(qrGalleryAdapter);
                            }
                        });
                    }
                }
            }
        });
//        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists()){
//                    u.setUsername(name);
//                    u.setEmail((String)documentSnapshot.getData().get("Email"));
//                    u.setPhoneNumber((String)documentSnapshot.getData().get("PhoneNumber"));
//                    u.setTotalScore((String)documentSnapshot.getData().get("Total Score"));
//
//                    CollectionReference collection = db.collection("Users").document(documentSnapshot.getId()).collection("QRInstancesList");
//                    collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                            for (QueryDocumentSnapshot dc: value){
//                                String name = dc.getId();
//                                String QRHash = (String) dc.getData().get("data");
//                                if (QRHash != null) {
//                                    AbstractQR type = new AbstractQR(QRHash);
//                                    String location = (String) dc.getData().get("location");
//                                    String owner = (String) dc.getData().get("owner");
//                                    String points = (String) dc.getData().get("points");
//                                    String timeStamp = (String) dc.getData().get("timestamp");
//
//                                    QRCodeInstanceNew code = new QRCodeInstanceNew(type, u, Timestamp.valueOf(timeStamp));
//                                    u.addScannedInstanceQrCodes(code);
//                                }
//                            }
//                        }
//                    });
//                }
//            }
//        });

    }
}