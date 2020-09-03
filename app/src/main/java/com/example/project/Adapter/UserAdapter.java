package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project.Model.User;
import com.example.project.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyHolder> {

    Context context;
    List<User>userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.row_users,parent,false);


        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String userImage =userList.get(position).getuImg();
        String userName =userList.get(position).getuName();

        holder.mNameTv.setText(userName);
        Glide.with(context).load(userList.get(position).getuImg()).into(holder.mAvaterTv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mAvaterTv;
        TextView mNameTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mAvaterTv=itemView.findViewById(R.id.searchUsersImgId);
            mNameTv=itemView.findViewById(R.id.searchUsersNameId);
        }
    }
}
