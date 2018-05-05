package com.quangannguyen.smartdoor;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class MoKhoaActivity extends AppCompatActivity {

    private Toolbar mToolBarMHMoKhoa;
    private EditText mEDTMaSo;
    private Button mBtnMoKhoa;

    private DatabaseReference mUserDB;
    private DatabaseReference mHouseDB;
    private DatabaseReference mFamilyDB;
    private DatabaseReference mHistoryDB;
    private FirebaseUser mCurrentUser;
    private String mUserID;
    private String mUserTopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_khoa);
        addControls();
        addFirebase();
        addEvents();
    }

    private void addEvents() {
        mBtnMoKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String code_pr = mEDTMaSo.getText().toString().trim();
                if(code_pr.equals("") == false) {

                    mUserDB.child(mUserID).child("hasHouse").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String check_hasHouse = dataSnapshot.getValue().toString();
                            if(check_hasHouse.equals("true")){

                                mHouseDB.child(mUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        House currentHouse = dataSnapshot.getValue(House.class);

                                        if(currentHouse.getCurrent_check().equals("false")){

                                            if(currentHouse.getCode_private().equals(code_pr) == true){

                                                mHouseDB.child(mUserID).child("current_check").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        add_history(mUserID, "", "true");
                                                        return;
                                                    }
                                                });

                                            }else{
                                                Toast.makeText(MoKhoaActivity.this, "Sai mã số bí mật. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                            }

                                        }else{
                                            Toast.makeText(MoKhoaActivity.this, "Cửa nhà của bạn đã được mở ra rồi!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }else{

                                mFamilyDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.getValue() != null){

                                            for(DataSnapshot familyData : dataSnapshot.getChildren()){
                                                mUserTopID = familyData.getKey().toString();
                                                mHouseDB.child(mUserTopID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.getValue() != null){
                                                            House currentHouse = dataSnapshot.getValue(House.class);

                                                            if(currentHouse.getCurrent_check().equals("false")){

                                                                if(currentHouse.getCode_private().equals(code_pr) == true){

                                                                    mHouseDB.child(mUserTopID).child("current_check").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            add_history(mUserTopID, mUserID, "true");
                                                                            return;
                                                                        }
                                                                    });

                                                                }else{
                                                                    Toast.makeText(MoKhoaActivity.this, "Sai mã số bí mật. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }else{
                                                                Toast.makeText(MoKhoaActivity.this, "Cửa nhà của bạn đã được mở ra rồi!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                        }else{
                                            Toast.makeText(MoKhoaActivity.this, "Bạn không có nhà để mở khóa!", Toast.LENGTH_SHORT).show();
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

                }else{
                    Toast.makeText(MoKhoaActivity.this, "Vui lòng nhập vào mã số bí mật của bạn!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void add_history(final String mParentUserID, String mSubUserID, String state) {


        final String ts = DateFormat.getDateTimeInstance().format(new Date());

        if(mSubUserID.equals("")){

            mUserDB.child(mParentUserID).child("ID").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HistoryFB mCurrentHistoryFB = new HistoryFB(dataSnapshot.getValue().toString(), ts, state);
                    mHistoryDB.child(mParentUserID).push().setValue(mCurrentHistoryFB).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MoKhoaActivity.this, "Mở khóa thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(MoKhoaActivity.this, "Mở khóa thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            mUserDB.child(mSubUserID).child("ID").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HistoryFB mCurrentHistoryFB = new HistoryFB(dataSnapshot.getValue().toString(), ts, state);
                    mHistoryDB.child(mParentUserID).push().setValue(mCurrentHistoryFB).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MoKhoaActivity.this, "Mở khóa thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(MoKhoaActivity.this, "Mở khóa thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    private void addFirebase() {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserID = mCurrentUser.getUid();

        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        mHouseDB = FirebaseDatabase.getInstance().getReference().child("Houses");
        mHistoryDB = FirebaseDatabase.getInstance().getReference().child("Histories");
        mFamilyDB = FirebaseDatabase.getInstance().getReference().child("Families").child(mUserID);
    }

    private void addControls() {
        mToolBarMHMoKhoa = findViewById(R.id.tb_MHMoKhoa);
        setSupportActionBar(mToolBarMHMoKhoa);
        getSupportActionBar().setTitle("Mở Khóa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEDTMaSo = findViewById(R.id.edt_MaSoBiMat);
        mBtnMoKhoa = findViewById(R.id.btn_MoKhoa);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
