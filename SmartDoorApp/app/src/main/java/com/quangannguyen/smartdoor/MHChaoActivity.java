package com.quangannguyen.smartdoor;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MHChaoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDB;
    private String mUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhchao);
        CountDownTimer mTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if(currentUser == null){
                    Intent intent_chuyenMHMain = new Intent(MHChaoActivity.this,MHDangNhapActivity.class);
                    intent_chuyenMHMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent_chuyenMHMain);
                    finish();
                }else{

                    mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
                    mUserID = currentUser.getUid();

                    mUserDB.child(mUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User currentUser = dataSnapshot.getValue(User.class);
                            if(currentUser.getAdmin().equals("true")){
                                Intent intent_ChuyenMHMainAdmin = new Intent(MHChaoActivity.this,MHMainAdminActivity.class);
                                intent_ChuyenMHMainAdmin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent_ChuyenMHMainAdmin);
                                finish();
                            }else{
                                Intent intent_ChuyenMHMain = new Intent(MHChaoActivity.this,MainActivity.class);
                                intent_ChuyenMHMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent_ChuyenMHMain);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        }.start();
    }

}
