package com.example.project.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project.Model.Comment;
import com.example.project.R;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context mContext;
    private List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment,parent,false);

        return new CommentViewHolder(row);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Glide.with(mContext).load(mData.get(position).getuImg()).into(holder.img_user);
        holder.name.setText(mData.get(position).getuName());
        holder.content.setText(mData.get(position).getContent());
        holder.date.setText(timestampToString((long)mData.get(position).getTimestamp()));



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView img_user;
        TextView name,content,date;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            img_user =itemView.findViewById(R.id.comment_user_img);
            name =itemView.findViewById(R.id.comment_user_name);
            content =itemView.findViewById(R.id.comment_content);
            date =itemView.findViewById(R.id.comment_date);
        }
    }

    private String  timestampToString(long times) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(times);
        String date = DateFormat.format("hh:mm", calendar).toString();
        return date;

    }
}
