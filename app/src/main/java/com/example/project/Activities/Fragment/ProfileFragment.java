package com.example.project.Activities.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project.Adapter.PostAdepter;
import com.example.project.Model.Post;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;





        private OnFragmentInteractionListener mListener;

        RecyclerView postRecyclerView;
        PostAdepter postAdepter;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        List<Post> postList;

        FirebaseAuth mAuth;
        FirebaseUser currentUser;

        private String postKey;
        private String currentUserID;
        private TextView name;
        private TextView email;
        private ImageView profileImg;






        public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);





        return fragment;


    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        currentUserID=currentUser.getUid();







        View fragmentView =inflater.inflate(R.layout.fragment_profile, container, false);
        postRecyclerView =fragmentView.findViewById(R.id.rv_profile);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) );
        postRecyclerView.setHasFixedSize(true);
        firebaseDatabase =FirebaseDatabase.getInstance();

        databaseReference=firebaseDatabase.getReference("Posts");






        name=fragmentView.findViewById(R.id.profile_user_name);
        email=fragmentView.findViewById(R.id.profile_user_email);
        profileImg=fragmentView.findViewById(R.id.profile_pic);

//profile pic,name,mail set
        name.setText(currentUser.getDisplayName());
        email.setText(currentUser.getEmail());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(profileImg);

        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    @Override
    public void onStart() {
        super.onStart();

        databaseReference.orderByChild("userId").equalTo(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    postList.add(post);

                }
                postAdepter = new PostAdepter(getActivity(), postList);
                postRecyclerView.setAdapter(postAdepter);
                Collections.reverse(postList);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
