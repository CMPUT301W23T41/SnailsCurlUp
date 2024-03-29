package com.example.snailscurlup.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snailscurlup.R;
import com.example.snailscurlup.UserListListener;
import com.example.snailscurlup.controllers.AllUsersController;
import com.example.snailscurlup.model.AllUsers;
import com.example.snailscurlup.model.User;
import com.example.snailscurlup.ui.scan.QRGalleryAdapter;
import com.example.snailscurlup.ui.scan.QRCodeInstanceNew;
import com.example.snailscurlup.ui.startup.StartUpActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment   {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters



    private UserListListener userListListener;
    private AllUsersController allUsersController;
    AllUsers allUsers;
    User activeUser;




    private FloatingActionButton profileFloaMenuicon;

    private Button qrGallerIcon;

    private TextView userUsernameField, userTotalScoreField, userCodeScannedField;

    private  RecyclerView QRGallery;




    public ProfileFragment() {
        // Required empty public constructor
    }
    public ProfileFragment(User activeUser) {
        this.activeUser = activeUser;
    }


    @Override
    public void onResume() {
        super.onResume();
        activeUser = userListListener.getAllUsers().getActiveUser();


    }
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPause();
        activeUser = userListListener.getAllUsers().getActiveUser();
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UserListListener) {
            userListListener = (UserListListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement UserListListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        QRGallery = view.findViewById(R.id.QRGalleryRecyclerView);

        allUsers = (AllUsers) getActivity().getApplicationContext();
        allUsers.init();

        // Only wait if active user is null at the moment
        if (allUsers.getActiveUser() == null) {
            // Wait for thread to finish
            // allUsers list is initializing...
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // retrieve active user
        if (allUsers.getActiveUser() != null) {
            activeUser = allUsers.getActiveUser();
        } else {
            activeUser = new User();
        }

        /**** commented out try for new AbstractQr implementation
        QrGalleryAdapter qrGalleryAdapter = new QrGalleryAdapter(this.getContext(), activeUser.getScannedQrCodes());
        ******/

        /****** new AbstractQr implementation ****/


            setAdapter(activeUser.getScannedInstanceQrCodes(),getParentFragmentManager());




        profileFloaMenuicon = view.findViewById(R.id.profile_floating_menuicon);
        qrGallerIcon = view.findViewById(R.id.sortby_qrgallery_button);




        userUsernameField = view.findViewById(R.id.username_fieldprof);
        userTotalScoreField = view.findViewById(R.id.totalscore_fieldprof);
        userCodeScannedField = view.findViewById(R.id.codesscanned_fieldprof);

        userUsernameField.setText(activeUser.getUsername());
        userTotalScoreField.setText(String.valueOf(activeUser.totalPointsEarned()));
        userCodeScannedField.setText(String.valueOf(activeUser.numberScannedQRCodeInstances()));

        profileFloaMenuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetMenuDialog();
            }
        });


        qrGallerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetQRGallerySortDialog();
            }
        });







        // Inflate the layo


        return view;
    }

    /*m https://www.section.io/engineering-education/bottom-sheet-dialogs-using-android-studio/ */

    private void showBottomSheetMenuDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.popup_menu_profile);

        Button logoutBtn = bottomSheetDialog.findViewById(R.id.logout_button);
        Button editProfBtn = bottomSheetDialog.findViewById(R.id.edit_profile_button);
        Button manageAccBtn = bottomSheetDialog.findViewById(R.id.manage_account_button);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

                Toast.makeText(getContext(), "Logged out", Toast.LENGTH_LONG).show();

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);;
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Sign out
                editor.putString("newAccount", "false");
                editor.putString("isLoggedIn", "false");
                editor.putString("currentUsername", null);
                editor.commit();

                // Go back to startup screen
                startActivity(new Intent(getActivity(), StartUpActivity.class));
            }
        });

        editProfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Edit Profile is Clicked", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();
            }
        });

        manageAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Manage Account is Clicked", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }


    // Dialog Menu for sorting Qr gallery
    private void showBottomSheetQRGallerySortDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.qrgallerysort_popup);

        Button ScoreDescButton = bottomSheetDialog.findViewById(R.id.sort_scoredesc_button);
        Button ScoreAscButton = bottomSheetDialog.findViewById(R.id.sort_scoreasc_button);
        Button DateNewFirstButton = bottomSheetDialog.findViewById(R.id.sort_datenewestfirst_button);
        Button DateOldFirstButton = bottomSheetDialog.findViewById(R.id.sort_dateoldestfirst_button);

        DateNewFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortedUserQRInstances(1);
                Toast.makeText(getContext(), "Sorted By Newest Date First", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();
            }
        });

        DateOldFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortedUserQRInstances(2);
                Toast.makeText(getContext(), "Sorted By Oldest Date First", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();

            }
        });

       ScoreAscButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortedUserQRInstances(3);
                Toast.makeText(getContext(), "Sorted By Ascending Score", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();
            }
        });


        ScoreDescButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortedUserQRInstances(4);
                Toast.makeText(getContext(), "Sorted By Descending Score", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();
            }

        });


        bottomSheetDialog.show();
    }

    // Adapter for Qr Gallery, set adapter based on injected arraylist of QRCodeInstanceNew


    public void setAdapter(ArrayList<QRCodeInstanceNew> qrCodeInstanceNews, FragmentManager fragmentManager) {
        QRGalleryAdapter qrGalleryAdapter = new QRGalleryAdapter(this.getContext(), qrCodeInstanceNews, fragmentManager);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

        QRGallery.setLayoutManager(linearLayoutManager);
        QRGallery.setAdapter(qrGalleryAdapter);
    }

    // Sort User QR Instances based on sortType, 1 = Date New First, 2 = Date Old First, 3 = Points Ascending, 4 = Points Descending
    public void SortedUserQRInstances(int sortType){
        /* define 4 Integer Sort types
        * 1. Sort by Date New First: 1
        * 2. Sort by Date Old First: 2
        * 3. Sort by Points Ascending: 3
        * 4. Sort by Points Descending: 4
         */

        ArrayList<QRCodeInstanceNew> sortedUserQRList = new ArrayList<>(activeUser.getScannedInstanceQrCodes());


        /*** Referenece for sorting with Collection.sort() with custom comparator sort by property, for date
         : essentially same idea as date as timestamp also have compareTo() method
         Link to post: https://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property
         Links to User Profile: https://stackoverflow.com/users/584674/james-garriss
         Users name: james-garriss
         * ***/
        // Sort by Date New First
        if(sortType == 1){
            sortedUserQRList.sort(new Comparator<QRCodeInstanceNew>() {
                @Override
                public int compare(QRCodeInstanceNew QR1, QRCodeInstanceNew QR2) {
                    return QR2.getScanQRLogTimeStamp().compareTo(QR1.getScanQRLogTimeStamp());
                }
            });
        }

        // Sort by Date Old First
        if(sortType == 2){
            sortedUserQRList.sort(new Comparator<QRCodeInstanceNew>() {
                @Override
                public int compare(QRCodeInstanceNew o1, QRCodeInstanceNew o2) {
                    return o1.getScanQRLogTimeStamp().compareTo(o2.getScanQRLogTimeStamp());
                }
            });
        }
        /******* End of Reference *******/


        /*** Referenece for sorting with Collection.sort() with custom comparator sort by property, for int
         Link to post: https://stackoverflow.com/questions/3699141/how-to-sort-an-array-of-ints-using-a-custom-comparator
         Links to User Profile: https://stackoverflow.com/users/257299/kevinarpe
         Users name: kevinarpe
         * ***/
        // Sort by Points Ascending
        if(sortType == 3){
            Collections.sort(sortedUserQRList, new Comparator<QRCodeInstanceNew>() {
                @Override
                public int compare(QRCodeInstanceNew QR1, QRCodeInstanceNew QR2) {
                    return Integer.compare(QR1.getPointsInt(),QR2.getPointsInt());
                }
            });
        }

        // Sort by Points Descending
        if(sortType == 4){
            Collections.sort(sortedUserQRList, new Comparator<QRCodeInstanceNew>() {
                @Override
                public int compare(QRCodeInstanceNew QR1, QRCodeInstanceNew QR2) {
                    return Integer.compare(QR2.getPointsInt(),QR1.getPointsInt());
                }
            });

        }
        /******* End of Reference *******/


        setAdapter(sortedUserQRList,getParentFragmentManager());

    }










    }





