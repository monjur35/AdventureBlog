package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project.Activities.PostDetailActivity;
import com.example.project.Model.Post;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdepter extends RecyclerView.Adapter<PostAdepter.MyViewHolder> {

    Context mContext;
    List<Post> mData;



    public PostAdepter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);

        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imgTitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgUserProfile);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView imgTitle;
        ImageView imgPost;
        ImageView imgUserProfile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgTitle =itemView.findViewById(R.id.row_post_titleID);
            imgPost =itemView.findViewById(R.id.row_post_imageID);
            imgUserProfile =itemView.findViewById(R.id.row_post_user_imgID);





            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postDetailActivity =new Intent(mContext, PostDetailActivity.class);
                    int position =getAdapterPosition();


                    postDetailActivity.putExtra("title",mData.get(position).getTitle());
                    postDetailActivity.putExtra("postImage",mData.get(position).getPicture());
                    postDetailActivity.putExtra("description",mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey",mData.get(position).getPostKey());
                    postDetailActivity.putExtra("userPhoto",mData.get(position).getUserPhoto());
                    //postDetailActivity.putExtra("userName",mData.get(position).getUserName());
                    long timestramp = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("postDate",timestramp);
                    mContext.startActivity(postDetailActivity);
                }
            });
        }
    }
}
