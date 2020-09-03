package com.example.project.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

public class LogActivity extends AppCompatActivity {


    private EditText userEmail,userPass;
    private Button logInBtn;
    private ProgressBar loginProgressBar;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    private Intent RegisterActivity;
    private TextView needAccount;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        userEmail=findViewById(R.id.LogInMailID);
        userPass=findViewById(R.id.LogInPassID);
        logInBtn=findViewById(R.id.LogInButtonID);
        loginProgressBar=findViewById(R.id.logInprogressBarID);
        mAuth=FirebaseAuth.getInstance();
        HomeActivity=new Intent(this,Home.class);
        RegisterActivity=new Intent(this,RegisterActivity.class);
        needAccount =findViewById(R.id.logIN_regID);

        loginProgressBar.setVisibility(View.INVISIBLE);


        needAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity);
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgressBar.setVisibility(View.VISIBLE);
                logInBtn.setVisibility(View.INVISIBLE);



                final String mail =userEmail.getText().toString();
                final  String password =userPass.getText().toString();

                if (mail.isEmpty() || password.isEmpty()){
                    showMsg("Please Verify All Field ");
                    logInBtn.setVisibility(View.VISIBLE);
                    loginProgressBar.setVisibility(View.INVISIBLE);


                }
                else
                {
                    signIn(mail,password);

                }
            }
        });

    }







    private void signIn(String mail, String password) {
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    showMsg("Log in Succesfull");
                    loginProgressBar.setVisibility(View.INVISIBLE);
                    logInBtn.setVisibility(View.VISIBLE);
                    updateUI();
                }
                else
                {
                    showMsg(task.getException().getMessage());
                    logInBtn.setVisibility(View.VISIBLE);
                    loginProgressBar.setVisibility(View.INVISIBLE);

                }

            }
        });
    }






    private void updateUI() {
        startActivity(HomeActivity);
        finish();

    }







    private void showMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user =mAuth.getCurrentUser();
        if (user!=null){
            updateUI();
        }
    }
}
