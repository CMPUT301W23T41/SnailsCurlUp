package com.example.snailscurlup.ui.scan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snailscurlup.R;
import com.example.snailscurlup.model.AllUsers;
import com.example.snailscurlup.model.User;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * QRInfoDialogFragment
 * <p>
 * This fragment displays the information about the QR code, when selected from Adapter
 * It also allows the user to add a comment to the QR code.
 * It also displays the comments that have been added to the QR code.
 *
 * @author AyanB123
 */
public class QRInfoDialogFragment extends DialogFragment {


    AllUsers allUsers;
    User activeUser;
    private Button dialogOkButton;
    private Button dialogAddCommentButton;
    private EditText addCommentEditText;
    private RecyclerView QRCommentGallery;
    private ImageView QRCodeImage;
    private TextView QRCodeName;
    private TextView QRCodePoints;
    private AbstractQR clickedQRCodeAbstract;

    private ArrayList<AbstractQRComment> clickedQRCodeComments;

    // This method sets the title and message for the dialog


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

    public static QRInfoDialogFragment newInstance() {
        QRInfoDialogFragment frag = new QRInfoDialogFragment();
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
        View view = inflater.inflate(R.layout.qrinfo_dialog_fragment, null);


        // Set the layout and buton and text  dialog
        QRCommentGallery = view.findViewById(R.id.comment_recycler_view);
        dialogOkButton = view.findViewById(R.id.dialog_ok_button);
        dialogAddCommentButton = view.findViewById(R.id.add_comment_button);
        addCommentEditText = view.findViewById(R.id.comment_box);
        QRCodeImage = view.findViewById(R.id.qrcode_image);
        QRCodeName = view.findViewById(R.id.QR_name);
        QRCodePoints = view.findViewById(R.id.QR_points);


        // get active user and userlist:
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


        //get hash of Qr code are wanting show:
        // Get the arguments passed to the fragment
        Bundle args = getArguments();
        String clickedQRCodeHash = args.getString("clickedQRCodeHash");

        //get AbstractQR object from hash:
        clickedQRCodeAbstract = activeUser.getAbstractQrCode(clickedQRCodeHash);

        //set QRCodeImage if Wifi access is available
        try {
            Picasso.get().load(clickedQRCodeAbstract.getURL()).into(QRCodeImage);

        } catch (Exception e) {
            Toast.makeText(getContext(), "QR Image cant be shown, check WIFI", Toast.LENGTH_LONG).show();
        }

        //set QRCodeName
        QRCodeName.setText(clickedQRCodeAbstract.getName());

        //set QRCodePoints
        String points = clickedQRCodeAbstract.getPointsInt() + " points";
        QRCodePoints.setText(points);

        //get QRCodeComments
        clickedQRCodeComments = clickedQRCodeAbstract.getQRcomments();


        //set QRCodeComments adapter
        setAdapter(clickedQRCodeComments);


        // Set the click listener for the add comment button
        dialogAddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText of comment
                String Newcomment = addCommentEditText.getText().toString();


                // Get the current timestamp of the comment
                long currentTimestamp = System.currentTimeMillis();
                Timestamp commentTimeStamp = new Timestamp(currentTimestamp);

                // Add the comment to the AbstractQR code list
                clickedQRCodeAbstract.addComment(activeUser.getUsername(), Newcomment, commentTimeStamp);

                // Update the comment list
                clickedQRCodeComments = clickedQRCodeAbstract.getQRcomments();

                // Update the adapter
                setAdapter(clickedQRCodeComments);

                // Clear the EditText and hide the keyboard
                addCommentEditText.setText("");
                hideKeyboardFrom(getContext(), view);


            }
        });

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
    public void setAdapter(ArrayList<AbstractQRComment> qrCodeComments) {

        // Set the adapter for the RecyclerView
        AbstractQRCommentsGalleryAdapter commmentGalleryAdapter = new AbstractQRCommentsGalleryAdapter(this.getContext(), qrCodeComments);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

        // Set the layout manager for the RecyclerView
        QRCommentGallery.setLayoutManager(linearLayoutManager);

        // Set the adapter for the RecyclerView
        QRCommentGallery.setAdapter(commmentGalleryAdapter);
    }
}
