package com.example.catchqr;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;


public class ScanFragment extends Fragment {

    private CodeScanner scanner;
    boolean CameraPermission = false;
    final int CameraRequestCode = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);

        scanner = new CodeScanner(this.getActivity(),scannerView);
        getCameraPermission();

        if (CameraPermission) {

            scanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_LONG).show();
                            // or use an alert dialog box
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Result");
                            builder.setMessage(result.getText());
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                        }
                    });
                }
            });

            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    scanner.startPreview();

                }
            });
        }
        return view;
    }

    private void getCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, CameraRequestCode);

        } else {
            scanner.startPreview();
            CameraPermission = true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CameraRequestCode){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                CameraPermission = true;
                scanner.startPreview();
            }
        }
    }
}