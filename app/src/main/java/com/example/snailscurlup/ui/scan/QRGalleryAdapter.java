package com.example.snailscurlup.ui.scan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snailscurlup.R;
import com.example.snailscurlup.model.User;
import com.example.snailscurlup.model.AllUsers;
import com.example.snailscurlup.model.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * QrGalleryAdapter
 * <p>
 * This class is the adapter for the RecyclerView of  QR Gallery.
 * It is used to display the QR codes in the gallery.
 *
 * @author AyanB123
 */

//
public class QRGalleryAdapter extends RecyclerView.Adapter<QRGalleryAdapter.Viewholder> {

    private final Context context;

    /*** commented out for new Abstract
     * private final ArrayList<QrCode> QRCodeArrayList; **/


    /***** new QRCodeInstanceNew are using  *****/
    private final ArrayList<QRCodeInstanceNew> QRCodeArrayList;

    // add FragmentManager variable to pass to QRInfoDialogFragment
    private final FragmentManager fragmentManager;
    private User selectedUser;

    AllUsers allUsers;
    User activeUser;


    // Constructor for initializing the values of context and data sent from Fragment works with Array Adapter
    public QRGalleryAdapter(Context context, ArrayList<QRCodeInstanceNew> QRCodeArrayList, FragmentManager fragmentManager) {
        this.context = context;
        this.QRCodeArrayList = QRCodeArrayList;

        // set FragmentManager variable to pass to QRInfoDialogFragment
        this.fragmentManager = fragmentManager;
    }
    public QRGalleryAdapter(Context context, ArrayList<QRCodeInstanceNew> QRCodeArrayList, FragmentManager fragmentManager,User u) {
        this.context = context;
        this.QRCodeArrayList = QRCodeArrayList;

        // set FragmentManager variable to pass to QRInfoDialogFragment
        this.fragmentManager = fragmentManager;
        this.selectedUser = u;
    }

    @NonNull
    @Override
    public QRGalleryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qrcode_gallery_card, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRGalleryAdapter.Viewholder holder, int position) {


        // to set data to textview and imageview of each card layout
        QRCodeInstanceNew singleqrcode = QRCodeArrayList.get(holder.getAdapterPosition());

        // set the Name and points  of the QR code
        holder.QRCodeName.setText(singleqrcode.getName());
        holder.QRCodeScore.setText(String.valueOf(singleqrcode.getPointsInt()));

        // format timestamp with simple Date format and set it to the textview
        String pattern = " M/d/yy, h:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String formattedTimestamp = simpleDateFormat.format(singleqrcode.getScanQRLogTimeStamp());
        holder.QRCodeTimeStamp.setText(formattedTimestamp);

        // set the QR code image using Picasso
        Picasso.get().load(singleqrcode.getAbstractQR().getGenUrl()).into(holder.QrCodeVisual);


        // set click listener on the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the position of the clicked item
                int position = holder.getAdapterPosition();
                // get the QR code instance corresponding to the clicked item
                QRCodeInstanceNew clickedQRCode = QRCodeArrayList.get(position);


                // create a new QRInfoDialogFragment and pass the QR code hash to it
                QRInfoDialogFragment dialogFragment = QRInfoDialogFragment.newInstance();
                Bundle bundle = new Bundle();
                dialogFragment.setSelectUser(selectedUser);
                bundle.putString("clickedQRCodeHash", clickedQRCode.AbstractQRHash());

                // set the arguments of the QRInfoDialogFragment
                dialogFragment.setArguments(bundle);

                dialogFragment.show(fragmentManager, "QRInfoDialogFragment");

            }
        });



        holder.DeleteQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllUsers allUsers = (AllUsers) context.getApplicationContext();

                // retrieve active user
                if (allUsers.getActiveUser() != null) {
                    activeUser = allUsers.getActiveUser();
                } else {
                    activeUser = new User();
                }



                // get the position of the clicked item
                int position = holder.getAdapterPosition();
                // get the QR code instance corresponding to the clicked item
                QRCodeInstanceNew clickedQRCode = QRCodeArrayList.get(position);

                allUsers.deleteUserScanQRCode(activeUser, clickedQRCode);
                notifyDataSetChanged();
            }
        });



        holder.QRCodeViewLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the position of the clicked item
                int position = holder.getAdapterPosition();
                // get the QR code instance corresponding to the clicked item
                QRCodeInstanceNew clickedQRCode = QRCodeArrayList.get(position);

                // create a new QRInstanceLogDialogFragment and pass the QR code details to it
                QRInstanceLogDialogFragment dialogFragment = QRInstanceLogDialogFragment.newInstance();

                Bundle bundle = new Bundle();
                bundle.putString("clickedQRCodeHash", clickedQRCode.AbstractQRHash());
                bundle.putParcelable("clickedQRCodeLogImage", clickedQRCode.getScanQRLogImage());
                bundle.putString("clickedQRCodeInstanceUserName", clickedQRCode.getScanQRInstanceUser().getUsername());
                bundle.putLong("clickedQRCodeTimeStamp", clickedQRCode.getScanQRLogTimeStamp().getTime());
                bundle.putString("clickedQRCodeLocation", clickedQRCode.getScanQRLogLocation());


                // set the arguments of the QRInstanceLogDialogFragment
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentManager, "QRInstanceLogDialogFragment");
            }
        });





    }



    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        if (QRCodeArrayList == null) {
            return 0;
        } else {
            return QRCodeArrayList.size();
        }
    }

    public void setQrInstanceList(ArrayList<QRCodeInstanceNew> newList) {
        QRCodeArrayList = new ArrayList<>();
        QRCodeArrayList = newList;
        notifyDataSetChanged();
    }




    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView QrCodeVisual;
        private final TextView QRCodeName;
        private final TextView QRCodeScore;

        private final TextView QRCodeTimeStamp;

        private final Button QRCodeViewComment;

        private final Button DeleteQRCode;

        private final Button QRCodeViewLog;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            QrCodeVisual = itemView.findViewById(R.id.qrcode_visual);
            QRCodeName = itemView.findViewById(R.id.qrcode_namefield);
            QRCodeScore = itemView.findViewById(R.id.qrcode_scorefield);
            QRCodeTimeStamp = itemView.findViewById(R.id.qrcode_timestampfield);
            QRCodeViewComment = itemView.findViewById(R.id.view_comments);
            DeleteQRCode = itemView.findViewById(R.id.delete_qrgallery_button);
            QRCodeViewLog = itemView.findViewById(R.id.view_log);


        }
    }
}
