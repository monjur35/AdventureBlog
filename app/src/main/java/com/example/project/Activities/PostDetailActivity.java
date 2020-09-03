package com.example.project.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project.Adapter.CommentAdapter;
import com.example.project.Helpers.EditDialog;
import com.example.project.Model.Comment;
import com.example.project.R;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {
    ImageView imgPost,imgUserPost,imgCurrentUser;
    TextView textPostTitle,txtPostDescription,textPostDateName;
    EditText editTextComment;
    Button addBtnCmmt;
    String PostKey;
    TextView userName;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment>commentList;
    static String COMMENT_KEY ="Comment";

    private Intent HomeActivity;

    Dialog postEdit;
    Button editPhoto;
    Button deletePhoto;


    String Uid;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        Window w =getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //getSupportActionBar().hide();




        //postEdit =new Dialog(this);

        //editPhoto=postEdit.findViewById(R.id.edit_photoID);
        //deletePhoto=postEdit.findViewById(R.id.delete_photoID);



        imgPost =findViewById(R.id.post_detail_img);
        imgUserPost =findViewById(R.id.post_detail_user_img);
        imgCurrentUser =findViewById(R.id.post_detail_current_user_img);
        textPostTitle =findViewById(R.id.post_detail_title);
        txtPostDescription =findViewById(R.id.post_detail_Desc);
        textPostDateName =findViewById(R.id.post_detail_name_date);
        editTextComment =findViewById(R.id.post_detail_comment);
        addBtnCmmt =findViewById(R.id.post_detail_commnt_add_button);
        userName=findViewById(R.id.user_name);

        RvComment =findViewById(R.id.rv_commentID);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();















        addBtnCmmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextComment.getText().toString().isEmpty()){


                    addBtnCmmt.setVisibility(View.INVISIBLE);
                    DatabaseReference commentReference =firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();
                    String commentContent =editTextComment.getText().toString();
                    String uid=firebaseUser.getUid();
                    String uName=firebaseUser.getDisplayName();
                    String uImg=firebaseUser.getPhotoUrl().toString();
                    Comment comment =new Comment(commentContent,uid,uImg,uName);

                    commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showMsg("Comment added");
                            editTextComment.setText("");
                            addBtnCmmt.setVisibility(View.VISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMsg("Fail to add Comment :"+e.getMessage());
                        }
                    });

                }
                else
                {
                    showMsg("Please Input Text");
                }




            }
        });









        String postImage=getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imgPost);

        String postTitle =getIntent().getExtras().getString("title");
        textPostTitle.setText(postTitle);

        String userPostImage=getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userPostImage).into(imgUserPost);

        String postDescription  =getIntent().getExtras().getString("description");
        txtPostDescription.setText(postDescription);

        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);

        PostKey =getIntent().getExtras().getString("postKey");
        String date =timestampToString(getIntent().getExtras().getLong("postDate"));
        textPostDateName.setText(date);
        //userName.setText("by "+firebaseUser.getDisplayName());


        iniRvComment();






    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.post_detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_photo_id:
                showMsg( "Working ");
                return true;

            case R.id.remove_photo_id:

                deletePost();
                Intent homeActivity =new Intent(getApplicationContext(),Home.class);
                startActivity(homeActivity);
                showMsg("Success");
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePost() {
        final String uid = firebaseUser.getUid();
        final DatabaseReference deleteRef =firebaseDatabase.getReference("Posts").child(PostKey);




            deleteRef.child("userId").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currentUser=dataSnapshot.getValue().toString();


                if (currentUser==uid){
                    deleteRef.removeValue();
                }
                else{
                    showMsg("You can't delete this photo");
                }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




    }



    private void openDialog() {
         EditDialog editDialog =new EditDialog();
         editDialog.show(getSupportFragmentManager(),"edit dialog");



    }










    private void iniRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));


        DatabaseReference commentRef=firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList =new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()){

                    Comment comment =snap.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter =new CommentAdapter(getApplicationContext(),commentList);
                RvComment.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }







    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }







    private String  timestampToString(long times){
        Calendar calendar= Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(times);
        String date = DateFormat.format("EEE, dd MMM yyyy",calendar).toString();
        return date;

    }
}
