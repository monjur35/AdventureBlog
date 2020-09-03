package com.example.project.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {


    ImageView ImgUserPhoto;
    static int PReqCode =1;
    static int REQUESTCODE =1;
    Uri pickedImgUri ;

    private EditText userName,userEmail,userPass,userPass2;
    private ProgressBar progressBar;
    private Button regBtn;
    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName =findViewById(R.id.regNameID);
        userEmail =findViewById(R.id.LogInMailID);
        userPass =findViewById(R.id.LogInPassID);
        userPass2 =findViewById(R.id.regPass2ID);
        progressBar =findViewById(R.id.regProgressBarID);
        regBtn =findViewById(R.id.LogInButtonID);
        mAuth=FirebaseAuth.getInstance();

        progressBar.setVisibility(View.INVISIBLE);



        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                final String name=userName.getText().toString();
                final String email=userEmail.getText().toString();
                final String pass=userPass.getText().toString();
                final String pass2=userPass2.getText().toString();


                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || !pass.equals(pass2)){
                    showMessege("Please Verify all Field perfectly");
                    regBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                }
                else
                {
                    createUserAccount(name,email,pass);
                }


            }
        });






        ImgUserPhoto =findViewById(R.id.regUserImgID);
        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=21){
                    checkAndRequestForPermission();
                }
                else {
                    openGallery();
                }
            }
        });
    }





    private void createUserAccount(final String name, final String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    showMessege("Account Created Successfully");
                    updateUserInfo(name,pickedImgUri,mAuth.getCurrentUser());
                    //upadateUserTable(name,email);
                }
                else
                {
                    showMessege("Account Creation Failed"+task.getException().toString());
                    regBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }





    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {



        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photo");
        final StorageReference imageFilePath =mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name).setPhotoUri(uri).build();


                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    showMessege("Register Complete");
                                    updateUI();
                                }
                            }
                        });
                    }
                });

            }
        });
    }

    private void updateUI() {
        Intent homeActivity =new Intent(getApplicationContext(),Home.class);
        startActivity(homeActivity);
        finish();
    }


    private void showMessege(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

    }





    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);

    }






    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(RegisterActivity.this, "Accept For Required Permission", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }
        else
        {
            openGallery();
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==REQUESTCODE && data != null){
            pickedImgUri=data.getData();
            ImgUserPhoto.setImageURI(pickedImgUri);


        }
    }
}
