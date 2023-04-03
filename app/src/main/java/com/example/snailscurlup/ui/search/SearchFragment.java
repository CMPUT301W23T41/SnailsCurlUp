package com.example.snailscurlup.ui.search;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;


import com.example.snailscurlup.R;
import com.example.snailscurlup.ui.scan.AbstractQR;
import com.example.snailscurlup.ui.scan.QRCode;
import com.example.snailscurlup.ui.scan.QRCodeInstanceNew;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.snailscurlup.model.User;

import java.sql.Timestamp;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private ArrayList<User> userList;
    private ArrayAdapter<User> userAdapter;

    private ListView listView;
    private User activeUser;

    private ImageButton searchButton;

    private FirebaseFirestore db;
    public SearchFragment() {
        // Required empty public constructor
    }

    public SearchFragment(User u) {
        this.activeUser = u;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userList = new ArrayList<User>();
        userAdapter = new UserAdapter(getActivity(),userList, activeUser);

        // load the User file and put it in UserList here
    }
    String value;
    public String createSearchResult(String input, View view){
        db = FirebaseFirestore.getInstance();
        CollectionReference reference =  db.collection("Users");
        Query searchQuery;
        if (input.equals("")){
            searchQuery = reference;
        }else{
            searchQuery = reference.orderBy(FieldPath.documentId()).startAt(input).endAt(input+"\uf8ff");
        }

        /**
         * TODO: The code below NEEDS to be reworked once new database stuff is up and running.
         * Query below will most likely not work. BEWARE!
         */

        searchQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent( QuerySnapshot queryDocumentSnapshots,
                        FirebaseFirestoreException error) {
                    userList.clear();

                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                    {

                        String name = doc.getId();
                        String email = (String) doc.getData().get("Email");
                        String phone = (String)doc.getData().get("PhoneNumber");
                        String totalScore = doc.getData().get("Total Score").toString();
                        CollectionReference collection = db.collection("Users").document(doc.getId()).collection("QRInstancesList");
                        User user1 = new User(name,email,phone,totalScore);
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

                                        QRCodeInstanceNew code = new QRCodeInstanceNew(type, user1, Timestamp.valueOf(timeStamp));
                                        user1.addScannedInstanceQrCodes(code);
                                    }
                                }
                                userList.add(user1);
                            }
                        });

                    }
                    userAdapter.notifyDataSetChanged();
                }
            });
        return value;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container, false);
        listView = view.findViewById(R.id.user_list);
        listView.setAdapter(userAdapter);

        searchButton = view.findViewById(R.id.search_button);




        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                MockUser mu = new MockUser("chen","email","778302",5);
//                userList.add(mu);
//                userAdapter.notifyDataSetChanged();
                EditText inputField = view.findViewById(R.id.search_bar_input_text);
                String input = inputField.getText().toString();


                createSearchResult(input,view);
                //startAt(input).endAt(input+"\uf8ff")
            }
        });

        // Inflate the layout for this fragment
        return view;

    }
}