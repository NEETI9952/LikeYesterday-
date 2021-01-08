package com.example.likeyesterday.MyFriends;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.likeyesterday.R;

import java.util.List;

import static com.example.likeyesterday.ProfileFragment.uid;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;
    private Context context;
    private List<FeedItem> feedlist;

    public FeedAdapter(Context context, List<FeedItem> feedlist) {
        this.context = context;
        this.feedlist = feedlist;
    }


    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==MSG_TYPE_LEFT){
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        FeedItem feedItem= feedlist.get(position);
        holder.descriptionTV.setText(feedItem.getDescription());
        Glide.with(context).load(feedItem.getImage()).into(holder.imageView);
        holder.dateTV.setText(feedItem.getDate());
        holder.timeTV.setText(feedItem.getTime());
        Log.i("recyclertest","test: "+feedItem.getDescription());

    }

    @Override
    public int getItemCount() {
        return feedlist.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionTV,dateTV,timeTV;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTV=itemView.findViewById(R.id.chatDescription);
            imageView=itemView.findViewById(R.id.chatImageView);
            dateTV=itemView.findViewById(R.id.chatDate);
            timeTV=itemView.findViewById(R.id.chatTime);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(feedlist.get(position).getSender().equals(uid)){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
