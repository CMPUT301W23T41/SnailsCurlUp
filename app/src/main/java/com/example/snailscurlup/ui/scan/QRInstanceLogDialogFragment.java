package com.example.snailscurlup.ui.scan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.snailscurlup.R;
import com.example.snailscurlup.controllers.Database;
import com.example.snailscurlup.model.AllUsers;
import com.example.snailscurlup.model.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * QRInstanceLogDialogFragment
 * <p>
 * This fragment displays the information about the QR code, and log detail of it of user
 *
 * @author AyanB123
 */
public class QRInstanceLogDialogFragment extends DialogFragment {


    AllUsers allUsers;
    User activeUser;
    private Button dialogOkButton;
    private Button dialogAddCommentButton;

    // all for Abstract Qr they scanned
    private ImageView QRCodeImage;
    private TextView QRCodeName;
    private TextView QRCodePoints;
    private AbstractQR clickedQRCodeAbstract;

    // instance log detail of it
    private ImageView QRCodeScanLogImage;
    private TextView QRCodeScanLogUser;
    private TextView QRCodeScanLogTime;
    private TextView QRCodeScanLogLocation;



    /// datat to get data from passed form other gallery adapter
    private String clickedQRCodeHash;
    private Bitmap clickedQRCodeLogImage;
    private String clickedQRCodeInstanceUserName;
    private Timestamp clickedQRCodeTimeStamp;
    private String clickedQRCodeLocation;




    /*** Reference for how hide keybaord:
     * https://www.nexmobility.com/articles/hide-soft-keyboard-android.html
     * AUthor: not stated
     * Company: Nexmobility
     */
    public static void hideKeyboardFrom(Context context, View view) {
        // Hide the keyboard from the view
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public static QRInstanceLogDialogFragment newInstance() {
        QRInstanceLogDialogFragment frag = new QRInstanceLogDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }



//    @Override
//    public void onResume() {
//
//        super.onResume();
//        /******** PLEASE FIX THIS -> coment wont show one adapter closed and open again ********/
//        clickedQRCodeComments = clickedQRCodeAbstract.getQRcomments();
//        setAdapter(clickedQRCodeComments);
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.qrviewlog_dialog_fragment, null);


        // Set the layout and buton and text  dialog
      ;
        dialogOkButton = view.findViewById(R.id.dialog_ok_button);
        QRCodeImage = view.findViewById(R.id.qrcode_image);
        QRCodeName = view.findViewById(R.id.QR_name3);
        QRCodePoints = view.findViewById(R.id.QR_points);

        // instance log detail of it
        QRCodeScanLogImage = view.findViewById(R.id.qrcodelog_image);
        QRCodeScanLogUser = view.findViewById(R.id.qr_scanuser);
        QRCodeScanLogTime = view.findViewById(R.id.Qr_scantimestamp);
        QRCodeScanLogLocation = view.findViewById(R.id.Qr_scanlocation);




        // get active user and userlist:
        allUsers = (AllUsers) getActivity().getApplicationContext();

        // retrieve active user
        if (allUsers.getActiveUser() != null) {
            activeUser = allUsers.getActiveUser();
        } else {
            activeUser = new User();
        }


        //get hash of Qr code are wanting show:
        // Get the arguments passed to the fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            clickedQRCodeHash = bundle.getString("clickedQRCodeHash");
            clickedQRCodeLogImage = bundle.getParcelable("clickedQRCodeLogImage");
            clickedQRCodeInstanceUserName = bundle.getString("clickedQRCodeInstanceUserName");
            clickedQRCodeTimeStamp = new Timestamp(bundle.getLong("clickedQRCodeTimeStamp"));
            clickedQRCodeInstanceUserName = bundle.getString("clickedQRCodeInstanceUserName");
            clickedQRCodeLocation = bundle.getString("clickedQRCodeLocation");
        }

        Database db = Database.getInstance();

       db.setActiveUserQRInstancesList(activeUser, getContext());


        //get AbstractQR object from hash:
         clickedQRCodeAbstract = activeUser.getAbstractQrCode(clickedQRCodeHash);


        /**** setting Abstract qr info ****/
        //set QRCodeImage if Wifi access is available
        try {
            Picasso.get().load(clickedQRCodeAbstract.getGenUrl()).into(QRCodeImage);


        } catch (Exception e) {
            Toast.makeText(getContext(), "QR Image cant be shown, check WIFI", Toast.LENGTH_LONG).show();
        }


        //set QRCodeName
        QRCodeName.setText(clickedQRCodeAbstract.getName());

        //set QRCodePoints
        String points = clickedQRCodeAbstract.getPointsInt() + " points";
        QRCodePoints.setText(points);

        /******************/


        /**** setting Instance qr scan info ****/

        // set User Log intfo of QR code
        String scanUser = "Scanned By: " + clickedQRCodeInstanceUserName;
        QRCodeScanLogUser.setText(scanUser);

        // set Time Log intfo of QR code
        // format timestamp with simple Date format and set it to the textview
        String pattern = " M/d/yy, h:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String formattedTimestamp = simpleDateFormat.format(clickedQRCodeTimeStamp);
        String scanTimestamp = "Scanned Timestamp: " + formattedTimestamp ;
        QRCodeScanLogTime.setText(scanTimestamp);

        try {
            QRCodeScanLogLocation.setText(clickedQRCodeLocation);
            QRCodeScanLogImage.setImageBitmap(clickedQRCodeLogImage);


        } catch (Exception e) {
            Toast.makeText(getContext(), "QR Log Image/ Location Not saved", Toast.LENGTH_LONG).show();
            QRCodeScanLogLocation.setText("Location not recorded by User");

        }










        // Set the click listener for the OK button
        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the dialog
                dismiss();
            }
        });


        builder.setView(view);
        return builder.create();


    }

    // This method sets the adapter for the RecyclerView of COmments of AbstractQR
}
