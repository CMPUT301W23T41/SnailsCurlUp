package com.example.snailscurlup.ui.leaderboard;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.snailscurlup.R;
import com.example.snailscurlup.model.User;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A Fragment subclass for displaying the leaderboard.
 * The leaderboard can be displayed based on user scores or top scores from QR codes.
 * The data is fetched from the Firebase Firestore.
 * Use the LeaderboardFragment newInstance factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    ListView leaderboardList;
    ArrayAdapter<Leader> leaderAdapter;
    ArrayList<Leader> leaderDataList;
    Button cumulativeButton;
    Button TopQrButton;
    FirebaseFirestore db;

    public LeaderboardFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderboardFragment.
     */
    public static LeaderboardFragment newInstance(String param1, String param2) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to do initial creation of the fragment.
     * This is called after onAttach and before onCreateView.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     * this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard,container, false);
        cumulativeButton = view.findViewById(R.id.buttonUserList);   //views
        TopQrButton = view.findViewById(R.id.buttonQRList);
        leaderboardList = view.findViewById(R.id.leader_list);

        leaderDataList = new ArrayList<>();                     //list and adapter
        leaderAdapter = new com.example.snailscurlup.ui.leaderboard.LeaderList(getActivity(), leaderDataList);
        leaderboardList.setAdapter(leaderAdapter);
        db = FirebaseFirestore.getInstance();

        cumulativeButton.setBackgroundColor(getContext().getColor(R.color.button_selected));
        TopQrButton.setBackgroundColor(getContext().getColor(R.color.secondary_color));

        Query User_leaderboard = db.collection("Users").orderBy("Total Score", Query.Direction.DESCENDING);   //query
        Query QR_leaderboard = db.collection("QR").orderBy("score", Query.Direction.DESCENDING);

        User_leaderboard.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                leaderDataList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    String UserName = doc.getId();
                    String Score = (String) doc.getData().get("Total Score");
                    leaderDataList.add(new Leader(UserName, Score));
                }
                leaderAdapter.notifyDataSetChanged();
            }
        });

        cumulativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cumulativeButton.setBackgroundColor(getContext().getColor(R.color.button_selected));
                TopQrButton.setBackgroundColor(getContext().getColor(R.color.secondary_color));
                User_leaderboard.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        leaderDataList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            String UserName = doc.getId();
                            String Score = (String) doc.getData().get("Total Score");
                            leaderDataList.add(new Leader(UserName, Score));
                        }
                        leaderAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        TopQrButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               TopQrButton.setBackgroundColor(getContext().getColor(R.color.button_selected));
               cumulativeButton.setBackgroundColor(getContext().getColor(R.color.secondary_color));
               QR_leaderboard.addSnapshotListener(new EventListener<QuerySnapshot>() {
                   @Override
                   public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                       leaderDataList.clear();
                       for (QueryDocumentSnapshot doc : value) {
                           String UserName = (String) doc.getData().get("name");
                           String Score = (String) doc.getData().get("score");
                           leaderDataList.add(new Leader(UserName, Score));
                       }
                       leaderAdapter.notifyDataSetChanged();
                   }
               });
           }
       }
        );

        return view;
    }
}