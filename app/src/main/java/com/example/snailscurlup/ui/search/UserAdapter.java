package com.example.snailscurlup.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.snailscurlup.R;
import com.example.snailscurlup.Searched_User_Profile;
import com.example.snailscurlup.model.User;
import com.example.snailscurlup.ui.scan.QRCodeInstanceNew;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {

    private ArrayList<User> userList;
    private Context context;
    private User activeUser;
    private final String selectedUser = "selectedUser";

    private final String userKey = "User Object";
    private final String localID = "local Host's id";
    private final String timeStamp = "timeStamp";
    public UserAdapter(Context context, ArrayList<User> users, User activeUser){
        super(context,0, users);
        this.context = context;
        this.userList = users;
        this.activeUser = activeUser;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.search_user_bar, parent,false);
        }

       User user = userList.get(position);
        view.setClickable(true);

        TextView userName = view.findViewById((R.id.username));
        TextView totalScore = view.findViewById(R.id.totalScore);
        TextView totalQR = view.findViewById(R.id.totalQR);

        userName.setText("Name: "+user.getUsername());
        totalScore.setText("Total Score:"+user.getTotalScore());
        totalQR.setText("Number of QR: "+Integer.toString(user.getScannedInstanceQrCodes().size()));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Searched_User_Profile.class);
                int key = -1;
                intent.putExtra(userKey,user);
                intent.putExtra(selectedUser,user.getUsername());
                intent.putExtra(localID,activeUser);
                String[] strArr = new String[user.getScannedInstanceQrCodes().size()];
                for (int i=0;i<user.getScannedInstanceQrCodes().size();i++){
                    strArr[i] = user.getScannedInstanceQrCodes().get(i).getScanQRLogTimeStamp().toString();
                }
                intent.putExtra(timeStamp,strArr);
                ((Activity)context).startActivityForResult(intent,key);
            }
        });





        return view;

    }
}
