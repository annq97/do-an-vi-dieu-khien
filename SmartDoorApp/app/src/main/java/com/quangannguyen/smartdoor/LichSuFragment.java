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
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class LichSuFragment extends Fragment {

    private RecyclerView mRVLichSu;
    private View mMainView;
    private ArrayList<History> mHistoryLists;
    private ArrayList<HistoryFB> mHistoryFBLists;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDB;
    private DatabaseReference mFamilyDB;
    private DatabaseReference mHouseDB;
    private DatabaseReference mHistoryDB;
    private String mUserID;
    private String mUserDownID;
    private LichSuAdapter lichSuAdapter;
    private HistoryFB historyFB;
    public LichSuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_lich_su, container, false);

        addControls();
        addFirebase();

        return mMainView;
    }

    private void addFirebase() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserID = mCurrentUser.getUid();
        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        mFamilyDB = FirebaseDatabase.getInstance().getReference().child("Families");
        mHouseDB = FirebaseDatabase.getInstance().getReference().child("Houses");
        mHistoryDB = FirebaseDatabase.getInstance().getReference().child("Histories");

        load_history();
    }

    private void load_history() {

        mUserDB.child(mUserID).child("hasHouse").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String check_hasHouse = dataSnapshot.getValue().toString();
                if (check_hasHouse.equals("true")) {

                    mHistoryDB.child(mUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mHistoryLists.clear();
                            mHistoryFBLists.clear();
                            for(DataSnapshot historyData : dataSnapshot.getChildren()){
                                historyFB = historyData.getValue(HistoryFB.class);
                                mHistoryFBLists.add(historyFB);
                            }

                            Collections.reverse(mHistoryFBLists);

                            for(final HistoryFB historyFB_item : mHistoryFBLists){
                                mUserDB.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot userData : dataSnapshot.getChildren()){
                                            if(userData.getValue(User.class).getID().equals(historyFB_item.getID())){
                                                History history = new History(userData.getValue(User.class).getName(),historyFB_item.getDate(),historyFB_item.getState());
                                                mHistoryLists.add(history);
                                                lichSuAdapter.notifyDataSetChanged();
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
                } else {

                    mFamilyDB.child(mUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() != null){

                                for(DataSnapshot familyData : dataSnapshot.getChildren()){
                                    mUserDownID = familyData.getKey().toString(); //userID bố



                                    mHistoryDB.child(mUserDownID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mHistoryLists.clear();
                                            mHistoryFBLists.clear();
                                            for(DataSnapshot historyData : dataSnapshot.getChildren()){
                                                historyFB = historyData.getValue(HistoryFB.class);
                                                mHistoryFBLists.add(historyFB);
                                            }

                                            Collections.reverse(mHistoryFBLists);

                                            for(final HistoryFB historyFB_item : mHistoryFBLists){
                                                mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for(DataSnapshot userData : dataSnapshot.getChildren()){
                                                            if(userData.getValue(User.class).getID().equals(historyFB_item.getID())){
                                                                History history = new History(userData.getValue(User.class).getName(),historyFB_item.getDate(),historyFB_item.getState());
                                                                mHistoryLists.add(history);
                                                                lichSuAdapter.notifyDataSetChanged();
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
        mRVLichSu = mMainView.findViewById(R.id.rv_LichSu);
        mRVLichSu.setHasFixedSize(true);
        mRVLichSu.setLayoutManager(new LinearLayoutManager(getContext()));

        mHistoryLists = new ArrayList<>();
        lichSuAdapter = new LichSuAdapter(mHistoryLists,getActivity().getApplicationContext());
        mRVLichSu.setAdapter(lichSuAdapter);

        mHistoryFBLists = new ArrayList<>();
    }

}
