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
public class HouseFragment extends Fragment {


    public HouseFragment() {
        // Required empty public constructor
    }


    private RecyclerView mRVHouse;
    private View mMainView;
    private HouseAdapter mHouseAdapter;
    private ArrayList<Item_House> mItem_HouseLists;
    private ArrayList<House> mHouseLists;
    private FirebaseUser mCurrentuser;
    private DatabaseReference mUserDB;
    private DatabaseReference mHouseDB;
    private String mUserID;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_house, container, false);

        addControls();
        addFirebase();

        return mMainView;
    }

    private void addFirebase() {

        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        mUserID = mCurrentuser.getUid();

        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        mHouseDB = FirebaseDatabase.getInstance().getReference().child("Houses");

        load_house();

    }

    private void load_house() {

        mHouseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mHouseLists.clear();
                mItem_HouseLists.clear();
                for(DataSnapshot houseData : dataSnapshot.getChildren()){
                    mHouseLists.add(houseData.getValue(House.class));
                }

                for(final House itemHouse : mHouseLists){

                    mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot userData : dataSnapshot.getChildren()){

                                if(userData.getValue(User.class).getID().equals(itemHouse.getID())){
                                    Item_House item_house = new Item_House(itemHouse.getID(), userData.getValue(User.class).getName());
                                    mItem_HouseLists.add(item_house);
                                    mHouseAdapter.notifyDataSetChanged();
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

        mRVHouse = mMainView.findViewById(R.id.rv_House);
        mRVHouse.setHasFixedSize(true);
        mRVHouse.setLayoutManager(new LinearLayoutManager(getContext()));

        mItem_HouseLists = new ArrayList<>();
        mHouseAdapter = new HouseAdapter(mItem_HouseLists,getActivity().getApplicationContext());
        mRVHouse.setAdapter(mHouseAdapter);


        mHouseLists = new ArrayList<>();

    }

}
