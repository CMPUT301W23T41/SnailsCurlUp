package com.example.snailscurlup.ui.scan;


import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snailscurlup.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * AbstractQRCommentsGalleryAdapter
 * <p>
 * This class is the adapter for the RecyclerView of  of comment section in QRIntoDialogFragment
 * It is used to display the comments in the comment section.
 *
 * @author AyanB123
 */
public class AbstractQRCommentsGalleryAdapter extends RecyclerView.Adapter<AbstractQRCommentsGalleryAdapter.ViewHolder> {

    private final Context context;

    // stores comments belong to a chosen AbstractQR code from QRGalleryAdapter
    private final ArrayList<AbstractQRComment> QRComments;

    public AbstractQRCommentsGalleryAdapter(Context context, ArrayList<AbstractQRComment> QRComments) {
        this.context = context;
        this.QRComments = QRComments;
    }

    @NonNull
    @Override
    public AbstractQRCommentsGalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment_card, parent, false);

        // set width of card to match parent prevent issue of card not filling width of recycler view
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = parent.getWidth();
        view.setLayoutParams(layoutParams);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AbstractQRComment singleComment = QRComments.get(holder.getAdapterPosition());


        // Set username and message of comment
        holder.commentUsername.setText(singleComment.getUsername());
        holder.commentMessage.setText(singleComment.getMessage());


        // generate relative time span
        long commentTime = singleComment.getTimestamp().getTime();
        long nowTime = System.currentTimeMillis();
        CharSequence ago = DateUtils.getRelativeTimeSpanString(commentTime, nowTime, DateUtils.SECOND_IN_MILLIS);

        holder.commentTimestamp.setText(ago.toString());

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        if (QRComments == null) {
            return 0;
        } else {
            return QRComments.size();
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public final ImageView commentUserImage;
        public final TextView commentUsername;
        public final TextView commentMessage;
        public final TextView commentTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // implement user profile image once implemented in user class
            //commentUserImage = itemView.findViewById(R.id.comment_user_img);
            commentUsername = itemView.findViewById(R.id.comment_username);
            commentMessage = itemView.findViewById(R.id.comment_message);
            commentTimestamp = itemView.findViewById(R.id.comment_timestamp);
        }
    }
}
