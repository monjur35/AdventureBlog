package com.example.project.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.project.Activities.Fragment.Home22Fragment;
import com.example.project.Activities.Fragment.ProfileFragment;
import com.example.project.Activities.Fragment.SettingsFragment;
import com.example.project.Activities.Fragment.UsersFragment;
import com.example.project.Model.Notification;
import com.example.project.Model.Post;
import com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Dialog popAddPost;
    ImageView popUpUserImage, addPostImage, addPostBtn;
    TextView addPostTitle, addPostDescription;
    ProgressBar addPostProgressBar;
    MenuItem menuItem;




    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;
    private Uri pickedImgUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        //ini addpost

        iniAddPost();
        setupAddPostImageClick();




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
        }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_tools,
                R.id.nav_share,
                R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateNavHeader();


        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new Home22Fragment()).commit();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                int id = Item.getItemId();
                if (id == R.id.nav_homeID) {
                    getSupportActionBar().setTitle("Home");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Home22Fragment()).addToBackStack(null).commit();

                } else if (id == R.id.nav_profileID) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ProfileFragment()).addToBackStack(null).commit();
                    getSupportActionBar().setTitle("Profile");


                } else if (id == R.id.nav_SettingsID) {
                    getSupportActionBar().setTitle("Settings");

                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new SettingsFragment()).addToBackStack(null).commit();

                } else if (id == R.id.nav_signOutID) {
                    FirebaseAuth.getInstance().signOut();
                    loginIntent();
                }


                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void notificationIntent() {
        Intent notificationIntent=new Intent(this, NotificationActivity.class);
        startActivity(notificationIntent);
    }

    private void setupAddPostImageClick() {

        addPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();
            }
        });

    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Home.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else {
            openGallery();
        }

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            addPostImage.setImageURI(pickedImgUri);

        }


    }


    private void iniAddPost() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;


        addPostTitle = popAddPost.findViewById(R.id.add_post_titleID);
        addPostDescription = popAddPost.findViewById(R.id.add_post_descID);
        popUpUserImage = popAddPost.findViewById(R.id.add_post_userImgID);
        addPostImage = popAddPost.findViewById(R.id.add_post_photo);
        addPostBtn = popAddPost.findViewById(R.id.add_post_create);
        addPostProgressBar = popAddPost.findViewById(R.id.add_post_progressBar);


        Glide.with(Home.this).load(currentUser.getPhotoUrl()).into(popUpUserImage);


        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addPostBtn.setVisibility(View.INVISIBLE);
                addPostProgressBar.setVisibility(View.VISIBLE);

                // we need to test all input fields (Title and description ) and post image

                if (!addPostTitle.getText().toString().isEmpty()
                        && !addPostDescription.getText().toString().isEmpty()
                        && pickedImgUri != null) {

                    //everything is okey no empty or null value
                    // Create Post Object and add it to firebase database
                    // first we need to upload post Image
                    // access firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownlaodLink = uri.toString();
                                    // create post Object
                                    Post post = new Post(addPostTitle.getText().toString(),
                                            addPostDescription.getText().toString(),
                                            imageDownlaodLink,
                                            currentUser.getUid(),
                                            currentUser.getPhotoUrl().toString());

                                    // Add post to firebase database

                                    addPost(post);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // something goes wrong uploading picture

                                    showMessage(e.getMessage());
                                    addPostProgressBar.setVisibility(View.INVISIBLE);
                                    addPostBtn.setVisibility(View.VISIBLE);


                                }
                            });


                        }
                    });


                } else {
                    showMessage("Please verify all input fields and choose Post Image");
                    addPostBtn.setVisibility(View.VISIBLE);
                    addPostProgressBar.setVisibility(View.INVISIBLE);

                }


            }
        });


    }


    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();


        String key = myRef.getKey();
        post.setPostKey(key);


        // add post data to firebase database

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added successfully");
                addPostProgressBar.setVisibility(View.INVISIBLE);
                addPostBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();

            }
        });


    }


    private void showMessage(String message) {

        Toast.makeText(Home.this, message, Toast.LENGTH_LONG).show();

    }





    private void loginIntent() {
        Intent loginActivity=new Intent(this,LogActivity.class);
        startActivity(loginActivity);
        finish();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        menuItem=menu.findItem(R.id.nav_notificationID);
        MenuItem item=menu.findItem(R.id.nav_searchID);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)){
                    searchUsers(query);
                }
                else
                {
                    getAllUsers();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    searchUsers(newText);
                }
                else
                {
                    getAllUsers();
                }


                return false;
            }
        });



        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                notificationIntent();

                return false;

            }
        });


        return true;
    }



    private void getAllUsers() {
        FirebaseUser fUser=FirebaseAuth.getInstance().getCurrentUser();
        StorageReference ref=FirebaseStorage.getInstance().getReference("users_photo");

    }

    private void searchUsers(String query) {

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }




    public void updateNavHeader(){
        NavigationView navigationView =findViewById(R.id.nav_view);
        View headerView =navigationView.getHeaderView(0);
        TextView navUsername=headerView.findViewById(R.id.nav_username);
        TextView navUsermail=headerView.findViewById(R.id.nav_user_mail);
        ImageView navUserphoto = headerView.findViewById(R.id.nav_user_photo);

        navUsername.setText(currentUser.getDisplayName());
        navUsermail.setText(currentUser.getEmail());

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserphoto);







    }


}
