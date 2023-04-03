package com.example.snailscurlup.ui.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.snailscurlup.R;

import java.util.ArrayList;

/**
 * This class is used to display a list of leaders with their respective information in a ArrayAdapter.
 */
public class LeaderList extends ArrayAdapter<Leader> {

    private ArrayList<Leader> userList;
    private Context context;
    /**
     * Constructs a new LeaderList with the specified context and list of users.
     * @param context The current context.
     * @param users The list of Leader objects to display.
     */
    public LeaderList(Context context, ArrayList<Leader> users){
        super(context,0, users);
        this.context = context;
        this.userList = users;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     * If convertView is null, inflate a new view using LayoutInflater.
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.fragment_list_content, parent,false);
        }

        Leader user = userList.get(position);

        //ImageView image = view.findViewById(R.id.leader_image);
        TextView Name = view.findViewById(R.id.leader_name);
        TextView score = view.findViewById(R.id.leader_score);
        TextView leaderIndex = view.findViewById(R.id.leader_index);

        //image.setImageIcon(user.getUsername());
        Name.setText(user.getUsername());
        score.setText("Score: "+user.getScore());
        leaderIndex.setText(Integer.toString(position+1));
        return view;

    }
}