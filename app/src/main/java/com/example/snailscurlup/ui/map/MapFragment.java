package com.example.snailscurlup.ui.map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

// import com.example.snailscurlup.databinding.ActivityMapsBinding;
import com.example.snailscurlup.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    FusedLocationProviderClient fusedLocationProviderClient;
    // private ActivityMapsBinding binding;
    LatLng userPosition;

    MapView mapView;
    GoogleMap map;

    FirebaseFirestore db;

    public void fetchCurrentUserLocation() {
        /**
         * Fetches the coordinates of the user's device and stores them in
         * longitude/latitude variables for later use.
         * @params None
         * @returns Coordinates of the user
         */
        // Get user location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        if (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {

            fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {

                                try {
                                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    // Double latitude = addressList.get(0).getLatitude();
                                    // Double longitude = addressList.get(0).getLongitude();
                                    double longi = addressList.get(0).getLongitude();
                                    double lat = addressList.get(0).getLatitude();
                                    // String address = addressList.get(0).getAddressLine(0);

                                    // TODO: UPLOAD LOCATION TO FIREBASE

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    // See this: https://stackoverflow.com/questions/16536414/how-to-use-mapview-in-android-using-google-map-v2

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mappreview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /**
         * Callback method for when the map element is ready.
         * This function populates the map with markers and other information.
         * @param googleMap - The Google Map element to be populated
         * @returns None
         */
        map = googleMap;
        // Try to set user location if allowed, skip it otherwise
        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Log.d("SECURITY", e.toString());
        }
        // Get QR Code markers from database
        // TODO: In the future, do this based on proximity (not ALL of them)
        db.collection("QRMapMarkers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Extract coordinates and name of each marker, plot them all
                                GeoPoint coords = document.getGeoPoint("location");
                                if (coords != null) {
                                    // Only add markers with geopoints attached to them
                                    String markerName = document.getString("name");
                                    double lat = coords.getLatitude();
                                    double lng = coords.getLongitude();
                                    LatLng marker = new LatLng(lat, lng);
                                    map.addMarker(new MarkerOptions().position(marker).title(markerName));
                                }
                                Log.d("FIREBASE", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("FIREBASE", "Error getting documents: ", task.getException());
                        }
                    }
                });
        // Move camera to North America roughly (fix this later maybe?)
        // map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(43.1, -87.9)));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        if (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
            fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {

                                try {
                                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    double latitude = addressList.get(0).getLatitude();
                                    double longitude = addressList.get(0).getLongitude();
                                    String address = addressList.get(0).getAddressLine(0);

                                    // map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));

                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                                            .zoom(17)                   // Sets the zoom
                                            // .bearing(90)                // Sets the orientation of the camera to east
                                            // .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                            .build();                   // Creates a CameraPosition from the builder
                                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                    // TODO: UPLOAD LOCATION TO FIREBASE

                                } catch (IOException e) {
                                    System.out.println("Exception with finding user location");
                                }
                            }
                        }
                    });
        }
    }

    // The following overrides are necessary or else the map can stop working sometimes.

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}