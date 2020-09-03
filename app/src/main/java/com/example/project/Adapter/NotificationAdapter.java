package com.example.project.Adapter;

import android.app.Notification;
import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project.Model.Comment;
import com.example.project.Model.Post;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Comment> mData;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public NotificationAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_notification,parent,false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(mData.get(position).getuImg()).into(holder.userImage);
        holder.username.setText(mData.get(position).getuName());
        //Glide.with(mContext).load(mData.get(position).).into(holder.postImage);



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


         ImageView userImage,postImage;
         TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            userImage=itemView.findViewById(R.id.notification_userImage);
            postImage=itemView.findViewById(R.id.postImage);
            username=itemView.findViewById(R.id.notification_user_name);


        }
    }



}
