package com.example.project.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.project.Adapter.NotificationAdapter;
import com.example.project.Model.Comment;
import com.example.project.Model.Notification;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    List<Notification> notificationList;

    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        getSupportActionBar().setTitle("Notification");
    }
}
