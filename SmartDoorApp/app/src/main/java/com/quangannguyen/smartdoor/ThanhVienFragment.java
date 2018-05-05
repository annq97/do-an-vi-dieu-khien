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
public class ThanhVienFragment extends Fragment {

    private RecyclerView mRVThanhVien;
    private View mMainView;
    private ArrayList<User> mThanhVienLists;

    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDB;
    private DatabaseReference mFamilyDB;
    private String mUserID;
    private String mUserDownID;
    private ThanhVienAdapter thanhVienAdapter;
    public ThanhVienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_thanh_vien, container, false);
        addControls();
        addFirebase();

        return mMainView;
    }

    private void addFirebase() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserID = mCurrentUser.getUid();
        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        mFamilyDB = FirebaseDatabase.getInstance().getReference().child("Families");
        load_thanhvien();
    }

    private void load_thanhvien() {

        mUserDB.child(mUserID).child("hasHouse").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String check_hasHouse = dataSnapshot.getValue().toString();
                if (check_hasHouse.equals("true")) {

                    mFamilyDB.child(mUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot familyData : dataSnapshot.getChildren()){
                                String familyUserID = familyData.getKey();
                                mUserDB.child(familyUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        mThanhVienLists.add(user);
                                        thanhVienAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {

                    mFamilyDB.child(mUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() != null){

                                for(DataSnapshot familyData : dataSnapshot.getChildren()){

                                    mUserDownID = familyData.getKey().toString();
                                    mUserDB.child(mUserDownID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);
                                            mThanhVienLists.add(user);
                                            thanhVienAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    mFamilyDB.child(mUserDownID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot familyData : dataSnapshot.getChildren()){
                                                String familyUserID = familyData.getKey();
                                                mUserDB.child(familyUserID).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        User user = dataSnapshot.getValue(User.class);
                                                        if(dataSnapshot.getKey().equals(mUserID) != true){
                                                            mThanhVienLists.add(user);
                                                            thanhVienAdapter.notifyDataSetChanged();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addControls() {
        mRVThanhVien = mMainView.findViewById(R.id.rv_ThanhVien);
        mRVThanhVien.setHasFixedSize(true);
        mRVThanhVien.setLayoutManager(new LinearLayoutManager(getContext()));

        mThanhVienLists = new ArrayList<>();
        thanhVienAdapter = new ThanhVienAdapter(mThanhVienLists,getActivity().getApplicationContext());
        mRVThanhVien.setAdapter(thanhVienAdapter);
    }


}
