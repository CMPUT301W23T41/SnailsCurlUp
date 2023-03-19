package com.example.snailscurlup.ui.scan;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;


import com.example.snailscurlup.R;
import com.example.snailscurlup.UserListListener;
import com.example.snailscurlup.controllers.AllUsers;
import com.example.snailscurlup.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DecodeFragment extends Fragment{

    View view;
    final int CameraRequestCode = 2;
    TextView photoStatus, geolocationStatus;
    NamesOfQR names = new NamesOfQR();
    FusedLocationProviderClient fusedLocationProviderClient;

//    private NavigationHeaderListener NavBarHeadListener; // get reference to main activity so that we can set  Haeder In bit
    private UserListListener userListListener; // get reference to main activity so that we can set  Haeder In bit

    private String data;

    private User activeUser;

    private AllUsers allUsers;
    private QrCode newQRCode;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UserListListener) {
            userListListener = (UserListListener) context;
//            NavBarHeadListener = (NavigationHeaderListener) context;
        } else {
            throw new RuntimeException(context + " must implement UserListListener, or NavBarHeadlIstner");
        }
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_scan_decode, container, false);
        photoStatus = view.findViewById(R.id.photo_status);
        geolocationStatus = view.findViewById(R.id.geolocation_status);

        // Receive the data from the QR code
        getParentFragmentManager().setFragmentResultListener("dataFromQR", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                // Retrieve data from QR code
                String data = result.getString("scanData");

                // get object for new Qr code
                newQRCode = new QrCode(data);


                // Get the seed value using the hash value
                // Using BigInteger because hash value is very big
                // BigInteger seed = new BigInteger(hash, 16);

                // Get and set image using seed value
                ImageView imageView = view.findViewById(R.id.QR_image);
                // String URL = "https://picsum.photos/seed/" + String.valueOf(seed) + "/270";
                Picasso.get()
                        .load(newQRCode.getURL())
                        .into(imageView);


                // Get and set name using seed value
                // Random random = new Random(seed.longValue());
                // int adjIndex = random.nextInt(names.adjectives.length);
                // String name = names.adjectives[adjIndex];
                TextView nameView = view.findViewById(R.id.QR_name);
                nameView.setText(newQRCode.getName());

                // Get and set points

                TextView pointsView = view.findViewById(R.id.QR_points);
                String points = String.valueOf(newQRCode.getPointsInt()) + " points";
                pointsView.setText(points);
            }
        });

        // Photo Button
        Button addPhotoButton = view.findViewById(R.id.photo_button);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CameraRequestCode);
            }
        });


        // Geolocation Button
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        Button addGeoButton = view.findViewById(R.id.geolocation_button);
        addGeoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                    CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
                    fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    setGeoLocation(location);

                                    if (location != null) {

                                        try {
                                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                            double latitude = addressList.get(0).getLatitude();
                                            double longitude = addressList.get(0).getLongitude();
                                            String address = addressList.get(0).getAddressLine(0);

                                            // TODO: UPLOAD LOCATION TO FIREBASE



                                            geolocationStatus.setText("Added Successfully!");
                                        } catch (IOException e) {
                                            System.out.println("Exception occurred with location");
                                        }
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "You need to enable Location permission from Settings", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Save Button
        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: INSERT FIREBASE CODE

                // retrieve active user
                // retrieve active user
                activeUser =  userListListener.getAllUsers().getActiveUser();
                userListListener.getAllUsers().getActiveUser().addScannedQrCodes(newQRCode);

                // Go back to scan fragment
                Fragment fragment = new ScanFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CameraRequestCode && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            photoStatus.setText("Added Successfully!");

            // TODO: UPLOAD PHOTO TO FIREBASE

        }
    }



    public QrCode getQrCode(String data){
        QrCode QrCode = new QrCode(data);
        return QrCode;
    }

    public void setGeoLocation(Location currlocation){
        boolean isvalid = false;
        if (currlocation!= null) {

            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addressList = geocoder.getFromLocation(currlocation.getLatitude(), currlocation.getLongitude(),1);
                newQRCode.setscanadresslist(addressList);


                // TODO: UPLOAD LOCATION TO FIREBASE



                geolocationStatus.setText("Added Successfully!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } {
                System.out.println("Exception occurred with location");
                isvalid=false;
            }
        }
    }



}

