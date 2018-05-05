package com.quangannguyen.smartdoor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {


    public UserFragment() {
        // Required empty public constructor
    }


    private RecyclerView mRVUser;
    private View mMainView;
    private UserAdapter mUserAdapter;

    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDB;
    private String mUserID;
    private ArrayList<User> mUserLists;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_user, container, false);

        addControls();
        addFirebase();
        return mMainView;
    }

    private void addFirebase() {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserID = mCurrentUser.getUid();
        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");

        load_users();

    }

    private void load_users() {

        mUserDB.child(mUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String ID = dataSnapshot.getValue(User.class).getID();
                mUserDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUserLists.clear();
                        for(DataSnapshot userData : dataSnapshot.getChildren()){
                            if(!userData.getValue(User.class).getID().equals(ID)){
                                mUserLists.add(userData.getValue(User.class));
                                mUserAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addControls() {

        mRVUser = mMainView.findViewById(R.id.rv_user);
        mRVUser.setHasFixedSize(true);
        mRVUser.setLayoutManager(new LinearLayoutManager(getContext()));

        mUserLists = new ArrayList<>();
        mUserAdapter = new UserAdapter(mUserLists,getActivity().getApplicationContext());
        mRVUser.setAdapter(mUserAdapter);

    }

}
